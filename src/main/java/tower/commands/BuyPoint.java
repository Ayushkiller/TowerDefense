package tower.commands;

import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.ui.Menus;
import tower.Domain.PlayerData;
import tower.Bundle;
import tower.Players;
import tower.Domain.Currency;
import java.util.Map;
import java.util.HashMap;

public class BuyPoint {
    private static Map<Player, Map<Item, Integer>> selectedItemsQuantities = new HashMap<>();

    public static void execute(Player player) {
        openMenu(player);
    }

    private static void openMenu(Player player) {
        String[][] buttons = new String[Currency.itemsforcore.length][Currency.itemsforcore[0].length];
    
        for (int i =   0; i < Currency.itemsforcore.length; i++) {
            for (int j =   0; j < Currency.itemsforcore[i].length; j++) {
                Item item = Currency.itemsforcore[i][j];
                int gain = Currency.Gain[i][j];
                int price = Currency.Priceforitems[i][j];
                buttons[i][j] = String.format("[lime]%s (+%d) [gray]Price: %d", item.emoji(), gain, price);
            }
        }
    
        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            openQuantityAdjustmentMenu(player, option);
        }), Bundle.get("menu.buypoint.title", player.locale()), "", buttons);
    }
    
    private static void openQuantityAdjustmentMenu(Player player, int option) {
        // Iterate through rows
        for (int row =   0; row < Currency.itemsforcore.length; row++) {
            // Iterate through columns
            for (int col =   0; col < Currency.itemsforcore[row].length; col++) {
                // Calculate the index based on the current row and column
                int index = row * Currency.itemsforcore[row].length + col;
    
                // Check if the calculated index matches the option
                if (index == option) {
                    // Use row and col to access elements in Currency.itemsforcore and Currency.MinQuantity
                    Item selectedItem = Currency.itemsforcore[row][col];
    
                    // Set up the menu for quantity adjustment
                    String title = "Adjust Quantity";
                    Map<Item, Integer> quantities = getSelectedItemsQuantities(player);
                    String updatedQuantities = "";
                    for (Map.Entry<Item, Integer> entry : quantities.entrySet()) {
                        updatedQuantities += entry.getKey().emoji() + ": " + entry.getValue() + "\n";
                    }
                    String description = "Select quantity adjustment\n\n" + updatedQuantities;
                    String[][] buttons = new String[][]{{"-1000", "-100", "-50", "+50", "+100", "+1000"}, {"Buy", "Close", "Back"}};
    
    
                    Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
                        if (opt <   6) { // Adjustment buttons
                            int adjustment = Integer.parseInt(buttons[0][opt]);
                            if (adjustment <   0) {
                                sendMessageToPlayer(player, "menu.buypoint.negativeQuantity");
                                return;
                            }
                            quantities.put(selectedItem, quantities.getOrDefault(selectedItem,   0) + adjustment);
                            openQuantityAdjustmentMenu(player, option);
                        } else if (opt ==   6) { // Buy button
                            Map<Item, Integer> selectedItems = getSelectedItemsQuantities(player);
                            if (hasEnoughItems(player.team(), option, player)) {
                                int totalPoints = calculateTotalPoints(selectedItems);
                                PlayerData playerData = Players.getPlayer(player);
                                playerData.addPoints(totalPoints, player);
                                removeItemsFromTeam(player.team(), selectedItems);
                                selectedItemsQuantities.remove(player);
                                player.sendMessage(Bundle.get("menu.buypoint.success"));
                            } else {
                                player.sendMessage(Bundle.get("critical.resource"));
                                selectedItemsQuantities.put(player, new HashMap<>());
                            }
                        } else if (opt ==   7) { // Close button
                            player.sendMessage(Bundle.get("menu.buypoint.close"));
                            selectedItemsQuantities.remove(player);
                        } else if (opt ==   8) { // Back button
                            openMenu(player);
                        }
                    }), title, description, buttons);
    
    
                    return; // Exit the method once the matching item is found
                }
            }
        }
    
        // If no matching item is found, send an error message
        player.sendMessage("Invalid selection. Please try again.");
    }
    private static boolean hasEnoughItems(Team team, int option, Player player) {
        Map<Item, Integer> selectedItems = getSelectedItemsQuantities(player);
        for (int i =   0; i < Currency.itemsforcore.length; i++) {
            for (int j =   0; j < Currency.itemsforcore[i].length; j++) {
                Item item = Currency.itemsforcore[i][j];
                int requiredAmount = selectedItems.getOrDefault(item,   0); // Use the quantity the player wants to purchase
                if (team.items().get(item) < requiredAmount) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int calculateTotalPoints(java.util.Map<Item, Integer> selectedItems) {
        int totalPoints =   0;
        for (java.util.Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            int minQuantity = getMinQuantityForItem(item);
            int actualQuantity = Math.min(quantity, minQuantity);
            int excessQuantity = Math.max(0, quantity - minQuantity);
    
            // Initialize i and j outside the loop to make them accessible for excess quantity calculation
            int i = -1;
            int j = -1;
    
            // Calculate points for actualQuantity
            for (int row =  0; row < Currency.itemsforcore.length; row++) {
                for (int col =  0; col < Currency.itemsforcore[row].length; col++) {
                    if (Currency.itemsforcore[row][col] == item) {
                        i = row;
                        j = col;
                        int pointGain = Currency.Gain[i][j];
                        int itemPrice = Currency.Priceforitems[i][j];
                        totalPoints += (float)pointGain / itemPrice * actualQuantity;
                        break;
                    }
                }
                if (i != -1 && j != -1) {
                    break; // Break the outer loop if the item is found
                }
            }
    
            // If there's excess quantity, calculate points for it
            if (excessQuantity >   0) {
                
                totalPoints += (float)Currency.Gain[i][j] / Currency.Priceforitems[i][j] * excessQuantity;
            }
        }
        return totalPoints;
    }

    private static int getMinQuantityForItem(Item item) {
        for (int i =   0; i < Currency.itemsforcore.length; i++) {
            for (int j =   0; j < Currency.itemsforcore[i].length; j++) {
                if (Currency.itemsforcore[i][j] == item) {
                    return Currency.MinQuantity[i][j];
                }
            }
        }
        return  0; // Return  0 if the item is not found in the itemsforcore array
    }
    private static void removeItemsFromTeam(Team team, java.util.Map<Item, Integer> selectedItems) {
        for (java.util.Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            int minQuantity = getMinQuantityForItem(item);
            int actualQuantity = Math.min(quantity, minQuantity);
            team.items().remove(item, actualQuantity);
        }
    }
    public static void createAndDisplayMenu(Player player, String title, String description, String[][] buttons) {
        Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
            // Implement menu logic here
        }), title, description, buttons);
    }
    public static Map<Item, Integer> getSelectedItemsQuantities(Player player) {
        Map<Item, Integer> selectedItems = selectedItemsQuantities.get(player);
        if (selectedItems == null) {
            selectedItems = new HashMap<>();
            selectedItemsQuantities.put(player, selectedItems);
        }
        return selectedItems;
    }
    public static void sendMessageToPlayer(Player player, String messageKey) {
        player.sendMessage(Bundle.get(messageKey, player.locale()));
    }


}
