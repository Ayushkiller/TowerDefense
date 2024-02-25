package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Settings {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case   0 -> toggleDisplayStatsForAll(); // Toggle stats for all players
            case   1 -> Menuforunits.execute(player); // Back
        }
    });
    private static final String[][] buttons = {
        {"[lime]On/Off"},
        {"[lightgray]Back", "[gray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("settings.message", player.locale), buttons);
    }

    public enum DisplayStatsMode {
        ON, OFF
    }
    private static DisplayStatsMode displayStatsMode = DisplayStatsMode.OFF;

    public static void toggleDisplayStatsForAll() {
        displayStatsMode = displayStatsMode == DisplayStatsMode.ON ? DisplayStatsMode.OFF : DisplayStatsMode.ON;
    }

    public static boolean isDisplayStatsForAll() {
        return displayStatsMode == DisplayStatsMode.ON;
    }

}