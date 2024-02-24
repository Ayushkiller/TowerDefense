package tower;

import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;
import java.util.HashMap;
import java.util.Map;

public class Players {
    private static final Map<String, PlayerData> players = new HashMap<>();

    public static PlayerData getPlayer(Player player) {
        if (!players.containsKey(player.uuid())) {
            PlayerData playerData = new PlayerData(player);
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

            if(Players.getPlayer(player).stats()) {
                // Display stats for all players
                displayStatsForAllPlayers(player);
            }

            // Update the HUD to show only the current player's points
            StringBuilder hud = new StringBuilder();
            hud.append("[green]").append((int) Players.getPlayer(player).getPoints());
            Call.setHudText(hud.toString());

            // Ensure stats are shown for the current player using their UUID
            if (player.uuid().equals(Players.getPlayer(player).getUuid())) {
                // Display stats for the current player
                displayStatsForCurrentPlayer(player);
            }
        });
    }

    private static void displayStatsForAllPlayers(Player player) {
        Groups.player.each(p -> {
            if (!p.dead()) {
                String message = "[scarlet]" + (int) p.unit().health + "/" + (int) p.unit().type.health + "\n" +
                        "[accent]" + (int) p.unit().shield + " " +
                        "[green]" + (int) Players.getPlayer(p).getPoints() + "\n  ";
                Call.label(player.con, message.replace("  ", Players.getPlayer(p).stats()? "[lime]true" : "[scarlet]false"),   0.017f, p.x, p.y-16);
            }
        });
    }

    private static void displayStatsForCurrentPlayer(Player player) {
        String message = "[scarlet]" + (int) player.unit().health + "/" + (int) player.unit().type.health + "\n" +
                "[accent]" + (int) player.unit().shield + " " +
                "[green]" + (int) Players.getPlayer(player).getPoints() + "\n  ";
        Call.label(player.con, message.replace("  ", Players.getPlayer(player).stats()? "[lime]true" : "[scarlet]false"),   0.017f, player.x, player.y-16);
    }
}
