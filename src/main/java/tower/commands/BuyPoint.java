package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.ui.Menus;
import tower.Domain.PlayerData;
import tower.Bundle;
import tower.Players;
import tower.Domain.Currency;

import java.util.HashMap;
import java.util.Map;

public class BuyPoint {

    private static final Map<Object, Integer> pointValues = new HashMap<>();



    public static void execute(Player player) {
        openMenu(player);
    }

    private static void openMenu(Player player) {
        String[][] buttons = new String[Currency.itemsforcore.length][Currency.itemsforcore[0].length];

        // Create button titles and point values for each item.
        for (int i = 0; i < Currency.itemsforcore.length; i++) {
            for (int j = 0; j < Currency.itemsforcore[i].length; j++) {
                Item item = Currency.itemsforcore[i][j];
                buttons[i][j] = String.format("[lime]%s (+%d)", item.name, pointValues.get(item));
            }
        }

        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            Item chosenItem = Currency.itemsforcore[option / buttons[0].length][option % buttons[0].length];
            int pointsToAdd = pointValues.get(chosenItem);
            PlayerData playerData = Players.getPlayer(player);
            playerData.addPoints(pointsToAdd);
            player.sendMessage(Bundle.get("menu.buypoint.success"));
        }), Bundle.get("menu.buypoint.title", player.locale()), "", buttons);
    }
}
