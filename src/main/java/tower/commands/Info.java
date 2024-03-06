package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;

import java.util.Arrays;
import java.util.List;

public class Info {
    private static final List<String> infoKeys = Arrays.asList("settings.Overview", "info.Buypoint", "info.Powerup", "info.SuperPow", "info.Points", "info.Units");
    private static int currentPage = 0; // Store the current page as a class-level variable

    private static int registerMenu() {
        return Menus.registerMenu((player, option) -> {
            handleMenuOption(player, option);
        });
    }

    private static void handleMenuOption(Player player, int option) {
        if (option == 0) { // Close button
            displayMenu(player);
        } else if (option == 1) { // Next button
            navigateToNextPage(player);
        }
    }

    private static void displayMenu(Player player) {
        String[][] buttonArray = new String[][]{
                {"Next"},
                {"Close"}
        };
        Call.menu(player.con, registerMenu(), Bundle.get("settings.title", player.locale), "", buttonArray);
    }

    private static void navigateToNextPage(Player player) {
        if (currentPage < infoKeys.size() - 1) {
            currentPage++;
            displayInfo(player);
        } else {
            displayMenu(player);
        }
    }

    private static void displayInfo(Player player) {
        String infoKey = infoKeys.get(currentPage);
        String message = Bundle.get(infoKey, player.locale);
        Call.infoMessage(player.con, message);
    }

    public static void execute(Player player) {
        currentPage = 0; // Reset current page to 0
        displayMenu(player);
    }
}