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
    private static final float tileSize = 1.0f; 
    private static final int unitCost = 100;
    private static final int unitLife = 50_000; 
    private static final int arcCost = 100;
    private static final int DISRUPT_unitCost = 40;
    private static final float ARC_RADIUS = 140f;
    private static final float ARC_ANGLE = 180f;
    private static final int ARC_UNIT_COUNT = 20;

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
            {"Squad"},
            {"Magic"}
        };

        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            switch (option) {
                case 0 -> spawnUnits(player, world, playerX, playerY, UnitTypes.corvus, 6, unitCost);
                case 1 -> spawnUnits(player, world, playerX, playerY, UnitTypes.collaris, 6, unitCost);
                case 2 -> spawnArcOfUnits(player, world, playerX, playerY, UnitTypes.disrupt, ARC_UNIT_COUNT, arcCost);
                case 3 -> spawnDisruptUnits(player, world, playerX, playerY);
            }
        }), "[lime]Choose an ability to use:", "", buttons);
    }

    private static void spawnUnits(Player player, World world, float playerX, float playerY, UnitType unitType, int unitCount, int cost) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getCash() >= cost) {
            playerData.subtractCash(cost); 
            float angleStep = 360f / unitCount;
            float radius = 100f;

            for (int i = 0; i < unitCount; i++) {
                int finalI = i;
                Timer.schedule(() -> {
                    float angle = finalI * angleStep;
                    float x = playerX + radius * (float) Math.cos(Math.toRadians(angle));
                    float y = playerY + radius * (float) Math.sin(Math.toRadians(angle));
                    spawnAndConfigureUnit(player, world, x, y, unitType, unitLife);
                }, i * 0.1f); // Delay each spawn by 0.1 seconds
            }
        } else {
            player.sendMessage(Bundle.get("spawn.unit.not-enough-cash", player.locale()));
        }
    }

    private static void spawnArcOfUnits(Player player, World world, float playerX, float playerY, UnitType unitType, int unitCount, int cost) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getCash() >= cost) {
            playerData.subtractCash(cost);
            float angleStep = ARC_ANGLE / unitCount;

            for (int i = 0; i < unitCount; i++) {
                int finalI = i;
                Timer.schedule(() -> {
                    float angle = finalI * angleStep - ARC_ANGLE / 2;
                    float x = playerX + ARC_RADIUS * (float) Math.cos(Math.toRadians(angle));
                    float y = playerY + ARC_RADIUS * (float) Math.sin(Math.toRadians(angle));
                    spawnAndConfigureUnit(player, world, x, y, unitType, 10_000); 
                }, i * 0.1f);
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-cash", player.locale()));
        }
    }

    private static void spawnDisruptUnits(Player player, World world, float playerX, float playerY) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getCash() >= DISRUPT_unitCost) {
            playerData.subtractCash(DISRUPT_unitCost);

            for (int i = 0; i < ARC_UNIT_COUNT; i++) {
                int finalI = i;
                Timer.schedule(() -> {
                    float angle = finalI * (ARC_ANGLE / ARC_UNIT_COUNT) - ARC_ANGLE / 2;
                    float x = playerX + ARC_RADIUS * (float) Math.cos(Math.toRadians(angle));
                    float y = playerY + ARC_RADIUS * (float) Math.sin(Math.toRadians(angle));

                    for (UnitType unitType : new UnitType[]{UnitTypes.zenith, UnitTypes.quell, UnitTypes.avert, UnitTypes.flare}) {
                        spawnAndConfigureUnit(player, world, x, y, unitType, 10_000); 
                    }
                }, i * 0.1f);
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-cash", player.locale()));
        }
    }

    private static void spawnAndConfigureUnit(Player player, World world, float x, float y, UnitType unitType, long lifetime) {
        Tile tile = world.tileWorld(x * tileSize, y * tileSize);
        if (tile != null) {
            Unit unit = unitType.spawn(x * tileSize, y * tileSize);
            if (unit != null && unit.isValid()) {
                unit.type.allowedInPayloads = false;
                unit.type.playerControllable = false;
                unit.type.autoFindTarget = true;
                if (unitType == UnitTypes.corvus || unitType == UnitTypes.collaris) {
                    unit.type.groundLayer = Layer.flyingUnit;
                    unit.type.weapons.get(0).reload = 10f;
                    unit.type.weapons.get(0).cooldownTime = 10f;
                }
                Timer.schedule(() -> {
                    if (unit.isValid()) {
                        unit.kill();
                    }
                }, lifetime / 1000f); 
            } else {
                player.sendMessage(Bundle.get("spawn.unit.failed", player.locale()));
                Players.getPlayer(player).addCash(unitCost);
            }
        } else {
            player.sendMessage(Bundle.get("spawn.unit.failed", player.locale()));
            Players.getPlayer(player).addCash(unitCost);
        }
    }
}
