package tower;

import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;
import tower.commands.Settings;


import java.util.HashMap;
import java.util.Map;

public class Players {
    private static final Map<String, PlayerData> players = new HashMap<>();

    public static PlayerData getPlayer(Player player) {
        if (!players.containsKey(player.uuid())) {
            PlayerData playerData = new PlayerData(player);
            // Set the player's name in the PlayerData object
            playerData.setName(player.name());
            players.put(player.uuid(), playerData);
        }
        return players.get(player.uuid());
    }
    public static void clearMap() {
        players.clear();
    }

    public static void forEach() {
        Groups.player.each(player -> {
            if(Players.getPlayer(player).getUnit() != null && player.unit() != Players.getPlayer(player).getUnit()) {
                player.unit(Players.getPlayer(player).getUnit());
            }
    
            // Check if stats are enabled for all players
            if (Settings.isDisplayStatsForAll()) {
                // Display stats for all players
                displayStatsForAllPlayers(player);
            }
            PlayerData playerData = Players.getPlayer(player);
            if (!player.dead() && player.name().equals(playerData.getName()) && player.uuid().equals(playerData.getUuid())) {
                float currentPoints = playerData.getPoints();
                if (currentPoints != playerData.getLastUpdatedPoints()) {
                    StringBuilder hud = new StringBuilder();
                    hud.append("[green]Your Points ").append((int) currentPoints);
                    Call.setHudText(hud.toString());
                    playerData.setLastUpdatedPoints(currentPoints);
                }
            }
        });
    }

    private static void displayStatsForAllPlayers(Player player) {
        if (Settings.isDisplayStatsForAll()) {
            Groups.player.each(p -> {
                if (!p.dead()) {
                    String message = "[scarlet]" + (int) p.unit().health + "/" + (int) p.unit().type.health + "\n" +
                   "[accent]" + (int) p.unit().shield + " " +
                  "[green]" + (int) Players.getPlayer(p).getPoints() + "\n  ";
                   Call.label(player.con, message.replace("  ", Settings.isDisplayStatsForAll() ? "[lime]on" : "[scarlet]off"),   0.017f, p.x, p.y-16);
                }
            });
        }
    }
}




