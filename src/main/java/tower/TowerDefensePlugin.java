package tower;

import mindustry.mod.Plugin;
import tower.game.EventLoader;
import useful.Bundle;

public class TowerDefensePlugin extends Plugin {

    // Loads game assets and data
    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);
        EventLoader.init();
        PluginLogic.init();
    }
}
