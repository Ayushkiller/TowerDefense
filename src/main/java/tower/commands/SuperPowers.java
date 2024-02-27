package tower.commands;

import mindustry.Vars;
import mindustry.content.StatusEffects;
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
    private static void spawnCorvusUnit(Player player, World world, float playerX, float playerY) {
        spawnUnitWithType(player, world, playerX, playerY, UnitTypes.corvus);
    }
    
    private static void spawnCollarisUnit(Player player, World world, float playerX, float playerY) {
        spawnUnitWithType(player, world, playerX, playerY, UnitTypes.collaris);
    }
    private static void openUnitSelectionMenu(Player player, World world, float playerX, float playerY) {
        String[][] buttons = {
            {"Corvus"},
            {"Collaris"},
            {"Squad"},
            {"Disrupt"},
            {"Omni Tower"} 
        };
        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            if (option ==  0) {
                spawnCorvusUnit(player, world, playerX, playerY);
            } else if (option ==  1) {
                spawnCollarisUnit(player, world, playerX, playerY);
            } else if (option ==  2) {
                spawnArcOfUnits(player, world, playerX, playerY, UnitTypes.disrupt);
            } else if (option ==  3) { // Add this block
                spawnDisruptUnit(player, world, playerX, playerY);
            }
             else if (option ==  4) { // Add this block
            specialSpawn(player, world, playerX, playerY);
            }
            
        }), "[lime]Choose a ability to use:", "", buttons);
    }
    
    private static void spawnUnitWithType(Player player, World world, float playerX, float playerY, UnitType unitType) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getPoints() >=  40) {
            float angleStep =  360f /  6;
            float radius =  100f; // Calculate the angle step for evenly spaced spawns
            for (int i =  0; i <  6; i++) {
                float angle = i * angleStep; // Calculate the angle for each unit
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
                    if (unit != null && unit.isValid()) {
                        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                        executor.schedule(() -> {
                            if (unit != null && unit.isValid()) {
                                unit.kill();
                            }
                        },  50, TimeUnit.SECONDS);
    
                        if (unitType == UnitTypes.corvus) {
                            unitType.groundLayer = Layer.flyingUnit;
                            unitType.weapons.get(0).reload =  10f;
                            unitType.weapons.get(0).cooldownTime =  10f;
                        } else if (unitType == UnitTypes.collaris) {
                            unitType.groundLayer = Layer.flyingUnit;
                            unitType.weapons.get(0).reload =  10f;
                            unitType.weapons.get(0).cooldownTime =  10f;
                        }
                    } else {
                        playerData.addPoints(40, player);
                        player.sendMessage(Bundle.get("spawn.unit.failed", player.locale()));
                    }
                } else {
                    player.sendMessage(Bundle.get("spawn.unit.invalid-location", player.locale()));
                }
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-points", player.locale()));
        }
    }
    private static void spawnArcOfUnits(Player player, World world, float playerX, float playerY, UnitType unitType) {
        PlayerData playerData = Players.getPlayer(player);
        int unitCost =   40; // Cost per unit
        int totalCost = unitCost; // Total cost remains   40 for all units

        if (playerData.getPoints() >= totalCost) {
            float radius =   140f;
            float arcAngle =   180f;
            float angleStep = arcAngle /   20; // Divide the arc by the number of units
            boolean allUnitsSpawned = true;

            for (int i =   0; i <   20; i++) {
                float angle = i * angleStep - arcAngle /   2; // Calculate the angle for each unit
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
                    if (unit == null || !unit.isValid()) {
                        allUnitsSpawned = false;
                        break;
                    }
                    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                    executor.schedule(() -> {
                        if (unit != null && unit.isValid()) {
                            unit.kill();
                        }
                    },   10, TimeUnit.SECONDS); // Adjusted to  5 seconds
                } else {
                    allUnitsSpawned = false;
                    break;
                }
            }

            if (allUnitsSpawned) {
                playerData.subtractPoints(totalCost, player); // Subtract the total cost
                player.sendMessage(Bundle.get("spawn.arc-of-units.success", player.locale()));
            } else {
                // If any unit was not successfully spawned, give back the points
                playerData.addPoints(totalCost, player);
                player.sendMessage(Bundle.get("spawn.arc-of-units.failed", player.locale()));
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-points", player.locale()));
        }
    }
    private static void spawnDisruptUnit(Player player, World world, float playerX, float playerY) {
        PlayerData playerData = Players.getPlayer(player);
        int price =  100; // Set the price to  100
        if (playerData.getPoints() >= price) {
            playerData.subtractPoints((float) price, player);
    
            // Spawn both disrupt and eclipse units for the player
            UnitType spawnType1 = UnitTypes.disrupt;
            UnitType spawnType2 = UnitTypes.eclipse;
            ScheduledExecutorService spawnExecutor = Executors.newSingleThreadScheduledExecutor();
            Unit spawned1 = spawnType1.spawn(playerX, playerY);
            Unit spawned2 = spawnType2.spawn(playerX, playerY);
    
            // Schedule a task to continuously spawn units within a radius of  80 units from the player
            spawnExecutor.scheduleAtFixedRate(() -> {
                if (player.unit() == spawned1 || player.unit() == spawned2) { // Check if the player is still controlling either of the spawned units
                    spawnUnitsWithinRadius(player, world, playerX, playerY,  80f, UnitTypes.zenith, UnitTypes.quell);
                    spawnUnitsWithinRadius(player, world, playerX, playerY,  140f, UnitTypes.flare, UnitTypes.avert);
                }
            },  0,  1, TimeUnit.SECONDS); // Check every second
    
            // Schedule a task to check every  5 seconds if the player is still controlling either of the spawned units
            ScheduledExecutorService controlCheckExecutor = Executors.newSingleThreadScheduledExecutor();
            controlCheckExecutor.scheduleAtFixedRate(() -> {
                if (player.unit() != spawned1 && player.unit() != spawned2) {
                    spawnExecutor.shutdown(); // Stop spawning if the player leaves the units
                    controlCheckExecutor.shutdown(); // Shutdown the control check executor as well
                }
            },  0,  5, TimeUnit.SECONDS); // Check every  5 seconds
    
            player.sendMessage(Bundle.get("unit.brought", player.locale));
        } else {
            player.sendMessage(Bundle.get("menu.units.not-enough.l", player.locale()));
        }
    }
    private static void specialSpawn(Player player, World world, float playerX, float playerY) {
    PlayerData playerData = Players.getPlayer(player);
    int price =  100; // Set the price to  200
    if (playerData.getPoints() >= price) {
        playerData.subtractPoints(price, player);

        // Define the units to spawn
        UnitType[] unitsToSpawn = {
            UnitTypes.corvus,
            UnitTypes.corvus,
            UnitTypes.corvus,
            UnitTypes.collaris,
            UnitTypes.collaris,
            UnitTypes.conquer,
            UnitTypes.conquer,
            UnitTypes.eclipse,
            UnitTypes.oct,
            UnitTypes.toxopid,
            UnitTypes.reign
        };

        // Spawn the units and apply the unmoving status effect
        for (UnitType unitType : unitsToSpawn) {
            Unit unit = unitType.spawn(playerX, playerY);
            if (unit != null && unit.isValid()) {
                unit.apply(StatusEffects.unmoving, Float.POSITIVE_INFINITY);
                unit.type.physics = false;
                unit.type.autoFindTarget = true;
                unit.type.alwaysUnlocked = true;
            }
        }

        player.sendMessage(Bundle.get("special.spawn.success", player.locale));
    } else {
        player.sendMessage(Bundle.get("menu.units.not-enough.l", player.locale()));
    }
}
private static void spawnUnitsWithinRadius(Player player, World world, float playerX, float playerY, float radius, UnitType... unitTypes) {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    int totalUnits = unitTypes.length;
    double angleStep =  360.0 / totalUnits; // Calculate the angle step for evenly spaced spawns

    executor.scheduleAtFixedRate(() -> {
        for (int i =  0; i < totalUnits; i++) {
            double radians = Math.toRadians(i * angleStep);
            float x = playerX + radius * (float) Math.cos(radians);
            float y = playerY + radius * (float) Math.sin(radians);

            int intX = (int) x;
            int intY = (int) y;
            float worldX = intX * tilesize;
            float worldY = intY * tilesize;

            Tile tile = world.tileWorld(worldX, worldY);
            if (tile != null) {
                Unit unit = unitTypes[i].spawn(worldX, worldY);
                if (unit != null && unit.isValid()) {
                    // Schedule a single executor for all units to be killed after   6 seconds
                    executor.schedule(() -> {
                        if (unit != null && unit.isValid()) {
                            unit.kill();
                        }
                    },   6, TimeUnit.SECONDS);
                }
            }
        }
    },   0,   4, TimeUnit.SECONDS); // Spawn units every   4 seconds
}
}