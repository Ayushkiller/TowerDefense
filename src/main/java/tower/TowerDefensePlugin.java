package tower;

import arc.util.CommandHandler;

import mindustry.gen.Player;
import mindustry.mod.Plugin;
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
        handler.register("m", "Opens a menu", (String[] args, Player player) -> Menu.execute(player));

    }
}
