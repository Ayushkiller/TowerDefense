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
    private static String[] messages;

    public static void execute(Player player) {
        initializeMessages(player.locale);
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), messages[currentMessageIndex], buttons);
    }

    private static void nextMessage(Player player) {
        currentMessageIndex = (currentMessageIndex + 1) % messages.length;
        openGui(player);
    }

    private static void initializeMessages(String locale) {
        messages = new String[]{
            Bundle.get("settings.Overview", locale),
            Bundle.get("info.Buypoint", locale),
            Bundle.get("info.Powerup", locale),
            Bundle.get("info.SuperPow", locale),
            Bundle.get("info.Points", locale),
            Bundle.get("info.Units", locale)
        };
    }
}