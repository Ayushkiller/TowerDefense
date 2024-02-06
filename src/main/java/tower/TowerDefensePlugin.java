package tower;

import arc.util.CommandHandler;
import mindustry.mod.Plugin;
import tower.commands.ClientCommands;
import tower.pathing.TowerPathfinder;
import useful.Bundle;

@SuppressWarnings("unused")
public class TowerDefensePlugin extends Plugin {


    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);
        TowerPathfinder.init();
        PluginLogic.init();
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        ClientCommands.register(handler);
    }
}
