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

    private static final Map<StatusEffect, Integer> effectPrices = new HashMap<>();
    private static String[][] buttons;

    static {
        initEffectPrices();
        initEffectsTable();
    }

    private static void initEffectPrices() {
        for (Map<String, Object> effectMap : Effects.effects) {
            StatusEffect effect = (StatusEffect) effectMap.get("effect");
            int price = (int) effectMap.get("price");
            effectPrices.put(effect, price);
        }
    }

    public static void execute(Player player) {
        if (player != null) {
            openGui(player);
        }
    }

    private static void openGui(Player player) {
        if (buttons == null || buttons.length == 0) {
            initEffectsTable();
        }
        Call.menu(player.con, menu, Bundle.get("menu.effects.title", player.locale()), "", buttons);
    }

    private static final int menu = Menus.registerMenu((player, option) -> {
        if (option >= 0 && option < Effects.effects.size()) {
            StatusEffect effect = (StatusEffect) Effects.effects.get(option).get("effect");
            buyEffect(effect, player);
        }
    });

    private static void initEffectsTable() {
        int rows = Effects.effects.size();
        buttons = new String[rows][1];

        for (int i = 0; i < rows; i++) {
            StatusEffect effect = (StatusEffect) Effects.effects.get(i).get("effect");
            int effectPrice = effectPrices.get(effect);
            String effectName = (String) Effects.effects.get(i).get("name");
            buttons[i][0] = effect.emoji() + " " + effectName + " Total Price: " + effectPrice;
        }
    }

    private static void buyEffect(StatusEffect effect, Player player) {
        PlayerData playerData = Players.getPlayer(player);
        int effectPrice = effectPrices.get(effect);
        UnitType currentUnitType = player.unit().type();

        int currentUnitPrice = getCurrentUnitPrice(currentUnitType);
        if (currentUnitPrice == -1) {
            player.sendMessage("Error: Unit type not found in UnitsTable.");
            return;
        }

        int additionalPrice = (int) (currentUnitPrice * 0.75);
        int totalPrice = effectPrice + additionalPrice;

        if (playerData.getCash() >= totalPrice) {
            applyEffectToPlayer(effect, player, playerData, totalPrice, effectPrice, additionalPrice);
        } else {
            player.sendMessage(Bundle.get("menu.effects.not-enough", player.locale()));
        }
    }

    private static int getCurrentUnitPrice(UnitType unitType) {
        for (Map<String, Object> unitMap : UnitsTable.units) {
            if (unitMap.get("unit") == unitType) {
                return (int) unitMap.get("price");
            }
        }
        return -1;
    }

    private static void applyEffectToPlayer(StatusEffect effect, Player player, PlayerData playerData, int totalPrice, int effectPrice, int additionalPrice) {
        playerData.subtractCash(totalPrice);
        player.unit().apply(effect, Float.POSITIVE_INFINITY);
        player.sendMessage(effect.emoji() + " " + Bundle.get("effect.bought.with.additional", player.locale()) + effectPrice + " + " + additionalPrice + " = " + totalPrice);
    }
}
