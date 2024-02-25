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
        String[][] buttons = new String[Currency.items.size()][1];

        for (int i =  0; i < Currency.items.size(); i++) {
            Map<String, Object> itemMap = Currency.items.get(i);
            Item item = (Item) itemMap.get("item");
            int gain = (int) itemMap.get("gain");
            int price = (int) itemMap.get("price");
            buttons[i][0] = String.format("%s (+%d) [gray]Price: %d", item.emoji(), gain, price);
        }

        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            openQuantityAdjustmentMenu(player, option);
        }), Bundle.get("menu.buypoint.title", player.locale()), "", buttons);
    }

    private static void openQuantityAdjustmentMenu(Player player, int option) {
        if (option >= Currency.items.size()) {
            player.sendMessage("Invalid selection. Please try again.");
            return;
        }

        Map<String, Object> selectedItemMap = Currency.items.get(option);
        Item selectedItem = (Item) selectedItemMap.get("item");

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
            if (opt <  6) { // Adjustment buttons
                int adjustment = Integer.parseInt(buttons[0][opt]);
                if (adjustment <  0) {
                    sendMessageToPlayer(player, "menu.buypoint.negativeQuantity");
                    return;
                }
                quantities.put(selectedItem, quantities.getOrDefault(selectedItem,  0) + adjustment);
                openQuantityAdjustmentMenu(player, option);
            } else if (opt ==  6) { // Buy button
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
            } else if (opt ==  7) { // Close button
                player.sendMessage(Bundle.get("menu.buypoint.close"));
                selectedItemsQuantities.remove(player);
            } else if (opt ==  8) { // Back button
                openMenu(player);
            }
        }), title, description, buttons);
    }

    private static boolean hasEnoughItems(Team team, int option, Player player) {
        Map<Item, Integer> selectedItems = getSelectedItemsQuantities(player);
        for (int i =  0; i < Currency.items.size(); i++) {
            Map<String, Object> itemMap = Currency.items.get(i);
            Item item = (Item) itemMap.get("item");
            int requiredAmount = selectedItems.getOrDefault(item,  0); // Use the quantity the player wants to purchase
            if (team.items().get(item) < requiredAmount) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unused")
    private static int calculateTotalPoints(java.util.Map<Item, Integer> selectedItems) {
        int totalPoints =  0;
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
            for (int row =  0; row < Currency.items.size(); row++) {
                Map<String, Object> itemMap = Currency.items.get(row);
                if (itemMap.get("item") == item) {
                    i = row;
                    j =  0; // Since we're using a single column for buttons, j is always  0
                    int pointGain = (int) itemMap.get("gain");
                    int itemPrice = (int) itemMap.get("price");
                    totalPoints += (float) pointGain / itemPrice * actualQuantity;
                    break;
                }
            }

            // If there's excess quantity, calculate points for it
            if (excessQuantity >  0) {
                totalPoints += (float) ((int) Currency.items.get(i).get("gain")) / ((int) Currency.items.get(i).get("price")) * excessQuantity;
            }
        }
        return totalPoints;
    }

    private static int getMinQuantityForItem(Item item) {
        for (int i =  0; i < Currency.items.size(); i++) {
            Map<String, Object> itemMap = Currency.items.get(i);
            if (itemMap.get("item") == item) {
                return (int) itemMap.get("minQuantity");
            }
        }
        return  0; // Return  0 if the item is not found in the items list
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