package tower.commands;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import mindustry.world.Tile;
import tower.Bundle;
import tower.Players;
import tower.Domain.PlayerData;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SuperPowers {
    private static final float tilesize =   1.0f; // Adjust the value as needed

    public static void execute(Player player) {
        Unit playerUnit = player.unit(); // Get the player's unit
        if (playerUnit != null) {
            float playerX = playerUnit.x; // Get the player's X position
            float playerY = playerUnit.y; // Get the player's Y position
            World world = Vars.world; // Get the World object from the unit
            openUnitSelectionMenu(player, world, playerX, playerY);
        } else {
            player.sendMessage(Bundle.get("player.unit.not-available", player.locale()));
        }
    }

    private static void openUnitSelectionMenu(Player player, World world, float playerX, float playerY) {
        String[][] buttons = {
            {"Corvus"},
            {"Collaris"},
            {"Squad"}
        };
        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            if (option ==  0) {
                spawnUnit(player, world, playerX, playerY, UnitTypes.corvus);
            } else if (option ==  1) {
                spawnUnit(player, world, playerX, playerY, UnitTypes.collaris);
            } else if (option ==  2) {
                spawnArcOfUnits(player, world, playerX, playerY, UnitTypes.disrupt); 
            }
        }), "[lime]Choose a ability to use:", "", buttons);
    }
    

    private static void spawnUnit(Player player, World world, float playerX, float playerY, UnitType unitType) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getPoints() >=   150) {
            float radius =   80f;
            float angle = (float) (Math.random() *  360f); // Random angle for spawning
            double radians = Math.toRadians(angle);
            float x = playerX + radius * (float) Math.cos(radians);
            float y = playerY + radius * (float) Math.sin(radians);

            int intX = (int) x;
            int intY = (int) y;
            float worldX = intX * tilesize;
            float worldY = intY * tilesize;

            Tile tile = world.tileWorld(worldX, worldY);
            if (tile != null) {
                // Inside your spawnUnit method, after spawning the unit
                Unit unit = unitType.spawn(worldX, worldY);

                // Create a ScheduledExecutorService
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                // Schedule a task to kill the unit after  60 seconds
                executor.schedule(() -> {
                    if (unit != null && unit.isValid()) {
                        unit.kill();
                    }
                },  60, TimeUnit.SECONDS);

                if (unitType == UnitTypes.corvus) {
                    unitType.groundLayer = Layer.flyingUnit;
                    unitType.weapons.get(0).reload =   10f;
                    unitType.weapons.get(0).cooldownTime =   10f;
                } else if (unitType == UnitTypes.collaris) {
                    unitType.groundLayer = Layer.flyingUnit;
                    unitType.weapons.get(0).reload =   10f;
                    unitType.weapons.get(0).cooldownTime =   10f;
                }
                playerData.subtractPoints(150, player);
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-points", player.locale()));
        }
    }
    private static void spawnArcOfUnits(Player player, World world, float playerX, float playerY, UnitType unitType) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getPoints() >=  150 *  20) { // Assuming each unit costs  150 points
            float radius =  80f;
            float arcAngle =  60f;
            float angleStep = arcAngle /  20; // Divide the arc by the number of units
            for (int i =  0; i <  20; i++) {
                float angle = i * angleStep - arcAngle /  2; // Calculate the angle for each unit
                double radians = Math.toRadians(angle);
                float x = playerX + radius * (float) Math.cos(radians);
                float y = playerY + radius * (float) Math.sin(radians);

                int intX = (int) x;
                int intY = (int) y;
                float worldX = intX * tilesize;
                float worldY = intY * tilesize;

                Tile tile = world.tileWorld(worldX, worldY);
                if (tile != null) {
                    Unit unit = unitType.spawn(worldX, worldY);
                    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                    executor.schedule(() -> {
                        if (unit != null && unit.isValid()) {
                            unit.kill();
                        }
                    },  30, TimeUnit.SECONDS);
                }
            }
            playerData.subtractPoints(100 , player); // Subtract the total cost
        player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-points", player.locale()));
        }
    }
}