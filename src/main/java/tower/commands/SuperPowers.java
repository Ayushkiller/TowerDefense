package tower.commands;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.world.Tile;
import tower.Players;
import tower.Domain.PlayerData;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SuperPowers {
    private static final float tilesize =  1.0f; // Adjust the value as needed

    public static void execute(Player player) {
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
            player.sendMessage("Not enough points to activate Super Powers. You need  150 points.");
            return;
        }

        float radius =  80f;
        int numberOfUnits =  6; // Number of units to spawn
        float angleStep =  360f / numberOfUnits; // Calculate the angle step for even spacing

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        for (int i =  0; i < numberOfUnits; i++) {
            float angle = i * angleStep;
            double radians = Math.toRadians(angle);
            float x = playerX + radius * (float) Math.cos(radians);
            float y = playerY + radius * (float) Math.sin(radians);

            int intX = (int) x;
            int intY = (int) y;
            float worldX = intX * tilesize;
            float worldY = intY * tilesize;

            Tile tile = world.tileWorld(worldX, worldY);
            if (tile != null) {
                spawnUnit(worldX, worldY, executor);
            }
        }

        playerData.subtractPoints(150, player);
    }

    private static void spawnUnit(float worldX, float worldY, ScheduledExecutorService executor) {
        Unit corvusUnit = UnitTypes.corvus.spawn(worldX, worldY);
        Unit collarisUnit = UnitTypes.collaris.spawn(worldX, worldY);

        UnitTypes.corvus.groundLayer = Layer.flyingUnit;
        UnitTypes.corvus.weapons.get(0).reload =  10f;
        UnitTypes.corvus.weapons.get(0).cooldownTime =  10f;

        UnitTypes.collaris.groundLayer = Layer.flyingUnit;
        UnitTypes.collaris.weapons.get(0).reload =  10f;
        UnitTypes.collaris.weapons.get(0).cooldownTime =  10f;
        UnitTypes.collaris.weapons.get(1).reload =  10f;
        UnitTypes.collaris.weapons.get(1).cooldownTime =  10f;

        executor.schedule(() -> {
            if (corvusUnit != null && !corvusUnit.dead()) {
                corvusUnit.kill();
            }
            if (collarisUnit != null && !collarisUnit.dead()) {
                collarisUnit.kill();
            }
        },  60, TimeUnit.SECONDS); // Schedule to kill after  60 seconds
    }
}