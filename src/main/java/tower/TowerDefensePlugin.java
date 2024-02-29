package tower;

import arc.util.CommandHandler;
import mindustry.core.GameState;
import mindustry.core.GameState.State;
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
     private GameState state; 
     private void err(String message) {
        System.err.println(message);
    }
    private void info(String message) {
        System.out.println(message);
    }
    public void registerClientCommands(CommandHandler handler) {
        handler.register("menu", "Opens a menu", (String[] args, Player player) -> Menu.execute(player));
    handler.register("pause", "<on/off>", "Pause or unpause the game.", arg -> {
    if(state.isMenu()){
        err("Cannot pause without a game running.");
        return;
    }
    boolean pause = arg[0].equals("on");
    state.set(state.isPaused() ? State.playing : State.paused);
    info(pause ? "Game paused." : "Game unpaused.");
     });

    }
}
