package tower.commands;

import java.awt.Color;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Players;
import tower.Domain.PlayerData;
import tower.Domain.Trailtable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Trail {
    private String type;
    private Color color;
    private Player attachedPlayer;
   
    public Trail(String type, Color color) {
        this.type = type;
        this.color = color;
    }
    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }
    public void attachToPlayer(Player player) {
        this.attachedPlayer = player;
        
    }

 


    private static final Map<String, Trail> trails = new HashMap<>();

    public static void initTrailsTable() {
        // Initialize the trails map based on the size of TrailsTable.trails
        for (Map<String, Object> trailMap : Trailtable.trails) {
            String type = (String) trailMap.get("type");
            Color color = (Color) trailMap.get("color");
            trails.put(type, new Trail(type, color));
        }
    }
public static void execute(Player player) {
    String[][] buttons = new String[trails.size()][1];
    int i = 0;
    for (Trail trail : trails.values()) {
        buttons[i][0] = trail.getType() + " " + trail.getColor().toString(); // Include the trail type and color in the button text
        i++;
    }
    Call.menu(player.con, Menus.registerMenu((player1, option) -> {
       if (option >= 0 && option < trails.size()) {
        Trail selectedTrail = trails.values().toArray(new Trail[0])[option];
        selectedTrail.attachToPlayer(player);

        // Assuming you have a method to get PlayerData for a player
        PlayerData playerData = Players.getPlayer(player);
        playerData.setCurrentTrail(selectedTrail);

        // Start the logic to make the trail follow the player
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (playerData.getCurrentTrail() != null ) {
                float playerX = player.x;
                float playerY = player.y;
                playerData.getCurrentTrail().updatePosition(playerX, playerY);
            }
        }, 0, 900, TimeUnit.MILLISECONDS); // Adjust the rate as needed
    } else {
        player.sendMessage("Invalid selection. Please try again.");
        }
    }), "Trail Selection", "", buttons);
}
private void updatePosition(float playerX, float playerY) {
   
    if (attachedPlayer != null) {
        attachedPlayer.x = playerX;
        attachedPlayer.y = playerY;
    }
}
}
