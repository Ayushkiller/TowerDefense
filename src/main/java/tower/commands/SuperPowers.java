package tower.commands;

import arc.util.Timer;
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

public class SuperPowers {
    private static final float tilesize = 1.0f; // Adjust the value as needed

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
                { "Corvus" },
                { "Collaris" },
                { "Squad" },
                { "Magic" }
        };
        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            if (option == 0) {
                spawnCorvusUnit(player, world, playerX, playerY);
            } else if (option == 1) {
                spawnCollarisUnit(player, world, playerX, playerY);
            } else if (option == 2) {
                spawnArcOfUnits(player, world, playerX, playerY, UnitTypes.disrupt);
            } else if (option == 3) { 
                spawnDisruptUnit(player, world, playerX, playerY);
            }

        }), "[lime]Choose a ability to use:", "", buttons);
    }

    private static void spawnUnitWithType(Player player, World world, float playerX, float playerY, UnitType unitType) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getCash() >= 100) { // Ensure the player has enough Cash
            playerData.subtractCash(100); // Subtract Cash as soon as the player confirms the purchase
            float angleStep = 360f / 6;
            float radius = 100f; // Calculate the angle step for evenly spaced spawns
            final boolean[] allUnitsSpawned = {true};


            for (int i = 0; i < 6; i++) {
                int finalI = i;
                Timer.schedule(() -> {
                    float angle = finalI * angleStep; // Calculate the angle for each unit
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
                            // Apply configurations to the individual unit
                            if (unitType == UnitTypes.corvus) {
                                unit.type.groundLayer = Layer.flyingUnit;
                                unit.type.weapons.get(0).reload = 10f;
                                unit.type.weapons.get(0).cooldownTime = 10f;
                                unit.type.playerControllable = false;
                                unit.type.autoFindTarget = true;
                                unit.type.allowedInPayloads = false;
                            } else if (unitType == UnitTypes.collaris) {
                                unit.type.groundLayer = Layer.flyingUnit;
                                unit.type.weapons.get(0).reload = 10f;
                                unit.type.weapons.get(0).cooldownTime = 10f;
                                unit.type.playerControllable = false;
                                unit.type.autoFindTarget = true;
                                unit.type.allowedInPayloads = false;
                            }

                            Timer.schedule(() -> {
                                if (unit.isValid()) {
                                    unit.kill();
                                }
                            }, 50000L); // 50 seconds in milliseconds
                            
                            
                        } else {
                            allUnitsSpawned[0] = false;

                        }
                    } else {
                        allUnitsSpawned[0] = false;

                    }
                }, i * 0.1f); // Delay each spawn by 0.1 seconds
            }

            if (!allUnitsSpawned[0]) {
                playerData.addCash(100);
                player.sendMessage(Bundle.get("spawn.unit.failed", player.locale()));
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-Cash", player.locale()));
        }
    }

    private static void spawnArcOfUnits(Player player, World world, float playerX, float playerY, UnitType unitType) {
        PlayerData playerData = Players.getPlayer(player);
        int totalCost = 100;

        if (playerData.getCash() >= totalCost) {
            float radius = 140f;
            float arcAngle = 180f;
            float angleStep = arcAngle / 20; // Divide the arc by the number of units

            playerData.subtractCash(totalCost);

            for (int i = 0; i < 20; i++) {
                int finalI = i;
                Timer.schedule(() -> {
                    float angle = finalI * angleStep - arcAngle / 2; // Calculate the angle for each unit
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
                            player.sendMessage(Bundle.get("spawn.arc-of-units.failed", player.locale()));
                            playerData.addCash(totalCost);
                        } else {
                            unit.type.allowedInPayloads = false;
                            unit.type.playerControllable = false;
                            unit.type.autoFindTarget = true;
                            Timer.schedule(() -> {
                                if (unit.isValid()) {
                                    unit.kill();
                                }
                            }, 10000); // Adjusted to 10 seconds
                        }
                    } else {
                        player.sendMessage(Bundle.get("spawn.arc-of-units.failed", player.locale()));
                        playerData.addCash(totalCost);
                    }
                }, i * 0.1f); // Delay each spawn by 0.1 seconds
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-Cash", player.locale()));
        }
    }

    private static void spawnDisruptUnit(Player player, World world, float playerX, float playerY) {
        PlayerData playerData = Players.getPlayer(player);
        int totalCost = 40; // Total cost for 4 unit types

        if (playerData.getCash() >= totalCost) {
            float radius = 140f;
            float arcAngle = 180f;
            float angleStep = arcAngle / 20; // Divide the arc by the number of units

            playerData.subtractCash(totalCost);

            for (int i = 0; i < 20; i++) {
                int finalI = i;
                Timer.schedule(() -> {
                    float angle = finalI * angleStep - arcAngle / 2; // Calculate the angle for each unit
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
                        for (UnitType unitType : new UnitType[] { UnitTypes.zenith, UnitTypes.quell, UnitTypes.avert,
                                UnitTypes.flare }) {
                            Unit unit = unitType.spawn(worldX, worldY);
                            if (unit == null || !unit.isValid()) {
                                player.sendMessage(Bundle.get("spawn.arc-of-units.failed", player.locale()));
                                playerData.addCash(totalCost);
                                return;
                            }
                           
                            unit.type.allowedInPayloads = false;
                            unit.type.playerControllable = false;
                            unit.type.autoFindTarget = true;
                            Timer.schedule(() -> {
                                if (unit.isValid()) {
                                    unit.kill();
                                }
                            }, 10000); // Adjusted to 10 seconds
                        }
                    } else {
                        player.sendMessage(Bundle.get("spawn.arc-of-units.failed", player.locale()));
                        playerData.addCash(totalCost);
                        return;
                    }
                }, i * 0.1f); // Delay each spawn by 0.1 seconds
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-Cash", player.locale()));
        }
    }
}
