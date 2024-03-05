package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Menuforunits {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 0 -> Units.execute(player);
            case 1 -> Info.execute(player, 1); // Adjusted to include the selectedOption argument
            case 2 -> SuperPowers.execute(player);
            case 3 -> BuyPoint.execute(player);
            case 4 -> Statuseffects.execute(player);
        }
    });

    private static final String[][] buttons = {
        {"[lime]Units", "[red]Info","[purple]Super[lime]Pow"},
        {"[cyan]BuyPoints","[blue]Powerups", "[lightgray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("menu.title", player.locale), "", buttons);
    }
}