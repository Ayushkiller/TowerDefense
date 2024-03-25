package tower;

import mindustry.mod.Plugin;
import tower.game.EventLoader;

public class TowerDefensePlugin extends Plugin {

    // Loads game assets and data
    @Override
    public void init() {
        EventLoader.init();
        PluginLogic.init();
    }
}
