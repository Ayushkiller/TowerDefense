package tower.commands;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.world.Tile;


import java.util.Random;

public class SuperPowers {
    private static final float tilesize =  1.0f; // Adjust the value as needed
    public static void execute(Player player) {
        // Implement the logic for the Super Powers menu option here
        player.sendMessage("Super Powers menu option selected.");
    
        // Spawn  6 units around the player within a  40f radius
        Unit playerUnit = player.unit(); // Get the player's unit
        if (playerUnit != null) {
            float playerX = playerUnit.x; // Get the player's X position
            float playerY = playerUnit.y; // Get the player's Y position
            World world = Vars.world;// Get the World object from the unit
            spawnUnitsAroundPlayer(player, world, playerX, playerY);
        } else {
            player.sendMessage("Player unit is not available.");
        }
    }
    
    private static void spawnUnitsAroundPlayer(Player player, World world, float playerX, float playerY) {
        float radius =  80f;
        Random random = new Random();
    
        for (int i =  0; i <  6; i++) {
            // Calculate a random angle for the spawn position
            float angle = random.nextFloat() *  360;
            // Convert the angle to radians
            double radians = Math.toRadians(angle);
            // Calculate the spawn position
            float x = playerX + radius * (float) Math.cos(radians);
            float y = playerY + radius * (float) Math.sin(radians);
    
            // Convert the float coordinates to integers
            int intX = (int) x;
            int intY = (int) y;
            // Convert the integer coordinates to world coordinates
            float worldX = intX * tilesize;
            float worldY = intY * tilesize;
    
            // Find the tile at the world position
            Tile tile = world.tileWorld(worldX, worldY);
            if (tile != null) {
                // Spawn a Corvus unit at the tile
                // Assuming there's a method to spawn a unit of a specific type
                Unit unit = UnitTypes.corvus.spawn(worldX, worldY); // Adjusted to use UnitType.spawn
                if (unit != null) {
                    // Optionally, set the unit's target or other properties here
                }
            }
        }
    }

}

