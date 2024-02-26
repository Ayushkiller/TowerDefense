package tower.commands;

import arc.graphics.Color;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.core.World;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.PowerAmmoType;
import mindustry.world.Tile;

public class SuperPowers {
    private static final float tilesize =  1.0f; // Adjust the value as needed
    private static UnitType customCorvus;

    public static void onContentInit() {
        if (customCorvus == null) {
            customCorvus = new UnitType("customCorvus") {{
                hitSize =  29f;
                health =  18000f;
                armor =  9f;
                stepShake =  1.5f;
                rotateSpeed =  1.5f;
                drownTimeMultiplier =  6f;

                hovering = true;
                shadowElevation =  0.2f;
                ammoType = new PowerAmmoType(40000);
                groundLayer = Layer.flyingUnit;

                speed =  0.3f;

                drawShields = false;

                weapons.add(new Weapon("corvus-weapon") {{
                    shootSound = Sounds.laserblast;
                    chargeSound = Sounds.lasercharge;
                    soundPitchMin =  1f;
                    top = false;
                    mirror = false;
                    shake =  14f;
                    shootY =  5f;
                    x = y =  0;
                    reload =  100f;
                    recoil =  0f;
                    alwaysContinuous = true;
                    cooldownTime =  100f;

                    shootStatusDuration =  60f *  2f;
                    shootStatus = StatusEffects.unmoving;
                    shoot.firstShotDelay = Fx.greenLaserCharge.lifetime;
                    parentizeEffects = true;

                    bullet = new LaserBulletType() {{
                        length =  460f;
                        damage =  560f;
                        width =  75f;

                        lifetime =  65f;

                        lightningSpacing =  35f;
                        lightningLength =  5;
                        lightningDelay =  1.1f;
                        lightningLengthRand =  15;
                        lightningDamage =  50;
                        lightningAngleRand =  40f;
                        largeHit = true;
                        lightColor = lightningColor = Pal.heal;

                        chargeEffect = Fx.greenLaserCharge;

                        healPercent =  25f;
                        collidesTeam = true;

                        sideAngle =  15f;
                        sideWidth =  0f;
                        sideLength =  0f;
                        colors = new Color[]{Pal.heal.cpy().a(0.4f), Pal.heal, Color.white};
                    }};
                }});
            }};
        }
    }

    public static void execute(Player player) {

        player.sendMessage("Super Powers menu option selected.");

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
                Unit unit = customCorvus.spawn(worldX, worldY);
                if (unit != null) {
                    // Additional logic for the spawned unit
                }
            }
        }
    }
}