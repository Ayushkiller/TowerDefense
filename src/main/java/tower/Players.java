package tower;

import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class Players {
    public static final Map<String, PlayerData> players = new HashMap<>();

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
    public static void forEach(Consumer<PlayerData> action) {
        players.values().forEach(action);
    }

    public static void forEach() {
        Groups.player.each(player -> {
            PlayerData playerData = Players.getPlayer(player);
            // Ensure playerData is not null before proceeding
            if (playerData != null) {
                if(Players.getPlayer(player).getUnit() != null && player.unit() != Players.getPlayer(player).getUnit()) {
                    player.unit(Players.getPlayer(player).getUnit());
                }

                if (!player.dead() && player.name().equals(playerData.getName()) && player.uuid().equals(playerData.getUuid())) {
                    float currentCash = playerData.getCash();
                    if (currentCash != playerData.getLastUpdatedCash()) {
                        StringBuilder hud = new StringBuilder();
                        hud.append("[green]Cash for[white] " + playerData.getName() + " - [lime]" + (int) playerData.getCash() + "\n ");
                        Call.setHudText(player.con, hud.toString());
                        playerData.setLastUpdatedCash(currentCash);
                    }
                }
                
            }
        });
    }


}




