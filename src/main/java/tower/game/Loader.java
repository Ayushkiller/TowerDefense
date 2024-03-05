package tower.game;

import tower.Domain.CustomStatusEffects;
import tower.commands.Units;

public class Loader {
    public static void load() {
        CustomStatusEffects.load();
        EventLoader.init();
        Units.initUnitsTable();

    }
}
