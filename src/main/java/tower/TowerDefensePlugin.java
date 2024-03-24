package tower;

import mindustry.mod.Plugin;
import useful.Bundle;

public class TowerDefensePlugin extends Plugin {

    // Loads game assets and data
    @Override
    public void init() {
        Bundle.load(TowerDefensePlugin.class);

        PluginLogic.init(); 
    }
}
 
