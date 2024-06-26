package tower.menus;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.commands.BuyPoint;
import tower.commands.Info;
import tower.commands.Statuseffects;
import tower.commands.SuperPowers;
import tower.commands.Units;

public class Menu {
    private static final int MENU_ID;

    static {
        MENU_ID = Menus.registerMenu((player, option) -> {
            switch (option) {
                case 0 -> Units.execute(player);
                case 1 -> Info.execute(player);
                case 2 -> SuperPowers.execute(player);
                case 3 -> BuyPoint.execute(player);
                case 4 -> Statuseffects.execute(player);
            }
        });
    }

    private static final String[][] BUTTONS = {
            { "[lime]Units", "[red]Info", "[lime]Power" },
            { "[cyan]Cash", "[blue]Powerups", "[lightgray]Close" }
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, MENU_ID, Bundle.get("menu.title", player.locale), "", BUTTONS);
    }
}
