package tower;

import arc.util.CommandHandler;

import mindustry.gen.Player;
import mindustry.mod.Plugin;
import tower.commands.Menuforunits;
import tower.commands.Point;
import tower.game.EventLoader;
import tower.menus.Menu;
import tower.pathing.TowerPathfinder;
import useful.Bundle;


public class TowerDefensePlugin extends Plugin {


    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);
        TowerPathfinder.init();
        Point.init();
        Menu.init();
        PluginLogic.init();
        EventLoader.init();
        Menuforunits.init();

    }
    public void registerClientCommands(CommandHandler handler) {
        handler.register("m", "Opens a menu", (String[] args, Player player) -> Menu.execute(player));
        handler.register("p", "Point somewhere.", (String[] args, Player player) -> Point.point(player));

    }
}
