package tower;

import java.util.HashMap;
import java.util.Map;

import mindustry.game.Team;
import arc.util.CommandHandler;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Plugin;
import mindustry.type.Item;
import tower.Domain.Currency;
import tower.Domain.CustomStatusEffects;
import tower.Domain.PlayerData;
import tower.commands.BuyPoint;
import tower.game.Loader;
import tower.game.Scenarios;
import tower.menus.Menu;
import tower.pathing.TowerPathfinder;
import useful.Bundle;
/**
 * The main plugin class for the Tower Defense game.
 * This class handles the initialization of the game, registration of commands, and other core functionalities.
 */
public class TowerDefensePlugin extends Plugin {

    /**
     * Initializes the plugin by loading resources, initializing game components, and setting up custom status effects.
     */
    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);
        Loader.load();
        TowerPathfinder.init();
        PluginLogic.init();
        CustomStatusEffects.load();
    }

   /**
     * Registers client-side commands for the game.
     * @param handler The command handler to register commands with.
     */
  public void registerClientCommands(CommandHandler handler) {
    handler.register("menu", "Opens the Special store", (String[] args, Player player) -> Menu.execute(player));
    handler.register("cash", "[item] [amount]", "Buy/sell cash.To sell cash add - to amount ", (String[] args, Player player) -> {
        if (args.length > 1) {
            String itemName = args[0];
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("[#f]⚠[] [#f]Invalid amount provided.");
                return;
            }
    
            // Check if the player has enough of the item to sell
            Item itemToSell = findItemByName(itemName);
            if (itemToSell == null) {
                player.sendMessage("[#f]⚠[] [#f]Item not found.");
                return;
            }
            if (player.team().items().get(itemToSell) < amount + 2000) {
                player.sendMessage("[#f]⚠[] [#f]You do not have enough of this item to sell.Ensure 2000 of resources left in core.");
                return;
            }
    
            // Check if the player has enough cash to sell the items
            int price = getItemPrice(itemToSell);
            int totalPrice = price * amount;
            PlayerData playerData = Players.getPlayer(player);
            if (playerData == null) {
                player.sendMessage("[#f]⚠[] [#f]Player data not found.");
                return;
            }
            if (playerData.getCash() < totalPrice) {
                player.sendMessage("[#f]⚠[] [#f]You do not have enough cash to Buy this amount.");
                return;
            }
    
            sellItems(player, itemName, amount);
        } else {
            player.sendMessage("[#f]⚠[] [#f]Please specify the item and amount to sell.");
        }
    });
}
   /**
     * Registers server-side commands for the game.
     * @param handler The command handler to register commands with.
     */
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
      /**
     * Sells items to the player.
     * @param player The player selling the items.
     * @param itemName The name of the item to sell.
     * @param amount The amount of the item to sell.
     */
  private void sellItems(Player player, String itemName, int amount) {
  
    Item itemToSell = findItemByName(itemName);
    if (itemToSell == null) {
        player.sendMessage("Item not found.");
      
        return;
    }

    int price = getItemPrice(itemToSell);
    PlayerData playerData = Players.getPlayer(player);
    if (playerData == null) {
       
        return;
    }

  
    int CashGained = calculateCashGained(itemToSell, price, amount);
    
    playerData.addCash(CashGained, player);
    removeItemsFromTeam(player.team(), itemToSell, amount);
    player.sendMessage("Sold " + amount + " " + itemToSell.toString() + ". You gained " + CashGained + " Cash.");
   
}
   /**
     * Calculates the cash gained from selling items.
     * @param item The item being sold.
     * @param price The price of the item.
     * @param amount The amount of the item being sold.
     * @return The cash gained from selling the items.
     */
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
    /**
     * Removes items from the player's team.
     * @param playerTeam The team of the player.
     * @param item The item to remove.
     * @param amount The amount of the item to remove.
     */
private void removeItemsFromTeam(Team playerTeam, Item item, int amount) {
    Map<Item, Integer> itemsToRemove = new HashMap<>();
    itemsToRemove.put(item, amount);
    BuyPoint.removeItemsFromTeam(playerTeam, itemsToRemove);
}

    /**
     * Finds an item by its name.
     * @param itemName The name of the item to find.
     * @return The item found, or null if not found.
     */
    private Item findItemByName(String itemName) {
        for (Map<String, Object> itemMap : Currency.items) {
            Item item = (Item) itemMap.get("item");
            if (item.toString().toLowerCase().contains(itemName.toLowerCase())) {
                return item;
            }
        }
        return null;
    }
    /**
     * Gets the price of an item.
     * @param item The item to get the price for.
     * @return The price of the item.
     */
private int getItemPrice(Item item) {
    for (Map<String, Object> itemMap : Currency.items) {
        Item currentItem = (Item) itemMap.get("item");
        if (currentItem == item) {
            return (int) itemMap.get("price");
        }
    }
    return 0;
}
  /**
     * Requests deployment for all players.
     */
private void requestDeploymentForAll() {
    Scenarios.requestDeploymentForAllPlayers();
}
}
