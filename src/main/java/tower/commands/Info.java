package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Info {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 1 -> Menuforunits.execute(player); // Back
            case 2 -> openUnitsInfo(player, 2);
            case 3 -> openPointsInfo(player, 3);
            case 4 -> openSuperpowerInfo(player, 4);
            case 5 -> openPowerupInfo(player, 5);
            case 6 -> openBuyPointInfo(player, 6);
        }
    });

    public static void execute(Player player, int selectedOption) {
        openGui(player, selectedOption);
    }

    public static void openGui(Player player, int selectedOption) {
        String[][] buttons = {
            {"[gray]Close"},
            {"[lime]" + Bundle.get("info." + getOptionName(2), player.locale)},
            {"[purple]" + Bundle.get("info." + getOptionName(3), player.locale)},
            {"[red]" + Bundle.get("info." + getOptionName(4), player.locale)},
            {"[gold]" + Bundle.get("info." + getOptionName(5), player.locale)},
            {"[alphaaa]" + Bundle.get("info." + getOptionName(6), player.locale)}
        };
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("settings.message", player.locale), buttons);
    }

    private static String getOptionName(int option) {
        switch (option) {
            case 2: return "units";
            case 3: return "points";
            case 4: return "superpower";
            case 5: return "powerup";
            case 6: return "buypoint";
            default: return "";
        }
    }

    // Add methods for each new GUI here
    public static void openUnitsInfo(Player player, int selectedOption) {
        execute(player, selectedOption);
    }

    public static void openPointsInfo(Player player, int selectedOption) {
        execute(player, selectedOption);
    }

    public static void openSuperpowerInfo(Player player, int selectedOption) {
        execute(player, selectedOption);
    }

    public static void openPowerupInfo(Player player, int selectedOption) {
        execute(player, selectedOption);
    }

    public static void openBuyPointInfo(Player player, int selectedOption) {
        execute(player, selectedOption);
    }
}