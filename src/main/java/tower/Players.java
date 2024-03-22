// Tower package containing utility classes for managing player data
package tower;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    // Method to iterate over all PlayerData instances and perform a given action
    public static void forEach(Consumer<PlayerData> action) {
        players.values().forEach(action);
    }

    // Method to update HUD text for each connected player
    public static void forEach() {
        // Iterate over all connected players
        Groups.player.each(player -> {
            PlayerData playerData = Players.getPlayer(player); // Get PlayerData instance for player
            if (playerData != null) { // If PlayerData instance exists
                float currentCash = playerData.getCash(); // Get current cash
                StringBuilder hud = new StringBuilder(); // Initialize StringBuilder for HUD text
                hud.append("[green]Cash for[white] " + playerData.getName() + " - [lime]") // Append cash text
                        .append((int) playerData.getCash()); // Append current cash
                Call.setHudText(player.con, hud.toString()); // Set HUD text for player
                playerData.setLastUpdatedCash(currentCash); // Update last updated cash
            }
        });
    }
}