package tower.commands;

import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;
import tower.Domain.Currency;
import tower.Domain.PlayerData;

/**
 * Handles the purchase of points in the tower defense game.
 * This class provides functionality to open a menu for players to buy points using items as currency.
 */

 public class BuyPoint {
    
    /**
     * Executes the purchase of points by opening the buy point menu for the player.
     *
     * @param player The player who is attempting to buy points.
     */

     public static void execute(Player player) {
        openBatchBuyMenu(player);
    }
    /**
     * Opens the buy point menu for the player.
     * The menu displays the items that can be used to purchase points and the corresponding point values.
     *
     * @param player The player for whom the menu is being opened.
     */
    private static void openBatchBuyMenu(Player player) {
        // Create a map to store selected items and quantities
        java.util.Map<Item, Integer> selectedItems = new java.util.HashMap<>();

        // Open menu with item buttons and quantity input fields
        Call.menu(player.con(), Menus.registerMenu((player1, option) -> {
            int tier = option / Currency.itemsforcore[0].length;
            int itemIndex = option % Currency.itemsforcore[0].length;
            Item item = Currency.itemsforcore[tier][itemIndex];

            // Prompt for quantity to purchase
            // Register a text input listener
            int textInputId = Menus.registerTextInput((pl, text) -> {
                try {
                    int quantity = Integer.parseInt(text);
                    if (quantity <=  0) {
                        throw new NumberFormatException("Quantity must be positive.");
                    }
                    selectedItems.put(item, quantity);

                    // Check if the player has enough items before proceeding with the purchase
                    if (hasEnoughItems(player.team(), selectedItems, player)) {
                        // Proceed with the purchase logic
                        // Remove items from the team's storage
                        removeItemsFromTeam(player.team(), selectedItems);

                        // Update the player's points
                        // Fetch the player's data
                        PlayerData playerData = Players.getPlayer(player);

                        // Calculate the total points to add based on the selected items and their quantities
                        int totalPoints = calculateTotalPoints(selectedItems);

                        // Update the player's points
                        playerData.addPoints(totalPoints);

                        // Optionally, send a confirmation message to the player
                        player.sendMessage(Bundle.get("menu.buypoint.success"));
                    } else {
                        // Inform the player that they do not have enough items
                        player.sendMessage(Bundle.get("menu.buypoint.error.notEnough"));
                    }
                } catch (NumberFormatException e) {
                    player1.sendMessage(Bundle.get("menu.buypoint.error.invalidQuantity"));
                }
            });

            // Open menu with item buttons and quantity input fields
            String title = "Buy here";
            String description = "Hi";
            String[][] buttons = new String[][]{{"Buy", "Close"}};
            Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
                // Prompt for quantity to purchase
                Menus.textInput(textInputId, Bundle.format("menu.buypoint.quantity", item.name), "",  5, "", false);
            }), title, description, buttons);
        }), "Buy Points Menu", "Select an item to purchase points", new String[][]{{"Item  1", "Item  2", "Item  3"}, {"Item  4", "Item  5", "Item  6"}});
    }
    private static boolean hasEnoughItems(Team team, java.util.Map<Item, Integer> selectedItems, Player player) {
        for (java.util.Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            int availableAmount = team.items().get(item);
            if (availableAmount < quantity) {
                player.sendMessage(Bundle.format("menu.buypoint.error.notEnough", item.name, availableAmount, quantity));
                return false;
            }
        }
        return true;
    }

    /**
     * Removes items from the team's storage based on the purchase.
     *
     * @param team The team from which to remove items.
     * @param selectedItems A map of items and their purchased quantities.
     */
    private static void removeItemsFromTeam(Team team, java.util.Map<Item, Integer> selectedItems) {
        for (java.util.Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            team.items().remove(item, quantity);
        }
    }
    private static int calculateTotalPoints(java.util.Map<Item, Integer> selectedItems) {
        int totalPoints =   0;
        for (java.util.Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
    
            // Find the index of the item in the itemsforcore array
            int itemIndex = -1;
            for (int i =   0; i < Currency.itemsforcore.length; i++) {
                for (int j =   0; j < Currency.itemsforcore[i].length; j++) {
                    if (Currency.itemsforcore[i][j] == item) {
                        itemIndex = j; // Assuming each item is unique across all tiers
                        break;
                    }
                }
                if (itemIndex != -1) break;
            }
    
            // If the item is found, calculate the total points for this item based on the ratio of gain points to priceforitems
            if (itemIndex != -1) {
                // Assuming the item's point gain is stored in the Gain array at the same index
                int pointGain = Currency.Gain[itemIndex / Currency.itemsforcore[0].length][itemIndex % Currency.itemsforcore[0].length];
                // Assuming the item's price is stored in the priceforitems array at the same index
                int itemPrice = Currency.Priceforitems[itemIndex / Currency.itemsforcore[0].length][itemIndex % Currency.itemsforcore[0].length];
    
                // Calculate the total points to add based on the ratio of gain points to priceforitems
                totalPoints += (pointGain / itemPrice) * quantity;
            }
        }
        return totalPoints;
    }
}