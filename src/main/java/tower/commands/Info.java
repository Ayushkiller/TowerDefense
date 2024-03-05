package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Info {
    private static final String[] infoKeys = {"settings.Overview", "info.Buypoint","info.Powerup", "info.SuperPow", "info.Points", "info.Units"};

    private static final int menu = Menus.registerMenu((player, option) -> {
        if (option >= 0 && option < infoKeys.length) {
            String title = Bundle.get("settings.title", player.locale); // Use settings.title as the title
            String message = Bundle.get(infoKeys[option], player.locale);
            // Concatenate the title with the message
            Call.infoMessage(player.con, title + ": " + message);
        }
    });

    private static final String[][] buttons = new String[infoKeys.length][];
    static {
        for (int i = 0; i < infoKeys.length; i++) {
            String[] parts = infoKeys[i].split("\\.");
            String label = parts[parts.length - 1]; // Extract the part after "info."
            buttons[i] = new String[] {label, infoKeys[i]}; // Each button has a label and an associated key
        }
    }

    public static void execute(Player player) {
        openGui(player);
    }
    private static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), "", buttons);
    }
}