package tower.commands;

import java.util.HashMap;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(Statuseffects.class.getName());

    public static void execute(Player player) {
        // Ensure the player object is not null
        if (player == null) {
            logger.severe("Player object is null.");
            return;
        }

        // Display the status effects menu to the player
        openGui(player);
    }

    private static void openGui(Player player) {
        // Ensure the buttons array is initialized
        if (buttons == null || buttons.length == 0) {
            initEffectsTable();
        }

        // Display the menu to the player
        Call.menu(player.con, menu, Bundle.get("menu.effects.title", player.locale), "", buttons);
    }

    private static final int menu = Menus.registerMenu((player, option) -> {
        StatusEffect effect = Effects.Effects[option / Effects.Effects[0].length][option % Effects.Effects[0].length];
        buyEffect(effect, player);
    });

    private static final java.util.Map<StatusEffect, Integer> effectPrices = new HashMap<>();
    private static String[][] buttons; // Declare the buttons variable here
    public static void initEffectsTable() {
        int numberOfRows = Effects.Effects.length;
        int numberOfColumns = Effects.Effects[0].length;

        buttons = new String[numberOfRows][numberOfColumns];

        for (int i = 0; i < Effects.Effects.length; i++) {
            for (int j = 0; j < Effects.Effects[i].length; j++) {
                StatusEffect effect = Effects.Effects[i][j];
                buttons[i][j] = effect.emoji(); // Assuming effect.emoji() returns a string representation of the effect
                effectPrices.put(effect, Effects.Priceforeffects[i][j]);
            }
        }
    }

    private static void buyEffect(StatusEffect effect, Player player) {
        PlayerData playerData = Players.getPlayer(player);
        int effectPrice = effectPrices.get(effect);

        // Fetch the current unit's type
        UnitType currentUnitType = player.unit().type();
        // Look up the current unit's price in UnitsTable.java
        Integer[] currentUnitPrice = UnitsTable.prices[currentUnitType.id];
        // Calculate 75% of the current unit's price
        int additionalPrice = (int) (currentUnitPrice[0] * 0.75);
        // Add the calculated amount to the status effect's price
        int totalPrice = effectPrice + additionalPrice;

        if (playerData.getPoints() >= totalPrice) {
            playerData.subtractPoints(totalPrice);
            // Ensure the unit ID is valid before applying the status effect
            if (currentUnitType.id >=   0) {
                // Apply the status effect with an infinite duration
                player.unit().apply(effect, Float.POSITIVE_INFINITY);
                // Display the total price to the player
                player.sendMessage(Bundle.get("effect.bought.with.additional", player.locale) + totalPrice);
            } else {
                // Include the incorrect unit ID in the error message
                player.sendMessage("Error: Invalid unit type ID: " + currentUnitType.id);
            }
        } else {
            player.sendMessage(Bundle.get("menu.effects.not-enough", player.locale()));
        }
    }
}