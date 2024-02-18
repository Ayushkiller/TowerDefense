package tower.game;

import tower.commands.Units;

public class Loader {
    public static void load() {

        EventLoader.init();
        Units.initUnitsTable();

    }
}
