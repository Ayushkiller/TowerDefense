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
/**
 * Handles the purchase of points in the tower defense game.
 * This class provides functionality to open a menu for players to buy points using items as currency.
 */

public class BuyPoint {
    
    /**
     * Executes the purchase of points by opening the buy point menu for the player.
     *
     * @param player The player who is attempting to buy points.
     */

    public static void execute(Player player) {

        openMenu(player);

}
    /**
     * Opens the buy point menu for the player.
     * The menu displays the items that can be used to purchase points and the corresponding point values.
     *
     * @param player The player for whom the menu is being opened.
     */
     private static void openMenu(Player player) {
    String[][] buttons = new String[Currency.itemsforcore.length][Currency.itemsforcore[0].length];

    // Create button titles and point values for each item.
    for (int i =  0; i < Currency.itemsforcore.length; i++) {
        for (int j =  0; j < Currency.itemsforcore[i].length; j++) {
            Item item = Currency.itemsforcore[i][j];
            int cost = Currency.Priceforitems[i][j];
            int gain = Currency.Gain[i][j];
            buttons[i][j] = String.format("[lime]%s (+%d) [gray](%d)", item.emoji(), gain, cost);
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
    /**
     * Checks if the team has enough items to purchase points.
     *
     * @param team The team that is attempting to purchase points.
     * @param option The index of the item and tier to check for sufficient quantity.
     * @return true if the team has enough items, false otherwise.
     */ //will be used for future//
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
    /**
     * Removes the items used to purchase points from the team's storage.
     *
     * @param team The team from which the items will be removed.
     * @param option The index of the item and tier to remove from the team's storage.
     */
    private static void removeItemsFromTeam(Team team, int option) {
    int tier = option / Currency.itemsforcore[0].length;
    int itemIndex = option % Currency.itemsforcore[0].length;
    Item item = Currency.itemsforcore[tier][itemIndex];
    int amountToRemove = Currency.Priceforitems[tier][itemIndex];
    team.items().remove(item, amountToRemove);
}
}



