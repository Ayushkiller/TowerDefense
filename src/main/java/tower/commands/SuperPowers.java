package tower.commands;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import arc.util.Timer;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.Menus;
import mindustry.ui.Menus.MenuListener;
import mindustry.world.Tile;
import tower.Players;
import tower.Domain.PlayerData;

public class SuperPowers {
    private static final float TILE_SIZE = 1.0f;
    private static final int UNIT_COST = 100;
    private static final int UNIT_LIFE = 50000;
    private static final int ARC_COST = 100;
    private static final int DISRUPT_UNIT_COST = 40;
    private static final float ARC_RADIUS = 140f;
    private static final float ARC_ANGLE = 180f;
    private static final int ARC_UNIT_COUNT = 20;
    private static final int ALPHA_COST = 50;
    private static final int ALPHA_BULLET_COUNT = 12;
    private static final long ALPHA_COOLDOWN = 5000;

    public static final ConcurrentMap<Player, Long> alphaAbilityLastUseTime = new ConcurrentHashMap<>();
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
                        case 0 -> spawnUnits(player, world, playerX, playerY, UnitTypes.corvus, 6, UNIT_COST);
                        case 1 -> spawnUnits(player, world, playerX, playerY, UnitTypes.collaris, 6, UNIT_COST);
                        case 2 -> spawnArcOfUnits(player, world, playerX, playerY, UnitTypes.disrupt, ARC_UNIT_COUNT, ARC_COST);
                        case 3 -> spawnDisruptUnits(player, world, playerX, playerY);
                        case 4 -> useAlphaAbility(player);
                    }
                } else {
                    player.sendMessage("You don't have a unit available to use abilities.");
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
            player.sendMessage("You don't have a unit available to use abilities.");
        }
    }

    private static void spawnUnits(Player player, World world, float playerX, float playerY, UnitType unitType, int unitCount, int cost) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getCash() >= cost) {
            playerData.subtractCash(cost, player);
            float angleStep = 360f / unitCount;
            float radius = 100f;

            for (int i = 0; i < unitCount; i++) {
                float angle = i * angleStep;
                float x = playerX + radius * (float) Math.cos(Math.toRadians(angle));
                float y = playerY + radius * (float) Math.sin(Math.toRadians(angle));
                Timer.schedule(() -> spawnAndConfigureUnit(player, world, x, y, unitType, UNIT_LIFE), i * 0.1f);
            }
        } else {
            player.sendMessage("Not enough cash to spawn units.");
        }
    }

    private static void spawnArcOfUnits(Player player, World world, float playerX, float playerY, UnitType unitType, int unitCount, int cost) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getCash() >= cost) {
            playerData.subtractCash(cost, player);
            float angleStep = ARC_ANGLE / unitCount;

            for (int i = 0; i < unitCount; i++) {
                float angle = i * angleStep - ARC_ANGLE / 2;
                float x = playerX + ARC_RADIUS * (float) Math.cos(Math.toRadians(angle));
                float y = playerY + ARC_RADIUS * (float) Math.sin(Math.toRadians(angle));
                Timer.schedule(() -> spawnAndConfigureUnit(player, world, x, y, unitType, 10000), i * 0.1f);
            }
        } else {
            player.sendMessage("Not enough cash to spawn arc of units.");
        }
    }

    private static void spawnDisruptUnits(Player player, World world, float playerX, float playerY) {
        PlayerData playerData = Players.getPlayer(player);
        if (playerData.getCash() >= DISRUPT_UNIT_COST) {
            playerData.subtractCash(DISRUPT_UNIT_COST, player);

            for (int i = 0; i < ARC_UNIT_COUNT; i++) {
                float angle = i * (ARC_ANGLE / ARC_UNIT_COUNT) - ARC_ANGLE / 2;
                float x = playerX + ARC_RADIUS * (float) Math.cos(Math.toRadians(angle));
                float y = playerY + ARC_RADIUS * (float) Math.sin(Math.toRadians(angle));

                for (UnitType unitType : new UnitType[]{UnitTypes.zenith, UnitTypes.quell, UnitTypes.avert, UnitTypes.flare}) {
                    Timer.schedule(() -> spawnAndConfigureUnit(player, world, x, y, unitType, 10000), i * 0.1f);
                }
            }
        } else {
            player.sendMessage("Not enough cash to spawn disrupt units.");
        }
    }

    private static void useAlphaAbility(Player player) {
        PlayerData playerData = Players.getPlayer(player);
        long currentTime = System.currentTimeMillis();
        long lastAlphaAbilityTime = alphaAbilityLastUseTime.getOrDefault(player, 0L);

        if (currentTime - lastAlphaAbilityTime >= ALPHA_COOLDOWN) {
            if (playerData.getCash() >= ALPHA_COST) {
                playerData.subtractCash(ALPHA_COST, player);
                Random random = new Random();

                for (int i = 0; i < ALPHA_BULLET_COUNT; i++) {
                    float angle = random.nextFloat() * 360;
                    sendEMPBullet(player, player.unit().x, player.unit().y, angle);
                }

                alphaAbilityLastUseTime.put(player, currentTime);
            } else {
                player.sendMessage("Not enough cash to use Alpha ability.");
            }
        } else {
            long remainingCooldown = ALPHA_COOLDOWN - (currentTime - lastAlphaAbilityTime);
            player.sendMessage("Alpha ability is on cooldown. Please wait " + (remainingCooldown / 1000) + " seconds.");
        }
    }

    private static void sendEMPBullet(Player player, float startX, float startY, float angle) {
        Unit playerUnit = player.unit();
        if (playerUnit != null) {
            float damage = 1.0f;
            float velocityScl = 1.0f;
            float lifetimeScl = 1.0f;
            Call.createBullet(getBullet(UnitTypes.navanax, "emp-cannon-mount"), player.team(), startX, startY, angle, damage, velocityScl, lifetimeScl);
        }
    }

    private static void spawnAndConfigureUnit(Player player, World world, float x, float y, UnitType unitType, long lifetime) {
        Tile tile = world.tileWorld(x * TILE_SIZE, y * TILE_SIZE);
        if (tile != null) {
            Unit unit = unitType.spawn(x * TILE_SIZE, y * TILE_SIZE);
            if (unit != null && unit.isValid()) {
                unit.type = unitType;
                unit.team = player.team();
                Timer.schedule(unit::kill, lifetime / 1000f);
            }
        }
    }

 private static BulletType getBullet(UnitType unitType, String weaponName) {
    for (Weapon weapon : unitType.weapons) {
        if (Objects.equals(weapon.name, weaponName)) {
            return weapon.bullet;
        }
    }
    return null;
}
}
