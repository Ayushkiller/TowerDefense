package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Info {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 0 -> {
                String[] infoKeys = {"settings.message", "info.buypoint","info.powerup", "info.superpower", "info.points", "info.units"};
                for (String key : infoKeys) {
                    Call.infoMessage(player.con, Bundle.get(key, player.locale));
                }
            }
            
        }
    });

    private static final String[][] buttons = {
        {"[lime]Next", "[lightgray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    private static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), "", buttons);
    }
}