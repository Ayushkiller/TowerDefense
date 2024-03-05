package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Info {
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 1 -> Menuforunits.execute(player); // Back
            case 2 -> {
                int currentIndex = getCurrentIndex(player);
                showNextInfo(player, currentIndex);
            }
        }
    });

    private static final List<String> buttons = Arrays.asList("Close", "Next");

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        String[][] buttonsArray = buttons.stream()
                                         .map(button -> new String[]{button})
                                         .toArray(String[][]::new);
        String title = Bundle.get("settings.title", player.locale) + "\n" + Bundle.get("settings.message", player.locale);
        Call.menu(player.con, menu, title, "", buttonsArray);
    }
    
    private static final List<String> infoKeys = Arrays.asList("info.units", "info.points", "info.superpower", "info.powerup", "info.buypoint");
    private static final Map<Player, Integer> currentIndexMap = new HashMap<>();

    private static int getCurrentIndex(Player player) {
        return currentIndexMap.getOrDefault(player, 0);
    }

    private static void showNextInfo(Player player, int currentIndex) {
        String nextInfoKey = infoKeys.get(currentIndex % infoKeys.size());
        String[][] nextButtons = {
            {"[gray]Close"},
            {"[white]Next"}
        };
        Call.menu(player.con, menu, Bundle.get("settings.title", player.locale), Bundle.get(nextInfoKey, player.locale), nextButtons);
    
        // Increment the current index for the player
        currentIndexMap.put(player, currentIndex + 1);
    }
}