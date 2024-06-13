package tower;

import java.util.Map;

import arc.util.CommandHandler;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Plugin;
import mindustry.type.Item;
import tower.Domain.Currency;
import tower.Domain.PlayerData;
import tower.menus.Menu;
import tower.pathing.TowerPathfinder;
import useful.Bundle;

public class TowerDefensePlugin extends Plugin {

    // Loads game assets and data
    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);
        TowerPathfinder.init();
        PluginLogic.init(); 
    }

    // Registers client-side chat commands
    public void registerClientCommands(CommandHandler handler) {
        handler.register("menu", "Opens the Special store", (String[] args, Player player) -> Menu.execute(player));

        handler.register("cash", "[item] [amount]", "Buy/sell cash. To sell cash, use a negative amount.",
                (String[] args, Player player) -> {
                    if (args.length > 1) {
                        String itemName = args[0];
                        int amount;
                        try {
                            amount = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("[#f]⚠[] [#f]Invalid amount provided.");
                            return;
                        }

                        if (amount > 0) {
                            // Player wants to sell items
                            sellItems(player, itemName, amount);
                        } else {
                            player.sendMessage("[#f]⚠[] [#f]Negative amounts are not allowed.");
                        }
                    } else {
                        player.sendMessage("[#f]⚠[] [#f]Please specify the item and amount.");
                    }
                });
    }

    // Registers server-side chat commands
    public void registerServerCommands(CommandHandler handler) {
        handler.register("death", "<Cash>", "Adds Cash to every player", (String[] args, Player player) -> {
            if (args.length > 0) {
                try {
                    int cashToAdd = Integer.parseInt(args[0]);
                    Groups.player.each(p -> {
                        PlayerData playerData = Players.getPlayer(p);
                        if (playerData != null) {
                            playerData.addCash(cashToAdd);
                        }
                    });
                } catch (NumberFormatException e) {
                    // Handle potential parsing errors
                }
            }
        });
    }

    // Handles selling items to the player
    private void sellItems(Player player, String itemName, int amount) {
        Item itemToSell = findItemByName(itemName);
        if (itemToSell == null) {
            player.sendMessage("[#f]⚠[] [#f]Item not found.");
            return;
        }

        // Check if the player has enough of the item to sell
        int availableAmount = player.team().core().items.get(itemToSell);
        int remainingAmountAfterSale = availableAmount - amount;

        // Ensure at least 2000 of the item always remains in the core
        if (remainingAmountAfterSale < 2000) {
            player.sendMessage("[#f]⚠[] [#f]You must keep at least 2000 " + itemToSell + " in your core.");
            return;
        }

        int price = getItemPrice(itemToSell);
        PlayerData playerData = Players.getPlayer(player);
        if (playerData == null) {
            player.sendMessage("[#f]⚠[] [#f]Player data not found.");
            return;
        }

        int cashGained = calculateCashGained(itemToSell, price, amount);
        playerData.addCash(cashGained);

        // Remove items from the player's core
        player.team().core().items.remove(itemToSell, amount);

        player.sendMessage(
                "[green]Sold " + amount + " " + itemToSell + ".[red] Transacted " + cashGained + " Cash.");
    }

    // Calculates the cash gained from selling an item
    private int calculateCashGained(Item item, int price, int amount) {
        for (Map<String, Object> itemMap : Currency.items) {
            Item currentItem = (Item) itemMap.get("item");
            if (currentItem == item) {
                int gain = (int) itemMap.get("gain");
                return (int) ((float) gain / price * amount);
            }
        }
        return 0;
    }

    // Finds an item by its name (case-insensitive)
    private Item findItemByName(String itemName) {
        for (Map<String, Object> itemMap : Currency.items) {
            Item item = (Item) itemMap.get("item");
            if (item.toString().toLowerCase().contains(itemName.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    // Gets the price of an item from the Currency data
    private int getItemPrice(Item item) {
        for (Map<String, Object> itemMap : Currency.items) {
            Item currentItem = (Item) itemMap.get("item");
            if (currentItem == item) {
                return (int) itemMap.get("price");
            }
        }
        return 0;
    }


}
