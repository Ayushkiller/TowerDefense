package tower.menus;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.commands.Settings;
import tower.commands.Units;

public class Menu {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 0 -> Units.execute(player);
            case 1 -> Settings.execute(player);
            case 2 -> {}
        }
    });

    private static final String[][] buttons = {
            {"[lime]Units", "[red]Settings"},
            {"[cyan]Donate", "[lightgray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("menu.title", player.locale), "", buttons);
    }
}
