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
        openBuySellMenu(player);
    }

    private static void openBuySellMenu(Player player) {
        String[][] buttons = {
                { "[red]Buy Cash", "[cyan]Buy Items" }
        };

        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            if (option == 0) { // Buy button
                openMenu(player);
            } else if (option == 1) {
                openSellMenu(player);
            }
        }), "[red]Buy", "", buttons);
    }

    private static void openSellMenu(Player player) {
        String[][] buttons = new String[Currency.items.size()][1];

        for (int i = 0; i < Currency.items.size(); i++) {
            Map<String, Object> itemMap = Currency.items.get(i);
            Item item = (Item) itemMap.get("item");
            int gain = (int) itemMap.get("gain");
            int price = (int) itemMap.get("price");
            buttons[i][0] = String.format("%s (-%d) [gray]Price: %d", item.emoji(), gain, price);
        }

        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            openQuantityAdjustmentMenuForSell(player, option);
        }), Bundle.get("menu.sellpoint.title", player.locale()), "", buttons);
    }

    private static void openQuantityAdjustmentMenuForSell(Player player, int option) {
        if (option < 0 || option >= Currency.items.size()) {
            player.sendMessage("Invalid selection. Please try again.");
            return;
        }
        Map<String, Object> selectedItemMap = Currency.items.get(option);
        Item selectedItem = (Item) selectedItemMap.get("item");

        String title = "Adjust Quantity";
        Map<Item, Integer> quantities = getSelectedItemsQuantities(player);
        String updatedQuantities = "";
        for (Map.Entry<Item, Integer> entry : quantities.entrySet()) {
            updatedQuantities += entry.getKey().emoji() + ": " + entry.getValue() + "\n";
        }
        String description = "Select quantity to sell\n\n" + updatedQuantities;

        // Calculate total cash required to buy selected items
        int totalCashRequired = calculateTotalCashRequired(quantities);
        description += "\n\n[orange]Total Cash Required: " + totalCashRequired;

        String[][] buttons = new String[][] { { "-2000", "-1000", "-100", "+400", "+1000", "+2000" },
                { "Sell", "Close", "Back" } };

        Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
            if (opt < 6) { // Adjustment buttons
                int adjustment = Integer.parseInt(buttons[0][opt]);
                int currentQuantity = quantities.getOrDefault(selectedItem, 0);
                quantities.put(selectedItem, currentQuantity + adjustment);
                openQuantityAdjustmentMenuForSell(player, option);
            } else if (opt == 6) { // Sell button
                Map<Item, Integer> selectedItems = getSelectedItemsQuantities(player);

                int totalQuantity = selectedItems.values().stream().mapToInt(Integer::intValue).sum();
                if (totalQuantity < 0) {
                    sendMessageToPlayer(player, "menu.sellpoint.negativeQuantity");
                    return;
                }
                PlayerData playerData = Players.getPlayer(player);
                if (playerData.getCash() < totalCashRequired) {
                    sendMessageToPlayer(player, "critical.insufficientFunds");
                    return;
                }

                addItemsToTeam(player.team(), selectedItems);
                playerData.subtractCash(totalCashRequired, player);
                selectedItemsQuantities.remove(player);
                player.sendMessage(Bundle.get("menu.sellpoint.success"));
            } else if (opt == 7) { // Close button
                player.sendMessage(Bundle.get("menu.sellpoint.close"));
                selectedItemsQuantities.remove(player);
            } else if (opt == 8) { // Back button
                openSellMenu(player);
            }
        }), title, description, buttons);
    }

    private static int calculateTotalCashRequired(Map<Item, Integer> selectedItems) {
        int totalCashRequired = 0;
        for (Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = Math.abs(entry.getValue()); // Use absolute value to calculate Cash

            // Directly calculate Cash for quantity
            for (int row = 0; row < Currency.items.size(); row++) {
                Map<String, Object> itemMap = Currency.items.get(row);
                if (itemMap.get("item") == item) {
                    int pointGain = (int) itemMap.get("gain");
                    int itemPrice = (int) itemMap.get("price");
                    totalCashRequired += (float) pointGain / itemPrice * quantity;
                    break;
                }
            }
        }
        return totalCashRequired;
    }

    private static void openMenu(Player player) {
        String[][] buttons = new String[Currency.items.size()][1];

        for (int i = 0; i < Currency.items.size(); i++) {
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
        if (option <   0 || option >= Currency.items.size()) {
            player.sendMessage("Invalid selection. Please try again.");
            return;
        }
        Map<String, Object> selectedItemMap = Currency.items.get(option);
        Item selectedItem = (Item) selectedItemMap.get("item");
    
        String title = "Adjust Quantity";
        Map<Item, Integer> quantities = getSelectedItemsQuantities(player);
        String updatedQuantities = "";
        for (Map.Entry<Item, Integer> entry : quantities.entrySet()) {
            updatedQuantities += entry.getKey().emoji() + ": " + entry.getValue() + "\n";
        }
        String description = "Select quantity adjustment\n\n" + updatedQuantities;
        String[][] buttons = new String[][]{{"-2000", "-1000", "-100", "+400", "+1000", "+2000"}, {"Buy", "Close", "Back"}};
    
        Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
            if (opt <   6) { // Adjustment buttons
                int adjustment = Integer.parseInt(buttons[0][opt]);
                int currentQuantity = quantities.getOrDefault(selectedItem,   0);
                int minQuantity = getMinQuantityForItem(selectedItem);
                if (adjustment <   0 && currentQuantity + adjustment <  minQuantity) {
                    sendMessageToPlayer(player, "menu.buypoint.negativeQuantity");
                    return;
                }
                quantities.put(selectedItem, currentQuantity + adjustment);
                openQuantityAdjustmentMenu(player, option);
            } else if (opt ==   6) { // Buy button
                Map<Item, Integer> selectedItems = getSelectedItemsQuantities(player);
                if (hasEnoughItems(player.team(), option, player)) {
                    int totalCash = calculateTotalCash(selectedItems);
                    PlayerData playerData = Players.getPlayer(player);
                    playerData.addCash(totalCash, player);
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
    private static boolean hasEnoughItems(Team team, int option, Player player) {
        Map<Item, Integer> selectedItems = getSelectedItemsQuantities(player);
        for (int i =  0; i < Currency.items.size(); i++) {
            Map<String, Object> itemMap = Currency.items.get(i);
            Item item = (Item) itemMap.get("item");
            int requiredAmount = selectedItems.getOrDefault(item,  0); // Use the quantity the player wants to purchase
            if (team.items().get(item) < requiredAmount ) {
                return false;
            }
        }
        return true;
    }

    private static int calculateTotalCash(Map<Item, Integer> selectedItems) {
        int totalCash = 0;
        for (Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = Math.abs(entry.getValue()); // Use absolute value to calculate Cash

            // Directly calculate Cash for quantity
            for (int row = 0; row < Currency.items.size(); row++) {
                Map<String, Object> itemMap = Currency.items.get(row);
                if (itemMap.get("item") == item) {
                    int pointGain = (int) itemMap.get("gain");
                    int itemPrice = (int) itemMap.get("price");
                    totalCash += (float) pointGain / itemPrice * quantity;
                    break;
                }
            }
        }
        return totalCash;
    }

    public static void removeItemsFromTeam(Team team, Map<Item, Integer> selectedItems) {
        for (Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            team.items().remove(item, quantity);
        }
    }

    public static void addItemsToTeam(Team team, Map<Item, Integer> selectedItems) {
        for (Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            team.items().add(item, quantity);
        }
    }

    public static void createAndDisplayMenu(Player player, String title, String description, String[][] buttons) {
        Call.menu(player.con(), Menus.registerMenu((p, opt) -> {
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