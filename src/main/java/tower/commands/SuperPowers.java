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

    public static void execute(Player player) {
        // Implement the logic for the Super Powers menu option here
        player.sendMessage("Super Powers menu option selected.");

        // Spawn  6 units around the player within a  40f radius
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
            // Calculate the angle for the current unit
            float angle = i * angleStep;
            // Convert the angle to radians
            double radians = Math.toRadians(angle);
            // Calculate the spawn position
            float x = playerX + radius * (float) Math.cos(radians);
            float y = playerY + radius * (float) Math.sin(radians);

            // Convert the float coordinates to integers
            int intX = (int) x;
            int intY = (int) y;
            // Convert the integer coordinates to world coordinates
            float worldX = intX * tilesize;
            float worldY = intY * tilesize;

            // Find the tile at the world position
            Tile tile = world.tileWorld(worldX, worldY);
            if (tile != null) {
                // Spawn a Corvus unit at the tile
                Unit unit = SuperPowers.collaris.spawn(worldX, worldY); // Adjusted to use UnitType.spawn
                if (unit != null) {
             
                }
            }
        }
    }
        private static final UnitType collaris = new UnitType("collaris") {
        {
            hitSize = 29f;
            health = 18000f;
            armor = 9f;
            stepShake = 1.5f;
            rotateSpeed = 1.5f;
            drownTimeMultiplier = 6f;

            legCount = 4;
            legLength = 14f;
            legBaseOffset = 11f;
            legMoveSpace = 1.5f;
            legForwardScl = 0.58f;
            hovering = true;
            shadowElevation = 5f;
            ammoType = new PowerAmmoType(4000);
            groundLayer = Layer.flyingUnit;

            speed = 0.3f;

            drawShields = false;

            weapons.add(new Weapon("corvus-weapon"){{
                shootSound = Sounds.laserblast;
                chargeSound = Sounds.lasercharge;
                soundPitchMin = 1f;
                top = false;
                mirror = false;
                shake = 14f;
                shootY = 5f;
                x = y = 0;
                reload = 350f;
                recoil = 0f;
                alwaysShooting=true;
                cooldownTime = 350f;

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
            }});
            };
            
        };
}
