// Tower package containing utility classes for managing player data
package tower;

import java.util.HashMap;
import java.util.Map;

import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;

// Players class containing utility methods for managing player data
public class Players {
    // Static map to store PlayerData instances using player UUID as key
    public static final Map<String, PlayerData> players = new HashMap<>();

    // Method to get PlayerData instance for a given Player object
    public static PlayerData getPlayer(Player player) {
        // If player not present in map, create new PlayerData instance
        if (!players.containsKey(player.uuid())) {
            PlayerData playerData = new PlayerData(player);
            players.put(player.uuid(), playerData); // Add PlayerData instance to map
        }
        // Return PlayerData instance for given player
        return players.get(player.uuid());
    }

    // Method to clear all PlayerData instances from the map
    public static void clearMap() {
        players.clear();
    }

    // Method to update HUD text for each connected player
    public static void forEach() {
        // Iterate over all connected players
        Groups.player.each(player -> {
            PlayerData playerData = Players.getPlayer(player); // Get PlayerData instance for player
            if (playerData != null) { // If PlayerData instance exists
                // Initialize StringBuilder for HUD text
                String hud = "[green]Cash for[white] " + player.name + " - [lime]" + // Append cash text
                        (int) playerData.getCash(); // Append current cash
                Call.setHudText(player.con, hud); // Set HUD text for player
            }
        });
    }
}