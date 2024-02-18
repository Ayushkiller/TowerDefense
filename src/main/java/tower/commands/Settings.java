package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;


public class Settings {
    public static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 0 -> Players.getPlayer(player).setStats(true); // On
            case 1 -> Players.getPlayer(player).setStats(false); // Off
            case 2 -> Menuforunits.execute(player); // Back
        }
    });

    private static final String[][] buttons = {
            {"[lime]On", "[red]Off"},
            {"[lightgray]Back", "[gray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("settings.message", player.locale), buttons);
    }
}
