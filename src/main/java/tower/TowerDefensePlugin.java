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
    System.out.println("Attempting to sell " + amount + " " + itemName + " for player " + player.name());
    Item itemToSell = findItemByName(itemName);
    if (itemToSell == null) {
        player.sendMessage("Item not found.");
        System.out.println("Item not found: " + itemName);
        return;
    }

    int price = getItemPrice(itemToSell);
    int totalPrice = price * amount;
    PlayerData playerData = Players.getPlayer(player);
    if (playerData == null) {
        player.sendMessage("Player data not found.");
        System.out.println("Player data not found for player " + player.name());
        return;
    }

    // Calculate points gained based on the amount of the resource removed
    int pointsGained = calculatePointsGained(itemToSell, price, amount);
    // Add the points to the player's account
    playerData.addPoints(pointsGained, player);
    removeItemsFromTeam(player.team(), itemToSell, amount);
    player.sendMessage("Sold " + amount + " " + itemToSell.toString() + " for " + totalPrice + " points. You gained " + pointsGained + " points.");
    System.out.println("Successfully sold " + amount + " " + itemToSell.toString() + " for " + totalPrice + " points. Gained " + pointsGained + " points.");
}

private int calculatePointsGained(Item item, int price, int amount) {
    for (Map<String, Object> itemMap : Currency.items) {
        Item currentItem = (Item) itemMap.get("item");
        if (currentItem == item) {
            int gain = (int) itemMap.get("gain");
            return (int) ((float) gain / price * amount);
        }
    }
    return 0;
}

private void removeItemsFromTeam(Team playerTeam, Item item, int amount) {
    Map<Item, Integer> itemsToRemove = new HashMap<>();
    itemsToRemove.put(item, amount);
    BuyPoint.removeItemsFromTeam(playerTeam, itemsToRemove);
}
private void buyItems(Player player, String itemName, int amount) {
    System.out.println("Attempting to buy " + amount + " " + itemName + " for player " + player.name());
    Item itemToBuy = findItemByName(itemName);
    if (itemToBuy == null) {
        player.sendMessage("Item not found.");
        System.out.println("Item not found: " + itemName);
        return;
    }

    int price = getItemPrice(itemToBuy);
    int totalPrice = price * amount;
    PlayerData playerData = Players.getPlayer(player);
    if (playerData == null || playerData.getPoints() < totalPrice) {
        player.sendMessage("Not enough points to buy " + amount + " " + itemToBuy.toString() + ".");
        System.out.println("Not enough points to buy " + amount + " " + itemToBuy.toString());
        return;
    }

    int pointsToRemove = calculatePointsToRemove(itemToBuy, price, amount);
    playerData.subtractPoints(pointsToRemove, player);
    addItemsToTeam(player, itemToBuy, amount);
    player.sendMessage("Bought " + amount + " " + itemToBuy.toString() + " for " + totalPrice + " points.");
    System.out.println("Successfully bought " + amount + " " + itemToBuy.toString() + " for " + totalPrice + " points.");
}

private Item findItemByName(String itemName) {
    for (Map<String, Object> itemMap : Currency.items) {
        Item item = (Item) itemMap.get("item");
        if (item.toString().equalsIgnoreCase(itemName)) {
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

private int calculatePointsToRemove(Item item, int price, int amount) {
    for (Map<String, Object> itemMap : Currency.items) {
        Item currentItem = (Item) itemMap.get("item");
        if (currentItem == item) {
            int gain = (int) itemMap.get("gain");
            return (int) ((float) gain / price * amount);
        }
    }
    return 0;
}

private void addItemsToTeam(Player player, Item item, int amount) {
    Team playerTeam = player.team();
    Map<Item, Integer> itemsToAdd = new HashMap<>();
    itemsToAdd.put(item, amount);
    BuyPoint.addItemsToTeam(playerTeam, itemsToAdd);
}
}
