package tower;

import arc.util.CommandHandler;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Plugin;
import tower.Domain.PlayerData;
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
    }


  public void registerClientCommands(CommandHandler handler) {
    handler.register("menu", "Opens a menu", (String[] args, Player player) -> Menu.execute(player));
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
  
}
