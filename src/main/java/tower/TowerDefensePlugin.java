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

  
    int pointsGained = calculatePointsGained(itemToSell, price, amount);
    
    playerData.addPoints(pointsGained, player);
    removeItemsFromTeam(player.team(), itemToSell, amount);
    player.sendMessage("Sold " + amount + " " + itemToSell.toString() + ". You gained " + pointsGained + " points.");
   
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
}
