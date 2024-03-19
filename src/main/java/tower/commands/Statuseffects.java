package tower.commands;

import java.util.HashMap;
import java.util.Map;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;
import tower.Domain.Effects;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;

public class Statuseffects {

    private static final java.util.Map<StatusEffect, Integer> effectPrices = new HashMap<>();
    private static String[][] buttons; // Declare the buttons variable here

    static {
        initEffectPrices();
    }

    private static void initEffectPrices() {
        for (Map<String, Object> effectMap : Effects.effects) {
            StatusEffect effect = (StatusEffect) effectMap.get("effect");
            int price = (int) effectMap.get("price");
            effectPrices.put(effect, price);
        }
    }

    public static void execute(Player player) {
        if (player == null) {

            return;
        }

        openGui(player);
    }

    private static void openGui(Player player) {
        if (buttons == null || buttons.length == 0) {
            initEffectsTable(player);
        }

        Call.menu(player.con, menu, Bundle.get("menu.effects.title", player.locale), "", buttons);
    }

    private static final int menu = Menus.registerMenu((player, option) -> {
        for (int i = 0; i < Effects.effects.size(); i++) {
            int index = i;
            if (index == option) {
                StatusEffect effect = (StatusEffect) Effects.effects.get(i).get("effect");
                buyEffect(effect, player);
                break;
            }
        }
    });

    private static void initEffectsTable(Player player) {
        int rows = Effects.effects.size();
        buttons = new String[rows][1];

        for (int i = 0; i < Effects.effects.size(); i++) {
            StatusEffect effect = (StatusEffect) Effects.effects.get(i).get("effect");
            int effectPrice = effectPrices.get(effect);
            String effectName = (String) Effects.effects.get(i).get("name"); // Fetch the name of the effect
            buttons[i][0] = effect.emoji() + " " + effectName + " Total Price: " + effectPrice;
        }
    }

    private static void buyEffect(StatusEffect effect, Player player) {
        PlayerData playerData = Players.getPlayer(player);
        int effectPrice = effectPrices.get(effect);

        // Fetch the current unit's type
        UnitType currentUnitType = player.unit().type();
        // Find the position of the current unit type within the UnitsTable.units array
        int row = -1;
        Map<String, Object> unitMap = null; // Declare unitMap outside the loop
        for (int i = 0; i < UnitsTable.units.size(); i++) {
            unitMap = UnitsTable.units.get(i); // Use 'i' here
            UnitType unitType = (UnitType) unitMap.get("unit");
            if (unitType == currentUnitType) {
                row = i;
                break;
            }
        }

        // Check if the unit position was found and unitMap is not null
        if (row == -1 || unitMap == null) {
            player.sendMessage("Error: Unit type not found in UnitsTable or unitMap is null.");
            return;
        }

        // Directly access the price for the current unit type from the unitMap
        int currentUnitPrice = (int) unitMap.get("price");

        // Calculate 75% of the current unit's price
        int additionalPrice = (int) (currentUnitPrice * 0.75);
        // Add the calculated amount to the status effect's price
        int totalPrice = effectPrice + additionalPrice;

        if (playerData.getCash() >= totalPrice) {
            if (effect != null) { // Ensure the effect is not null before applying it
                playerData.subtractCash(totalPrice, player);
                player.unit().apply(effect, Float.POSITIVE_INFINITY);
                // Modify the message to include the emoji, base effect price, and the
                // additional price
                player.sendMessage(effect.emoji() + " " + Bundle.get("effect.bought.with.additional", player.locale)
                        + effectPrice + " + " + additionalPrice + " = " + totalPrice);
            } else {
                player.sendMessage("Error: The effect could not be applied. Please try again.");
            }
        } else {
            player.sendMessage(Bundle.get("menu.effects.not-enough", player.locale()));
        }
    }
}