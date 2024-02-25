package tower;

import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;
import tower.commands.Settings;
import java.util.Iterator;

import java.util.HashMap;
import java.util.Map;

import arc.util.Timer;

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
// Iterate through all players using iterator
Iterator<Player> players = Groups.player.iterator();
while (players.hasNext()) {
  Player player = players.next();

  // Check player unit and update if needed
  if (Players.getPlayer(player).getUnit() != null && player.unit() != Players.getPlayer(player).getUnit()) {
    player.unit(Players.getPlayer(player).getUnit());
  }

  // Check stats display setting and display stats if enabled
  if (Settings.isDisplayStatsForAll()) {
    displayStatsForAllPlayers(player);
  }

  // Update HUD for the current player
  updatePlayerHud(player);
  }

}
      
      private static void updatePlayerHud(Player player) {
          PlayerData playerData = Players.getPlayer(player);
          if (!player.dead() && player.uuid().equals(playerData.getUuid())) {
              float currentPoints = playerData.getPoints();
              // Format the HUD text dynamically based on player data
              String hudText = "[green]" + player.name() + "'s Points: " + (int) currentPoints;

              // Use Call.setHudText to display the points on the client's HUD
              Call.setHudText(player.con, hudText);
          }
      }
              
    

 private static void displayStatsForAllPlayers(Player player) {
    if (Settings.isDisplayStatsForAll()) {
        Groups.player.each(p -> {
            if (!p.dead()) {
                Timer.schedule(() -> {
                    String message = "[green]"+player.name()+ "[red]Points" + (int) Players.getPlayer(p).getPoints() + "\n  ";
                    Call.label(player.con, message.replace("  ", Settings.isDisplayStatsForAll() ? "[lime]On" : "[scarlet]Off"),   0.017f, p.x, p.y-16);
                },  0f,  1f);
            }
        });
    }

 } 
}


