package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Menuforunits {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 0 -> Units.execute(player);
            case 1 -> Info.execute(player);
            case 2 -> BuyPoint.execute(player);
            case  3 -> Statuseffects.execute(player);

        }
    });

    private static final String[][] buttons = {
            {"[lime]Units",  "[red]Info"},
            {"[cyan]BuyPoints","[blue]Powerups", "[lightgray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("menu.title", player.locale), "", buttons);
    }

    public static void init() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }
}
