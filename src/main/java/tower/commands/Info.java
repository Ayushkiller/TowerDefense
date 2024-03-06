package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Info {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 1 -> Menuforunits.execute(player); // Back
            case 2 -> nextMessage(player); // Next
        }
    });
    private static final String[][] buttons = {
        {"[gray]Close"},
        {"[accent]Next"}
    };
    private static int currentMessageIndex = 0;
    private static String[] messages = {
        Bundle.get("settings.Overview", "en"), 
        Bundle.get("info.Buypoint", "en"),
        Bundle.get("info.Powerup", "en"),
        Bundle.get("info.SuperPow", "en"),
        Bundle.get("info.Points", "en"),
        Bundle.get("info.Units", "en"),
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), messages[currentMessageIndex], buttons);
    }

    private static void nextMessage(Player player) {
        currentMessageIndex = (currentMessageIndex + 1) % messages.length;
        openGui(player);
    }
}