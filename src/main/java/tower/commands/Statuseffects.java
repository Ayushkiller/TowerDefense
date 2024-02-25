package tower.commands;

import java.util.HashMap;
import java.util.Map;
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
    private static final java.util.Map<StatusEffect, Integer> effectPrices = new HashMap<>();
    static {
        // Initialize effectPrices map with prices from Effects.Priceforeffects
        for (int i =   0; i < Effects.Effects.length; i++) {
            for (int j =   0; j < Effects.Effects[i].length; j++) {
                StatusEffect effect = Effects.Effects[i][j];
                int price = Effects.Priceforeffects[i][j];
                effectPrices.put(effect, price);
            }
        }
    }

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
        if (buttons == null || buttons.length ==   0) {
            initEffectsTable(player); // Pass the Player object here
        }

        // Display the menu to the player
        Call.menu(player.con, menu, Bundle.get("menu.effects.title", player.locale), "", buttons);
    }

    private static final int menu = Menus.registerMenu((player, option) -> {
        // Iterate through each row of the Effects.Effects array
        for (int i =   0; i < Effects.Effects.length; i++) {
            // Iterate through each column in the current row
            for (int j =   0; j < Effects.Effects[i].length; j++) {
                // Calculate the index based on the current row and column
                int index = i * Effects.Effects[i].length + j;
                // Check if the calculated index matches the option
                if (index == option) {
                    StatusEffect effect = Effects.Effects[i][j];
                    buyEffect(effect, player);
                    break; // Exit the loop once the matching effect is found
                }
            }
        }
    });

    private static String[][] buttons; // Declare the buttons variable here
    private static void initEffectsTable(Player player) {
        // Initialize the buttons array based on the size of Effects.Effects
        int rows = Effects.Effects.length;
        int columns = Effects.Effects[0].length;
        buttons = new String[rows][columns];
    
        for (int i =   0; i < Effects.Effects.length; i++) {
            for (int j =   0; j < Effects.Effects[i].length; j++) {
                StatusEffect effect = Effects.Effects[i][j];
                int effectPrice = effectPrices.get(effect);
                // Fetch the current unit's type
                UnitType currentUnitType = player.unit().type();
                // Find the position of the current unit type within the UnitsTable.units array
                int row = -1, column = -1;
                Map<String, Object> unitMap = null; // Declare unitMap outside the loop
                for (int k =   0; k < UnitsTable.units.size(); k++) {
                    unitMap = UnitsTable.units.get(k); // Use 'k' here
                    UnitType unitType = (UnitType) unitMap.get("unit");
                    if (unitType == currentUnitType) {
                        row = k; // Use 'k' here
                        column =   0; // Assuming you want to access the first element of the map
                        break;
                    }
                }
    
                // Now unitMap is accessible here
                if (row == -1 || column == -1) {
                    buttons[i][j] = effect.emoji() + " Error: Unit type not found";
                    continue;
                }
                int currentUnitPrice = (int) unitMap.get("price");
                // Calculate   75% of the current unit's price
                int additionalPrice = (int) (currentUnitPrice *   0.75);
                // Add the calculated amount to the status effect's price
                int totalPrice = effectPrice + additionalPrice;
                // Concatenate the emoji with the total price
                buttons[i][j] = effect.emoji() + " Total Price: " + totalPrice;
            }
        }
    }

    private static void buyEffect(StatusEffect effect, Player player) {
        PlayerData playerData = Players.getPlayer(player);
        int effectPrice = effectPrices.get(effect);

        // Fetch the current unit's type
        UnitType currentUnitType = player.unit().type();
        // Find the position of the current unit type within the UnitsTable.units array
        int row = -1, column = -1;
        Map<String, Object> unitMap = null; // Declare unitMap outside the loop
        for (int i =  0; i < UnitsTable.units.size(); i++) {
            unitMap = UnitsTable.units.get(i); // Use 'i' here
            UnitType unitType = (UnitType) unitMap.get("unit");
            if (unitType == currentUnitType) {
                row = i;
                column =  0; // Assuming you want to access the first element of the map
                break;
            }
        }

        // Check if the unit position was found and unitMap is not null
        if (row == -1 || column == -1 || unitMap == null) {
            player.sendMessage("Error: Unit type not found in UnitsTable or unitMap is null.");
            return;
        }

        // Check if unitMap contains the "price" key
        if (!unitMap.containsKey("price")) {
            player.sendMessage("Error: Price not found for the unit.");
            return;
        }

        // Directly access the price for the current unit type from the unitMap
        int currentUnitPrice = (int) unitMap.get("price");

        // Calculate  75% of the current unit's price
        int additionalPrice = (int) (currentUnitPrice *  0.75);
        // Add the calculated amount to the status effect's price
        int totalPrice = effectPrice + additionalPrice;

        if (playerData.getPoints() >= totalPrice) {
            // Directly apply the status effect with an infinite duration
            playerData.subtractPoints(totalPrice, player);
            player.unit().apply(effect, Float.POSITIVE_INFINITY);
            // Display the total price to the player
            player.sendMessage(Bundle.get("effect.bought.with.additional", player.locale) + totalPrice);
        } else {
            player.sendMessage(Bundle.get("menu.effects.not-enough", player.locale()));
        }
    }
}
