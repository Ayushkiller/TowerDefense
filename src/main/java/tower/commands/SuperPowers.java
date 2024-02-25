package tower.commands;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.entities.units.AIController;
import mindustry.gen.Player;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.world.Tile;

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
            World world = Vars.world; // Get the World object from the unit
            spawnUnitsAroundPlayer(player, world, playerX, playerY, playerUnit);
        } else {
            player.sendMessage("Player unit is not available.");
        }
    }

    private static void spawnUnitsAroundPlayer(Player player, World world, float playerX, float playerY, Unit playerUnit) {
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
                Unit unit = UnitTypes.corvus.spawn(worldX, worldY); // Adjusted to use UnitType.spawn
                if (unit != null) {
                    // Set the unit's target to the player's position
                    unit.controller(new PlayerIndependentAI(playerUnit));
                }
            }
        }
    }

// I will fix it later asssssssssssssssssss

    private static class PlayerIndependentAI extends AIController {
        private final Unit targetUnit;

        public PlayerIndependentAI(Unit targetUnit) {
            this.targetUnit = targetUnit;
        }

        @Override
        public boolean checkTarget(Teamc target, float x, float y, float range) {
            // Check if the target is the player's unit
            return ((AIController) target).unit() == targetUnit;
        }

    }
}