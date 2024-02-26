package tower.commands;

import static mindustry.content.UnitTypes.flare;

import java.util.Map;

import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.Tile;
import tower.Domain.UnitsTable;
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
                reload =10f;
                recoil = 0f;
                alwaysContinuous = true;
                cooldownTime = 10f;
                autoTarget=true;
                shootStatusDuration = 60f * 2f;
                shootStatus = StatusEffects.unmoving;
                shoot.firstShotDelay = Fx.greenLaserCharge.lifetime;
                parentizeEffects = true;
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
        float radius =   80f;
        int numberOfUnits =   6; // Number of units to spawn
        float angleStep =   360f / numberOfUnits; // Calculate the angle step for even spacing
    
        for (int i =   0; i < numberOfUnits; i++) {
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
                    UnitTypes.corvus.weapons.get(1).bullet.fragBullet = flare.weapons.get(0).bullet;
                    UnitTypes.corvus.weapons.get(1).bullet.lightningColor.argb8888(132);

                    UnitTypes.corvus.weapons.get(1).bullet.despawnHit=true;
                    UnitTypes.corvus.weapons.get(1).bullet.damage = 460f;
                    UnitTypes.corvus.weapons.get(1).bullet.lifetime = 65f;
                    UnitTypes.corvus.weapons.get(1).bullet.lightningLength = 5;
                    UnitTypes.corvus.weapons.get(1).bullet.lightningLengthRand = 15;
                    UnitTypes.corvus.weapons.get(1).bullet.lightningDamage = 50;
                    UnitTypes.corvus.weapons.get(1).bullet.lightColor = Pal.heal;
                    UnitTypes.corvus.weapons.get(1).bullet.chargeEffect = Fx.greenLaserCharge;
                    UnitTypes.corvus.weapons.get(1).bullet.healPercent = 25f;
                    UnitTypes.corvus.weapons.get(1).bullet.collidesTeam = true;
                    for (Map<String, Object> unitMap : UnitsTable.units) {
                        UnitType unitType = (UnitType) unitMap.get("unit");
                        if (unitType == playerUnit.type()) {
                            unitType.weapons.add(sex);
                            break;
                        }
                    }
                }
            }
        }
    }
    private static final Weapon sex = new Weapon("sex") {{
        shootSound = Sounds.laserblast;
        chargeSound = Sounds.lasercharge;
        soundPitchMin = 1f;
        top = false;
        mirror = false;
        shake = 14f;
        shootY = 5f;
        x = y = 0;
        reload = 10f;
        recoil = 0f;
        alwaysShooting=true;
        cooldownTime = 10f;
        autoTarget=true;
        shootStatusDuration = 60f * 2f;
        shootStatus = StatusEffects.unmoving;
        shoot.firstShotDelay = Fx.greenLaserCharge.lifetime;
        parentizeEffects = true;
}};

}