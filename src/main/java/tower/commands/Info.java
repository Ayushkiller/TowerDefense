package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

import java.util.logging.Logger;

public class Info {
    private static final Logger logger = Logger.getLogger(Info.class.getName());
    private static final int menu = Menus.registerMenu(Info::handleMenuOption);
    private static final String[][] buttons = {
        {"[gray]Close"},
        {"[accent]Next"}
    };
    private static int currentMessageIndex = 0;
    private static String[] messages;

    public static void execute(Player player) {
        logger.info("Executing Info command for player: " + player.name);
        initializeMessages(player.locale);
        openGui(player);
    }

    public static void openGui(Player player) {
        logger.info("Opening GUI for player: " + player.name);
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), messages[currentMessageIndex], buttons);
    }

    private static void nextMessage(Player player) {
        currentMessageIndex = (currentMessageIndex + 1) % messages.length;
        logger.info("Displaying next message for player: " + player.name);
        openGui(player);
    }

    private static void initializeMessages(String locale) {
        logger.info("Initializing messages for locale: " + locale);
        messages = new String[]{
            Bundle.get("settings.Overview", locale),
            Bundle.get("info.Buypoint", locale),
            Bundle.get("info.Powerup", locale),
            Bundle.get("info.SuperPow", locale),
            Bundle.get("info.Points", locale),
            Bundle.get("info.Units", locale)
        };
    }

    private static void handleMenuOption(Player player, int option) {
        logger.info("Handling menu option for player: " + player.name + ", option: " + option);
        switch (option) {
            case 0 -> Menuforunits.execute(player); // Back
            case 1 -> nextMessage(player); // Next
        }
    }
}