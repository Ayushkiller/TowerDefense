package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

public class Info {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case   1 -> Menuforunits.execute(player); // Back 
        }
    });
    private static final String[][] buttons = {
        {"[gray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get("settings.message", player.locale), buttons);
    }


}