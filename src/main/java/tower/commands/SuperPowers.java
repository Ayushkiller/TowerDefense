package tower.commands;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import arc.Events;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.entities.bullet.BulletType;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import mindustry.ui.Menus.MenuListener;
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
    private static final int ALPHA_COST = 50;

    // Cooldown time for Alpha ability in milliseconds
    private static final long ALPHA_COOLDOWN = 5000;
    // Concurrent map to track the last use time of Alpha ability for each player
    public static final ConcurrentMap<Player, Long> alphaAbilityLastUseTime = new ConcurrentHashMap<>();

    // Concurrent map to track registered players for tap event
    public static final ConcurrentMap<Player, Boolean> registeredForTapEvent = new ConcurrentHashMap<>();

    // Menu ID for the ability selection menu
    private static final int abilitySelectionMenuId;

    static {
        abilitySelectionMenuId = Menus.registerMenu(new MenuListener() {
            @Override
            public void get(Player player, int option) {
                Unit playerUnit = player.unit();
                if (playerUnit != null) {
                    float playerX = playerUnit.x;
                    float playerY = playerUnit.y;
                    World world = Vars.world;

                    switch (option) {
                        case 0 -> spawnUnits(player, world, playerX, playerY, UnitTypes.corvus, 6, unitCost);
                        case 1 -> spawnUnits(player, world, playerX, playerY, UnitTypes.collaris, 6, unitCost);
                        case 2 -> spawnArcOfUnits(player, world, playerX, playerY, UnitTypes.disrupt, ARC_UNIT_COUNT, arcCost);
                        case 3 -> spawnDisruptUnits(player, world, playerX, playerY);
                        case 4 -> useAlphaAbility(player);
                    }
                } else {
                    player.sendMessage(Bundle.get("player.unit.not-available", player.locale()));
                }
            }
        });
    }

    public static void execute(Player player) {
        Unit playerUnit = player.unit();
        if (playerUnit != null) {
            Call.menu(player.con, abilitySelectionMenuId, "[lime]Choose an ability to use:", "", new String[][]{
                    {"Corvus"},
                    {"Collaris"},
                    {"Squad"},
                    {"Magic"},
                    {"Alpha"}
            });
        } else {
            player.sendMessage(Bundle.get("player.unit.not-available", player.locale()));
        }
    }

    private static void spawnUnits(Player player, World world, float playerX, float playerY, UnitType unitType,
                                   int unitCount, int cost) {
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

    private static void spawnArcOfUnits(Player player, World world, float playerX, float playerY, UnitType unitType,
                                        int unitCount, int cost) {
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

                    for (UnitType unitType : new UnitType[]{UnitTypes.zenith, UnitTypes.quell, UnitTypes.avert,
                            UnitTypes.flare}) {
                        spawnAndConfigureUnit(player, world, x, y, unitType, 10_000);
                    }
                }, i * 0.1f);
            }
        } else {
            player.sendMessage(Bundle.get("spawn.arc-of-units.not-enough-cash", player.locale()));
        }
    }

    private static void useAlphaAbility(Player player) {
        PlayerData playerData = Players.getPlayer(player);
        long currentTime = System.currentTimeMillis();
        long lastAlphaAbilityTime = alphaAbilityLastUseTime.getOrDefault(player, 0L);

        if (currentTime - lastAlphaAbilityTime >= ALPHA_COOLDOWN) {
            if (playerData.getCash() >= ALPHA_COST) {
                playerData.subtractCash(ALPHA_COST);
                player.sendMessage(Bundle.get("alpha.ability.tap-target", player.locale()));

                // Check if the tap event listener is already registered for this player
                if (!registeredForTapEvent.containsKey(player)) {
                    Events.on(EventType.TapEvent.class, event -> {
                        if (event.player == player) {
                            sendEMPBullet(player, player.x, player.y, event.tile.worldx(), event.tile.worldy());
                        }
                    });
                    registeredForTapEvent.put(player, true);
                }

                alphaAbilityLastUseTime.put(player, currentTime);
            } else {
                player.sendMessage(Bundle.get("alpha.ability.not-enough-cash", player.locale()));
            }
        } else {
            long remainingCooldown = ALPHA_COOLDOWN - (currentTime - lastAlphaAbilityTime);
            player.sendMessage("Alpha ability is on cooldown. Please wait " + (remainingCooldown / 1000) + " seconds.");
        }
    }

    private static void sendEMPBullet(Player player, float startX, float startY, float endX, float endY) {
        Unit playerUnit = player.unit();
        if (playerUnit != null) {
            float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX)); // Calculate angle in degrees
            playerUnit.rotation(angle); // Rotate the player's unit to face the target
            float damage = 1.0f; // Damage of the bullet
            float velocityScl = 1.0f; // Velocity scale
            float lifetimeScl = 1.0f; // Lifetime scale
            Call.createBullet(getBullet(UnitTypes.navanax, "emp-cannon-mount"), player.team(), startX, startY, angle,
                    damage, velocityScl, lifetimeScl);
        }
    }

    private static void spawnAndConfigureUnit(Player player, World world, float x, float y, UnitType unitType,
                                              long lifetime) {
        Tile tile = world.tileWorld(x * tileSize, y * tileSize);
        if (tile != null) {
            Unit unit = unitType.spawn(x * tileSize, y * tileSize);
            if (unit != null && unit.isValid()) {
                unit.type = unitType;
                unit.team = player.team();
                Timer.schedule(unit::kill, lifetime / 1000f);
            }
        }
    }

    private static BulletType getBullet(UnitType unitType, String weaponName) {
        for (var weapon : unitType.weapons) {
            if (Objects.equals(weapon.name, weaponName)) {
                return weapon.bullet;
            }
        }
        return null;
    }
}
