package tower.commands;

import arc.graphics.Color;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.Weapon;
import mindustry.world.Tile;

public class SuperPowers {
    private static final float tilesize =  1.0f; // Adjust the value as needed
        private static final Weapon customCorvusWeapon = new Weapon("customCorvusWeapon") {{
                shootSound = Sounds.laserblast;
                chargeSound = Sounds.lasercharge;
                soundPitchMin = 1f;
                top = false;
                mirror = false;
                shake = 14f;
                shootY = 5f;
                x = y = 0;
                reload = 35f;
                recoil = 0f;

                cooldownTime = 35f;

                shootStatusDuration = 60f * 2f;
                shootStatus = StatusEffects.unmoving;
                shoot.firstShotDelay = Fx.greenLaserCharge.lifetime;
                parentizeEffects = true;

                bullet = new LaserBulletType(){{
                    length = 460f;
                    damage = 560f;
                    width = 75f;

                    lifetime = 65f;

                    lightningSpacing = 35f;
                    lightningLength = 5;
                    lightningDelay = 1.1f;
                    lightningLengthRand = 15;
                    lightningDamage = 50;
                    lightningAngleRand = 40f;
                    largeHit = true;
                    lightColor = lightningColor = Pal.heal;

                    chargeEffect = Fx.greenLaserCharge;

                    healPercent = 25f;
                    collidesTeam = true;

                    sideAngle = 15f;
                    sideWidth = 0f;
                    sideLength = 0f;
                    colors = new Color[]{Pal.heal.cpy().a(0.4f), Pal.heal, Color.white};
                }};

    }};
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
                Unit unit = UnitTypes.corvus.spawn(worldX, worldY);
                if (unit != null) {
               UnitTypes.corvus.weapons.add(customCorvusWeapon);
                    
                }
            }
        }
    }
}