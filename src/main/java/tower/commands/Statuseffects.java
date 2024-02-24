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
        if (buttons == null || buttons.length ==  0) {
            initEffectsTable(player); // Pass the Player object here
        }
    
        // Display the menu to the player
        Call.menu(player.con, menu, Bundle.get("menu.effects.title", player.locale), "", buttons);
    }

    private static final int menu = Menus.registerMenu((player, option) -> {
        StatusEffect effect = Effects.Effects[option / Effects.Effects[0].length][option % Effects.Effects[0].length];
        buyEffect(effect, player);
    });

    private static String[][] buttons; // Declare the buttons variable here
    private static void initEffectsTable(Player player) {
        for (int i =   0; i < Effects.Effects.length; i++) {
            for (int j =   0; j < Effects.Effects[i].length; j++) {
                StatusEffect effect = Effects.Effects[i][j];
                int effectPrice = effectPrices.get(effect);
                // Fetch the current unit's type
                UnitType currentUnitType = player.unit().type();
                // Find the position of the current unit type within the UnitsTable.units array
                int row = -1, column = -1;
                for (int k =   0; k < UnitsTable.units.length; k++) {
                    for (int l =   0; l < UnitsTable.units[k].length; l++) {
                        if (UnitsTable.units[k][l] == currentUnitType) {
                            row = k;
                            column = l;
                            break;
                        }
                    }
                    if (row != -1) {
                        break;
                    }
                }
                // Check if the unit position was found
                if (row == -1 || column == -1) {
                    buttons[i][j] = effect.emoji() + " Error: Unit type not found";
                    continue;
                }
                // Directly access the price for the current unit type from the prices array using the row and column
                int currentUnitPrice = UnitsTable.prices[row][column];
                // Calculate  75% of the current unit's price
                int additionalPrice = (int) (currentUnitPrice *  0.75);
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
        for (int i =  0; i < UnitsTable.units.length; i++) {
            for (int j =  0; j < UnitsTable.units[i].length; j++) {
                if (UnitsTable.units[i][j] == currentUnitType) {
                    row = i;
                    column = j;
                    break;
                }
            }
            if (row != -1) {
                break;
            }
        }
    
        // Check if the unit position was found
        if (row == -1 || column == -1) {
            player.sendMessage("Error: Unit type not found in UnitsTable.");
            return;
        }
    
        // Directly access the price for the current unit type from the prices array using the unit position
        int currentUnitPrice = UnitsTable.prices[row][column];

        // Calculate   75% of the current unit's price
        int additionalPrice = (int) (currentUnitPrice *   0.75);
        // Add the calculated amount to the status effect's price
        int totalPrice = effectPrice + additionalPrice;
    
        if (playerData.getPoints() >= totalPrice) {
            playerData.subtractPoints(totalPrice, player);
            // Apply the status effect with an infinite duration
            player.unit().apply(effect, Float.POSITIVE_INFINITY);
            // Display the total price to the player
            player.sendMessage(Bundle.get("effect.bought.with.additional", player.locale) + totalPrice);
        } else {
            player.sendMessage(Bundle.get("menu.effects.not-enough", player.locale()));
        }
    }
}
