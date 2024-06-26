package tower.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.ui.Menus;
import tower.Players;
import tower.Domain.Currency;
import tower.Domain.PlayerData;

public class BuyPoint {
    private static final Map<Player, Map<Item, Integer>> selectedItemsQuantities = new HashMap<>();
    private static final Map<Integer, BiConsumer<Player, Integer>> dynamicListeners = new HashMap<>();
    private static int buySellMenuId;
    private static int buyMenuId;
    private static int sellMenuId;
    private static int quantityAdjustmentMenuId;

    static {
        registerMenus();
    }

    public static void execute(Player player) {
        openBuySellMenu(player);
    }

    public static void registerMenus() {
        buySellMenuId = Menus.registerMenu((player, option) -> dynamicListeners.get(buySellMenuId).accept(player, option));
        buyMenuId = Menus.registerMenu((player, option) -> dynamicListeners.get(buyMenuId).accept(player, option));
        sellMenuId = Menus.registerMenu((player, option) -> dynamicListeners.get(sellMenuId).accept(player, option));
        quantityAdjustmentMenuId = Menus.registerMenu((player, option) -> dynamicListeners.get(quantityAdjustmentMenuId).accept(player, option));
    }

    private static void openBuySellMenu(Player player) {
        String[][] buttons = { { "[red]Buy Cash", "[cyan]Buy Items" } };
        dynamicListeners.put(buySellMenuId, (p, option) -> {
            if (option == 0) {
                openBuyMenu(p);
            } else if (option == 1) {
                openSellMenu(p);
            }
        });
        Call.menu(player.con, buySellMenuId, "Choose an Option", "", buttons);
    }

    private static void openBuyMenu(Player player) {
        String[][] buttons = createButtons(Currency.items, true);
        dynamicListeners.put(buyMenuId, (p, option) -> openQuantityAdjustmentMenu(p, option, true));
        Call.menu(player.con, buyMenuId, "Buy Items", "", buttons);
    }

    private static void openSellMenu(Player player) {
        String[][] buttons = createButtons(Currency.items, false);
        dynamicListeners.put(sellMenuId, (p, option) -> openQuantityAdjustmentMenu(p, option, false));
        Call.menu(player.con, sellMenuId, "Sell Items", "", buttons);
    }

    private static void openQuantityAdjustmentMenu(Player player, int option, boolean isBuy) {
        if (option < 0 || option >= Currency.items.size()) {
            player.sendMessage("Invalid selection. Please try again.");
            return;
        }
        Map<String, Object> selectedItemMap = Currency.items.get(option);
        Item selectedItem = (Item) selectedItemMap.get("item");

        String title = "Adjust Quantity";
        Map<Item, Integer> quantities = getSelectedItemsQuantities(player);
        String description = generateDescription(quantities, player, isBuy);

        String[][] buttons = { 
            { "-2000", "-1000", "-100", "+400", "+1000", "+2000" },
            { isBuy ? "Buy" : "Sell", "Close", "Back" }
        };

        dynamicListeners.put(quantityAdjustmentMenuId, (p, opt) -> handleQuantityAdjustmentMenu(p, opt, selectedItem, option, isBuy));
        Call.menu(player.con, quantityAdjustmentMenuId, title, description, buttons);
    }

    private static void handleQuantityAdjustmentMenu(Player player, int opt, Item selectedItem, int option, boolean isBuy) {
        Map<Item, Integer> quantities = getSelectedItemsQuantities(player);
        if (opt < 6) { 
            adjustItemQuantity(player, selectedItem, quantities, opt);
            openQuantityAdjustmentMenu(player, option, isBuy);
        } else if (opt == 6) { 
            if (isBuy) {
                handleBuyAction(player, quantities);
            } else {
                handleSellAction(player, quantities);
            }
        } else if (opt == 7) { 
            player.sendMessage("[grey]Transaction closed.");
            selectedItemsQuantities.remove(player);
        } else if (opt == 8) { 
            if (isBuy) {
                openBuyMenu(player);
            } else {
                openSellMenu(player);
            }
        }
    }

    private static void adjustItemQuantity(Player player, Item item, Map<Item, Integer> quantities, int opt) {
        int adjustment = Integer.parseInt(new String[] { "-2000", "-1000", "-100", "+400", "+1000", "+2000" }[opt]);
        int currentQuantity = quantities.getOrDefault(item, 0);
        int newQuantity = currentQuantity + adjustment;
        if (newQuantity < 0) {
            player.sendMessage("Quantity cannot be negative.");
        } else {
            quantities.put(item, newQuantity);
        }
    }

    private static void handleBuyAction(Player player, Map<Item, Integer> selectedItems) {
        int totalCash = calculateTotalCash(selectedItems);
        if (totalCash == 0) {
            player.sendMessage("[red]No items selected.");
            return;
        }
        if (hasEnoughItems(player.team(), selectedItems)) {
            PlayerData playerData = Players.getPlayer(player);
            playerData.addCash(totalCash, player);
            removeItemsFromTeam(player.team(), selectedItems);
            selectedItemsQuantities.remove(player);
            player.sendMessage("[green]Items purchased successfully.");
        } else {
            player.sendMessage("[red]Not enough items in the team's inventory.");
            selectedItemsQuantities.put(player, new HashMap<>());
        }
    }

    private static void handleSellAction(Player player, Map<Item, Integer> selectedItems) {
        int totalQuantity = selectedItems.values().stream().mapToInt(Integer::intValue).sum();
        if (totalQuantity < 0) {
            player.sendMessage("[red]Quantity cannot be negative.");
            return;
        }
        PlayerData playerData = Players.getPlayer(player);
        int totalCashRequired = calculateTotalCashRequired(selectedItems);
        if (playerData.getCash() < totalCashRequired) {
            player.sendMessage("[red] Insufficient funds.");
            return;
        }
        if (totalCashRequired == 0) {
            player.sendMessage("[red] No items selected.");
            return;
        }
        addItemsToTeam(player.team(), selectedItems);
        playerData.subtractCash(totalCashRequired, player);
        selectedItemsQuantities.remove(player);
        player.sendMessage("[green] Items sold successfully.");
    }

    private static String[][] createButtons(List<Map<String, Object>> items, boolean isBuy) {
        String[][] buttons = new String[items.size()][1];
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> itemMap = items.get(i);
            Item item = (Item) itemMap.get("item");
            int gain = (int) itemMap.get("gain");
            int price = (int) itemMap.get("price");
            buttons[i][0] = String.format("%s (%s%d) [gray]Price: %d", item.emoji(), isBuy ? "+" : "-", gain, price);
        }
        return buttons;
    }

    private static String generateDescription(Map<Item, Integer> quantities, Player player, boolean isBuy) {
        StringBuilder description = new StringBuilder("Select quantity adjustment\n\n");
        for (Map.Entry<Item, Integer> entry : quantities.entrySet()) {
            int teamQuantity = player.team().items().get(entry.getKey()); 
            description.append(String.format("%s: %d [gray]Team Quantity: %d\n", entry.getKey().emoji(), entry.getValue(), teamQuantity));
        }
        description.append("\n\n[orange]Total Cash Required: ").append(calculateTotalCashRequired(quantities));
        return description.toString();
    }

    private static int calculateTotalCashRequired(Map<Item, Integer> selectedItems) {
        return selectedItems.entrySet().stream()
                .mapToInt(entry -> {
                    Item item = entry.getKey();
                    int quantity = Math.abs(entry.getValue());
                    return Currency.items.stream()
                            .filter(itemMap -> itemMap.get("item") == item)
                            .mapToInt(itemMap -> (int) itemMap.get("gain") / (int) itemMap.get("price") * quantity)
                            .findFirst()
                            .orElse(0);
                }).sum();
    }

    private static int calculateTotalCash(Map<Item, Integer> selectedItems) {
        return calculateTotalCashRequired(selectedItems);
    }

    private static boolean hasEnoughItems(Team team, Map<Item, Integer> selectedItems) {
        return selectedItems.entrySet().stream()
                .allMatch(entry -> team.items().get(entry.getKey()) >= entry.getValue());
    }

    private static void removeItemsFromTeam(Team team, Map<Item, Integer> selectedItems) {
        selectedItems.forEach((item, quantity) -> team.items().remove(item, quantity));
    }

    private static void addItemsToTeam(Team team, Map<Item, Integer> selectedItems) {
        selectedItems.forEach((item, quantity) -> team.items().add(item, quantity));
    }

    private static Map<Item, Integer> getSelectedItemsQuantities(Player player) {
        return selectedItemsQuantities.computeIfAbsent(player, k -> new HashMap<>());
    }

    public static void clearMenuIds() {
        selectedItemsQuantities.clear();
        dynamicListeners.clear();
    }
}
