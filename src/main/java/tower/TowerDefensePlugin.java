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
import tower.menus.Menu;
import tower.pathing.TowerPathfinder;
import useful.Bundle;

public class TowerDefensePlugin extends Plugin {

    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);
        TowerPathfinder.init();
        PluginLogic.init();
        Loader.load();
        CustomStatusEffects.load();
    }


  public void registerClientCommands(CommandHandler handler) {
    handler.register("menu", "Opens a menu", (String[] args, Player player) -> Menu.execute(player));
    handler.register("sell", "[item] [amount]", "Sells items based on the price", (String[] args, Player player) -> {
        if (args.length > 1) {
            String itemName = args[0];
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid amount provided.");
                return;
            }
            sellItems(player, itemName, amount);
        } else {
            player.sendMessage("Please specify the item and amount to sell.");
        }
    });
    handler.register("buy", "[item] [amount]", "Buys items based on the price", (String[] args, Player player) -> {
        if (args.length > 1) {
            String itemName = args[0];
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid amount provided.");
                return;
            }
            buyItems(player, itemName, amount);
        } else {
            player.sendMessage("Please specify the item and amount to buy.");
        }
    });
}
  public void registerServerCommands(CommandHandler handler) {
      handler.register("death", "<points>", "Adds points to every player", (String[] args, Player player) -> {
          if (args.length > 0) {
              try {
                  int pointsToAdd = Integer.parseInt(args[0]);
                  Groups.player.each(p -> {
                      PlayerData playerData = Players.getPlayer(p);
                      if (playerData != null) {
                          playerData.addPoints(pointsToAdd, p);
                      }
                  });
              } catch (NumberFormatException e) {
                  // Handle the case where the argument is not a valid integer
                  System.out.println("Invalid number of points provided.");
              }
          } else {
              System.out.println("No points specified.");
          }
      });
  }
  private void sellItems(Player player, String itemName, int amount) {
    Item itemToSell = null;
    for (Map<String, Object> itemMap : Currency.items) {
        Item item = (Item) itemMap.get("item");
        if (item.toString().equalsIgnoreCase(itemName)) {
            itemToSell = item;
            break;
        }
    }

    if (itemToSell == null) {
        player.sendMessage("Item not found.");
        return;
    }

    int price = 0;
    for (Map<String, Object> itemMap : Currency.items) {
        Item item = (Item) itemMap.get("item");
        if (item == itemToSell) {
            price = (int) itemMap.get("price");
            break;
        }
    }

    int totalPrice = price * amount;
    PlayerData playerData = Players.getPlayer(player);
    if (playerData != null) {
        playerData.addPoints(totalPrice, player);
        Team playerTeam = player.team(); 
        playerData.addPoints(amount, player);
        Map<Item, Integer> itemsToremove = new HashMap<>();
        itemsToremove.put(itemToSell, amount);
        BuyPoint.removeItemsFromTeam(playerTeam, itemsToremove);
    }
    player.sendMessage("Sold " + amount + " " + itemToSell.toString() + " for " + totalPrice + " points.");
}
private void buyItems(Player player, String itemName, int amount) {
    Item itemToBuy = null;
    for (Map<String, Object> itemMap : Currency.items) {
        Item item = (Item) itemMap.get("item");
        if (item.toString().equalsIgnoreCase(itemName)) {
            itemToBuy = item;
            break;
        }
    }

    if (itemToBuy == null) {
        player.sendMessage("Item not found.");
        return;
    }

    int price = 0;
    for (Map<String, Object> itemMap : Currency.items) {
        Item item = (Item) itemMap.get("item");
        if (item == itemToBuy) {
            price = (int) itemMap.get("price");
            break;
        }
    }

    int totalPrice = price * amount;
    PlayerData playerData = Players.getPlayer(player);
    if (playerData != null) {
        if (playerData.getPoints() >= totalPrice) {
            Team playerTeam = player.team(); 
            playerData.subtractPoints(totalPrice, player);
            Map<Item, Integer> itemsToAdd = new HashMap<>();
            itemsToAdd.put(itemToBuy, amount);
            BuyPoint.addItemsToTeam(playerTeam, itemsToAdd);
            player.sendMessage("Bought " + amount + " " + itemToBuy.toString() + " for " + totalPrice + " points.");
        } else {
            player.sendMessage("Not enough points to buy " + amount + " " + itemToBuy.toString() + ".");
        }
    }
}
}
