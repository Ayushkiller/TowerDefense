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

        for (int i =  0; i < Currency.itemsforcore.length; i++) {
            for (int j =  0; j < Currency.itemsforcore[i].length; j++) {
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
                Item selectedItem = Currency.itemsforcore[option / Currency.itemsforcore[0].length][option % Currency.itemsforcore[0].length];
                int adjustment = Integer.parseInt(buttons[0][opt]);

                if (adjustment <   0) {
                    sendMessageToPlayer(player, "menu.buypoint.negativeQuantity");
                    return;
                }

                quantities.put(selectedItem, quantities.getOrDefault(selectedItem,   0) + adjustment);

                // Reopen the QuantityAdjustmentMenu with updated quantities
                openQuantityAdjustmentMenu(player, option);
            } else if (opt ==   6) { // Buy button
                if (hasEnoughItems(player.team(), option, player)) {
                    openConfirmPurchaseMenu(player, option);
                } else {
                    sendMessageToPlayer(player, "critical.resource");
                    // Reset selectedItems for the player
                    selectedItemsQuantities.put(player, new HashMap<>());
                    // Reopen the QuantityAdjustmentMenu
                    openQuantityAdjustmentMenu(player, option);
                }
            } else if (opt ==   7) { // Close button
                sendMessageToPlayer(player, "menu.buypoint.close");
            } else if (opt ==   8) { // Back button
                openMenu(player);
            }
        }), title, description, buttons);
    }
  


    private static void openConfirmPurchaseMenu(Player player, int option) {
        String title = "Confirm Purchase";
        String description = "Are you sure you want to purchase?";
        String[][] buttons = new String[][]{{"Buy", "Cancel"}};

        Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
            if (opt ==   0) { // If the player clicks "Buy"
                Team team = player.team();
                if (hasEnoughItems(team, option, player)) {
                    Map<Item, Integer> selectedItems = selectedItemsQuantities.get(player);
                    int totalPoints = calculateTotalPoints(selectedItems); // Call the new method
                    // Add the total points to the player's points
                    PlayerData playerData = Players.getPlayer(player);
                    playerData.addPoints(totalPoints, player);

                    // Remove the selected items from the team's inventory
                    removeItemsFromTeam(team, selectedItems);

                    // Clear the selected items quantities for the player
                    selectedItemsQuantities.remove(player);

                    player.sendMessage(Bundle.get("menu.buypoint.success"));
                    // Return to the QuantityAdjustmentMenu after a successful purchase
                    openQuantityAdjustmentMenu(player, option);
                } else {
                    player.sendMessage(Bundle.get("critical.resource")); // Use a different message for insufficient resources
                }
            } else { // If the player clicks "Cancel"
                player.sendMessage(Bundle.get("menu.buypoint.cancel"));
                // Return to the QuantityAdjustmentMenu after cancelling
                openQuantityAdjustmentMenu(player, option);
            }
        }), title, description, buttons);
    }

    
    private static boolean hasEnoughItems(Team team, int option, Player player) {
        Map<Item, Integer> selectedItems = selectedItemsQuantities.get(player);
        if (selectedItems == null) {
            // If selectedItems is null, initialize it with an empty map
            selectedItems = new HashMap<>();
            selectedItemsQuantities.put(player, selectedItems);
        }
        for (int i =   0; i < Currency.itemsforcore.length; i++) {
            for (int j =   0; j < Currency.itemsforcore[i].length; j++) {
                Item item = Currency.itemsforcore[i][j];
                int requiredAmount = selectedItems.getOrDefault(item,   0); // Use the quantity the player wants to purchase
                if (team.items().get(item) < requiredAmount || team.items().get(item) < Currency.MinQuantity[i][j]) {
                    return false;
                }
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
        int totalPoints =  0;
        for (java.util.Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
    
            // Iterate through rows of itemsforcore
            for (int i =  0; i < Currency.itemsforcore.length; i++) {
                // Iterate through items in the current row
                for (int j =  0; j < Currency.itemsforcore[i].length; j++) {
                    if (Currency.itemsforcore[i][j] == item) {
                        // Found the item, calculate the total points for this item based on the ratio of gain points to priceforitems
                        int pointGain = Currency.Gain[i][j];
                        int itemPrice = Currency.Priceforitems[i][j];
                        totalPoints += (float)pointGain / itemPrice * quantity;
                        break; // Break the inner loop as the item is found
                    }
                }
            }
        }
        return totalPoints;
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