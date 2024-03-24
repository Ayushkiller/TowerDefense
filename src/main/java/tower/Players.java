// Tower package containing utility classes for managing player data
package tower;

import java.util.HashMap;
import java.util.Map;

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
            playerData.setName(player.name); // Set player name
            players.put(player.uuid(), playerData); // Add PlayerData instance to map
        }
        // Return PlayerData instance for given player
        return players.get(player.uuid());
    }

    // Method to clear all PlayerData instances from the map
    public static void clearMap() {
        players.clear();
    }

}