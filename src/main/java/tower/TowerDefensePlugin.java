package tower;

import java.util.Map;

import arc.util.CommandHandler;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Plugin;
import mindustry.type.Item;
import tower.Domain.Currency;
import tower.Domain.CustomStatusEffects;
import tower.Domain.PlayerData;
import tower.game.Loader;
import tower.game.Scenarios;
import tower.menus.Menu;
import tower.pathing.TowerPathfinder;
import useful.Bundle;

public class TowerDefensePlugin extends Plugin {


    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);
        Loader.load();
        TowerPathfinder.init();
        PluginLogic.init();
        CustomStatusEffects.load();
    }
    public void registerClientCommands(CommandHandler handler) {
        handler.register("menu", "Opens the Special store", (String[] args, Player player) -> Menu.execute(player));
        handler.register("cash", "[item] [amount]", "Buy/sell cash. To sell cash, use negative amount.",
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
                        } else if (amount < 0) {
                            // Player wants to buy items
                            buyItems(player, itemName, -amount); // Convert negative amount to positive
                        } else {
                            player.sendMessage("[#f]⚠[] [#f]Amount should not be zero.");
                        }
                    } else {
                        player.sendMessage("[#f]⚠[] [#f]Please specify the item and amount.");
                    }
                });
    }


    public void registerServerCommands(CommandHandler handler) {
        handler.register("death", "<Cash>", "Adds Cash to every player", (String[] args, Player player) -> {
            if (args.length > 0) {
                try {
                    int CashToAdd = Integer.parseInt(args[0]);
                    Groups.player.each(p -> {
                        PlayerData playerData = Players.getPlayer(p);
                        if (playerData != null) {
                            playerData.addCash(CashToAdd, p);
                        }
                    });
                } catch (NumberFormatException e) {

                }
            } else {

            }
        });
        handler.register("deployall", "Requests deployment for all players", (String[] args, Player player) -> {
            requestDeploymentForAll();

        });
    }


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
            player.sendMessage("[#f]⚠[] [#f]You must keep at least 2000 " + itemToSell.toString() + " in your core.");
            return;
        }
    
        int price = getItemPrice(itemToSell);
        PlayerData playerData = Players.getPlayer(player);
        if (playerData == null) {
            player.sendMessage("[#f]⚠[] [#f]Player data not found.");
            return;
        }
    
        int cashGained = calculateCashGained(itemToSell, price, amount);
        playerData.addCash(cashGained, player);
    
        // Remove items from the player's core
        player.team().core().items.remove(itemToSell, amount);
    
        player.sendMessage(
                "[green]Sold " + amount + " " + itemToSell.toString() + ".[red] Transacted " + cashGained + " Cash.");
    }
    
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

    private void buyItems(Player player, String itemName, int amount) {
        Item itemToBuy = findItemByName(itemName);
        if (itemToBuy == null) {
            player.sendMessage("[#f]⚠[] [#f]Item not found.");
            return;
        }

        int price = getItemPrice(itemToBuy);
        PlayerData playerData = Players.getPlayer(player);
        if (playerData == null) {
            player.sendMessage("[#f]⚠[] [#f]Player data not found.");
            return;
        }

        int requiredCash = price * amount;
        if (playerData.getCash() < requiredCash) {
            player.sendMessage("[#f]⚠[] [#f]You do not have enough cash to buy these items.You need "+ requiredCash);
            return;
        }

        // Add items to the player's core
        player.team().core().items.add(itemToBuy, amount);

        // Deduct cash from the player
        playerData.subtractCash(requiredCash, player);

        player.sendMessage("Bought " + amount + " " + itemToBuy.toString() + " for " + requiredCash + " Cash.");
    }

    private Item findItemByName(String itemName) {
        for (Map<String, Object> itemMap : Currency.items) {
            Item item = (Item) itemMap.get("item");
            if (item.toString().toLowerCase().contains(itemName.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    private int getItemPrice(Item item) {
        for (Map<String, Object> itemMap : Currency.items) {
            Item currentItem = (Item) itemMap.get("item");
            if (currentItem == item) {
                return (int) itemMap.get("price");
            }
        }
        return 0;
    }

    private void requestDeploymentForAll() {
        Scenarios.requestDeploymentForAllPlayers();
    }
}
