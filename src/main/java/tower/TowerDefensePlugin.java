package tower;

import arc.util.CommandHandler;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Plugin;
import tower.Domain.PlayerData;
import tower.game.EventLoader;
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
        EventLoader.init();
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
                            playerData.addCash(cashToAdd, player);
                        }
                    });
                } catch (NumberFormatException e) {
                    // Handle potential parsing errors
                }
            }
        });
    }

 public void registerClientCommands(CommandHandler handler) {
        handler.register("menu", "Opens the Special store", (String[] args, Player player) -> Menu.execute(player));
 }

}
