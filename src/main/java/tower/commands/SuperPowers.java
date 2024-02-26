package tower.commands;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.world.Tile;
import mindustry.world.draw.DrawLiquidRegion;
import tower.Players;
import tower.Domain.PlayerData;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class SuperPowers {
    private static final float tilesize =  1.0f; // Adjust the value as needed

    public static void execute(Player player) {


        // Spawn  6 units around the player within a  40f radius
        Unit playerUnit = player.unit(); // Get the player's unit
        if (playerUnit != null) {
            float playerX = playerUnit.x; // Get the player's X position
            float playerY = playerUnit.y; // Get the player's Y position
            World world = Vars.world; // Get the World object from the unit
            spawnUnitsAroundPlayer(player, world, playerX, playerY, playerUnit);
        } else {
            player.sendMessage("Player unit is not available.");
        }
    }

    private static void spawnUnitsAroundPlayer(Player player, World world, float playerX, float playerY, Unit playerUnit) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getPoints() <  150) {
            player.sendMessage("Not enough points to activate Super Powers.You need 150 points.");
            return;
        }

        float radius =  80f;
        int numberOfUnits =  6; // Number of units to spawn
        float angleStep =  360f / numberOfUnits; // Calculate the angle step for even spacing

        for (int i =  0; i < numberOfUnits; i++) {
            // Calculate the angle for the current unit
            float angle = i * angleStep;
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
                Unit corvusUnit = UnitTypes.corvus.spawn(worldX, worldY);
                Unit collarisunit = UnitTypes.collaris.spawn(worldX, worldY); // Adjusted to use UnitType.spawn
                UnitTypes.corvus.groundLayer = Layer.flyingUnit;

                UnitTypes.corvus.weapons.get(0).reload = 10f;

                UnitTypes.corvus.weapons.get(0).cooldownTime = 10f;
                UnitTypes.collaris.groundLayer = Layer.flyingUnit;

                UnitTypes.collaris.weapons.get(0).reload = 10f;

                UnitTypes.collaris.weapons.get(0).cooldownTime = 10f;
                UnitTypes.collaris.weapons.get(1).reload = 10f;

                UnitTypes.collaris.weapons.get(1).cooldownTime = 10f;


                UnitTypes.latum.crushDamage= 50f/10f;    
                 // Inside the spawnUnitsAroundPlayer method, after spawning the units
                  ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                  if (corvusUnit != null) {
                    executor.schedule(() -> {
                        if (corvusUnit != null && !corvusUnit.dead()) {
                            corvusUnit.kill();
                        }
                    },  60, TimeUnit.SECONDS); // Schedule to kill after  60 seconds
                }
                
                if (collarisunit != null) {
                    executor.schedule(() -> {
                        if (collarisunit != null && !collarisunit.dead()) {
                            collarisunit.kill();
                        }
                    },  60, TimeUnit.SECONDS); // Schedule to kill after  60 seconds
                }
            }
        }

        // Deduct the points for activating Super Powers
        playerData.subtractPoints(150, player);
    }
}