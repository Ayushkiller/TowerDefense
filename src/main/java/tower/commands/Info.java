package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;



public class Info {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 1 -> Menuforunits.execute(player); 
        }
    });
    private static final String[][] buttons = {
        {"[gray]Close","[lime]Units","[blue]Points"},
        {"[red]Superpower","[orange]Powerup","[gold]Buy Points"}
    };

    public static void execute(Player player) {
        // Display the initial menu with settings.message
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("settings.message", player.locale), buttons);
    }
    public static void handleSelection(Player player, int selectedOption) {

        execute(player, selectedOption);
    }
    public static void execute(Player player, int selectedOption) {
        String messageKey;
        switch (selectedOption) {
            case 2:
                messageKey = "info.units";
                break;
            case 3:
                messageKey = "info.points";
                break;
            case 4:
                messageKey = "info.superpower";
                break;
            case 5:
                messageKey = "info.powerup";
                break;
            case 6:
                messageKey = "info.buypoint";
                break;
            default:
                messageKey = "settings.message"; // Default message
        }
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get(messageKey, player.locale), buttons);
    }
    

}