package tower.commands;

import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Playerc;
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
        String description = "Select quantity adjustment";
        String[][] buttons = new String[][]{{"-1000", "-100", "-50", "+50", "+100", "+1000"}};
    
        Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
            // Adjust the quantity based on the selected button
            Item selectedItem = Currency.itemsforcore[option / Currency.itemsforcore[0].length][option % Currency.itemsforcore[0].length];
            int adjustment = Integer.parseInt(buttons[0][opt]);
    
            // Check if the adjustment is negative and display an error message or ignore the action
            if (adjustment <  0) {
                player.sendMessage("You cannot set the quantity to a negative value.");
                return;
            }
    
            Map<Item, Integer> quantities = selectedItemsQuantities.computeIfAbsent(player, k -> new HashMap<>());
            quantities.put(selectedItem, quantities.getOrDefault(selectedItem,   0) + adjustment);
    
            // After adjusting the quantity, open the menu to confirm the purchase
            openConfirmPurchaseMenu(player, option);
        }), title, description, buttons);
    }


    private static void openConfirmPurchaseMenu(Player player, int option) {
        String title = "Confirm Purchase";
        String description = "Are you sure you want to purchase?";
        String[][] buttons = new String[][]{{"Buy", "Cancel"}};
    
        Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
            if (opt ==  0) { // If the player clicks "Buy"
                Team team = player.team();
                if (hasEnoughItems(team, option)) {
                    Map<Item, Integer> selectedItems = selectedItemsQuantities.get(player);
                    int totalPoints = calculateTotalPoints(selectedItems); // Call the new method
                    // Add the total points to the player's points
                    PlayerData playerData = Players.getPlayer(player);
                    playerData.addPoints(totalPoints);
    
                    // Remove the selected items from the team's inventory
                    removeItemsFromTeam(team, selectedItems);
    
                    player.sendMessage(Bundle.get("menu.buypoint.success"));
                } else {
                    player.sendMessage("You do not have enough items to complete this purchase.");
                }
            } else { // If the player clicks "Cancel"
                player.sendMessage(Bundle.get("menu.buypoint.cancel"));
            }
        }), title, description, buttons);
    }
    private static boolean hasEnoughItems(Team team, int option) {
        for (int i =  0; i < Currency.itemsforcore[option / Currency.itemsforcore[0].length].length; i++) {
            Item item = Currency.itemsforcore[option / Currency.itemsforcore[0].length][i];
            int requiredAmount = Currency.Priceforitems[option / Currency.itemsforcore[0].length][i];
            if (team.items().get(item) < requiredAmount) {
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
        int totalPoints =  0;
        for (java.util.Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            // Find the index of the item in the itemsforcore array
            int itemIndex = -1;
            for (int i =  0; i < Currency.itemsforcore.length; i++) {
                for (int j =  0; j < Currency.itemsforcore[i].length; j++) {
                    if (Currency.itemsforcore[i][j] == item) {
                        itemIndex = j;
                        break;
                    }
                }
                if (itemIndex != -1) break;
            }

            // If the item is found, calculate the total points for this item based on the ratio of gain points to priceforitems
            if (itemIndex != -1) {
                int pointGain = Currency.Gain[itemIndex / Currency.itemsforcore[0].length][itemIndex % Currency.itemsforcore[0].length];
                int itemPrice = Currency.Priceforitems[itemIndex / Currency.itemsforcore[0].length][itemIndex % Currency.itemsforcore[0].length];
                totalPoints += (float)pointGain / itemPrice * quantity;
            } else {
                // Handle the case where the item is not found in the itemsforcore array
                // This could involve logging an error, skipping the item, or handling it in another appropriate manner
                System.err.println("Item not found in itemsforcore array: " + ((Playerc) item).name());

            }
        }
        return totalPoints;
    }
}