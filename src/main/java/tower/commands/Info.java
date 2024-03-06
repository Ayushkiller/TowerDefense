package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Info {
    private static final List<String> infoKeys = Arrays.asList("settings.Overview", "info.Buypoint", "info.Powerup", "info.SuperPow", "info.Points", "info.Units");
    private static final Map<String, String[]> buttons = infoKeys.stream()
            .collect(Collectors.toMap(
                    key -> key.split("\\.")[1], // Extract the part after "info." as label
                    key -> new String[]{key.split("\\.")[1], key} // Each button has a label and an associated key
            ));

    private static final int menu = Menus.registerMenu((player, option) -> {
        if (option >= 0 && option < infoKeys.size()) {
            String infoKey = infoKeys.get(option);
            String message = Bundle.get(infoKey, player.locale);
            Call.infoMessage(player.con, message);
        }
    });

    public static void execute(Player player) {
        openGui(player);
    }

    private static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), "", buttons.values().toArray(new String[0][]));
    }
}