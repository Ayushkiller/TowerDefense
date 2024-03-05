package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Info {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 1 -> Menuforunits.execute(player); // Back
            case 2 -> openUnitsInfo(player);
            case 3 -> openPointsInfo(player);
            case 4 -> openSuperpowerInfo(player);
            case 5 -> openPowerupInfo(player);
            case 6 -> openBuyPointInfo(player);
        }
    });
    private static final String[][] buttons = {
        {"[gray]Close"},
        {"[gray]Units"},
        {"[gray]Points"},
        {"[gray]Superpower"},
        {"[gray]Powerup"},
        {"[gray]Buy Points"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("settings.message", player.locale), buttons);
    }

    // Add methods for each new GUI here
    public static void openUnitsInfo(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("info.units", player.locale), buttons);
    }

    public static void openPointsInfo(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("info.points", player.locale), buttons);
    }

    public static void openSuperpowerInfo(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("info.superpower", player.locale), buttons);
    }

    public static void openPowerupInfo(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("info.powerup", player.locale), buttons);
    }

    public static void openBuyPointInfo(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("info.buypoint", player.locale), buttons);
    }
}