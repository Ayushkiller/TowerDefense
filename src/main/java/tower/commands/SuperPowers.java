package tower.commands;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.game.EventType.PlayerLeave;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.Menus;
import mindustry.world.Tile;
import tower.Bundle;
import tower.Players;
import tower.Domain.PlayerData;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import arc.Events;

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
            {"Magic"},
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
        if (playerData.getPoints() >= 100) { // Ensure the player has enough points
            playerData.subtractPoints(100, player); // Subtract points as soon as the player confirms the purchase
            float angleStep = 360f / 6;
            float radius = 100f; // Calculate the angle step for evenly spaced spawns
            for (int i = 0; i < 6; i++) {
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
                        }, 50, TimeUnit.SECONDS);

                        if (unitType == UnitTypes.corvus) {
                            unitType.groundLayer = Layer.flyingUnit;
                            unitType.weapons.get(0).reload = 10f;
                            unitType.weapons.get(0).cooldownTime = 10f;
                        } else if (unitType == UnitTypes.collaris) {
                            unitType.groundLayer = Layer.flyingUnit;
                            unitType.weapons.get(0).reload = 10f;
                            unitType.weapons.get(0).cooldownTime = 10f;
                        }
                    } else {
                        playerData.addPoints(100, player);
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
        int unitCost =   100; // Cost per unit
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
    int unitCost =   40; // Cost per unit
    int totalCost = unitCost; // Total cost for   4 unit types

    if (playerData.getPoints() >= totalCost) {
        float radius =   140f;
        float arcAngle =   180f;
        float angleStep = arcAngle /   20; // Divide the arc by the number of units
        boolean[] allUnitsSpawned = new boolean[]{true};

        // Define the duration for spawning units in seconds
        long spawnDuration =   4; //   10 seconds

        // Schedule the spawning of units over the specified duration
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> scheduledTask = executor.schedule(() -> {
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
                    // Spawn Zenith, Quell, Avert, and Flare units
                    for (UnitType unitType : new UnitType[]{UnitTypes.zenith, UnitTypes.quell, UnitTypes.avert, UnitTypes.flare}) {
                        Unit unit = unitType.spawn(worldX, worldY);
                        if (unit == null || !unit.isValid()) {
                            allUnitsSpawned[0] = false;
                            break;
                        }
                        executor.schedule(() -> {
                            if (unit != null && unit.isValid()) {
                                unit.kill();
                            }
                        },   120, TimeUnit.SECONDS); // Adjusted to   5 seconds
                    }
                } else {
                    allUnitsSpawned[0] = false;
                    break;
                }
            }

            if (allUnitsSpawned[0]) {
                playerData.subtractPoints(totalCost, player); // Subtract the total cost
                player.sendMessage(Bundle.get("spawn.arc-of-units.success", player.locale()));
            } else {
                // If any unit was not successfully spawned, give back the points
                playerData.addPoints(totalCost, player);
                player.sendMessage(Bundle.get("spawn.arc-of-units.failed", player.locale()));
            }
        }, spawnDuration, TimeUnit.SECONDS);

        // Add a listener for the PlayerLeave event to cancel the scheduled task
        Events.on(PlayerLeave.class, event -> {
            if (event.player == player) {
                scheduledTask.cancel(false); // Cancel the scheduled task
            }
        });
    } else {
        player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-points", player.locale()));
    }
}
    private static void specialSpawn(Player player, World world, float playerX, float playerY) {
    PlayerData playerData = Players.getPlayer(player);
    int price =  400; // Set the price to  200
    if (playerData.getPoints() >= price) {
        playerData.subtractPoints(price, player);

        // Define the units to spawn
        UnitType[] unitsToSpawn = {
            UnitTypes.corvus,
            UnitTypes.collaris,
            UnitTypes.conquer,
            UnitTypes.eclipse,
            UnitTypes.oct,
            UnitTypes.toxopid,
            UnitTypes.reign
        };
                Weapon[] weaponsToAdd = {
            UnitTypes.corvus.weapons.get(0),
            UnitTypes.reign.weapons.get(0),
            UnitTypes.reign.weapons.get(1),
            UnitTypes.conquer.weapons.get(0),
            UnitTypes.toxopid.weapons.get(0),
            UnitTypes.toxopid.weapons.get(1),
            UnitTypes.eclipse.weapons.get(2)
        };
        for (UnitType unitType : unitsToSpawn) {
            Unit unit = unitType.spawn(playerX, playerY);
            if (unit != null && unit.isValid()) {
         
                for(Weapon wpn: weaponsToAdd){unit.type.weapons.add(wpn);}
                unit.type.speed=0.000001f;
                unit.type.playerControllable=false;
                unit.type.alwaysShootWhenMoving=true;
                unit.type.physics = false;
                unit.type.autoFindTarget = true;
                unit.type.alwaysUnlocked = true;
                unit.type.abilities.add(UnitTypes.oct.abilities.get(0), UnitTypes.oct.abilities.get(1));
            }
        }

        player.sendMessage(Bundle.get("special.spawn.success", player.locale));
    } else {
        player.sendMessage(Bundle.get("menu.units.not-enough.l", player.locale()));
    }
}

}