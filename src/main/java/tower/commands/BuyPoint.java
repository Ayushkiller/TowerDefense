package tower.commands;

import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.ui.Menus;
import tower.Domain.PlayerData;
import tower.Bundle;
import tower.Players;
import tower.Domain.Currency;


public class BuyPoint {

    public static void execute(Player player) {

        openMenu(player);

}

private static void openMenu(Player player) {
    String[][] buttons = new String[Currency.itemsforcore.length][Currency.itemsforcore[0].length];

    // Create button titles and point values for each item.
    for (int i =  0; i < Currency.itemsforcore.length; i++) {
        for (int j =  0; j < Currency.itemsforcore[i].length; j++) {
            Item item = Currency.itemsforcore[i][j];
            buttons[i][j] = String.format("[lime]%s (+%d)", item.name, Currency.Gain[i][j]);
        }
    }

    Call.menu(player.con, Menus.registerMenu((player1, option) -> {
        PlayerData playerData = Players.getPlayer(player);
        Team team = player.team();

        // Check if team has enough items before purchase
        if (hasEnoughItems(team, option)) {
            int pointsToAdd = Currency.Gain[option / Currency.itemsforcore[0].length][option % Currency.itemsforcore[0].length];
            playerData.addPoints(pointsToAdd);

            // Remove items from team storage
            removeItemsFromTeam(team, option);

            player.sendMessage(Bundle.get("menu.buypoint.success"));
        } else {
            player.sendMessage(Bundle.get("menu.buypoint.notenoughitems"));
        }
    }), Bundle.get("menu.buypoint.title", player.locale()), "", buttons);
}

private static boolean hasEnoughItems(Team team, int option) {
    for (int i = 0; i < Currency.itemsforcore[option / Currency.itemsforcore[0].length].length; i++) {
        Item item = Currency.itemsforcore[option / Currency.itemsforcore[0].length][i];
        int requiredAmount = Currency.Priceforitems[option / Currency.itemsforcore[0].length][i];
        if (team.items().get(item) < requiredAmount) {
            return false;
        }
    }
    return true;
}

private static void removeItemsFromTeam(Team team, int option) {
    for (int i = 0; i < Currency.itemsforcore[option / Currency.itemsforcore[0].length].length; i++) {
        Item item = Currency.itemsforcore[option / Currency.itemsforcore[0].length][i];
        int amountToRemove = Currency.Priceforitems[option / Currency.itemsforcore[0].length][i];
        team.items().remove(item, amountToRemove);
    }
}

}

