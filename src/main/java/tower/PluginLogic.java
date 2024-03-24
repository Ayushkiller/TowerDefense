package tower;

import static mindustry.Vars.*;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Timer;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.Administration;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.RepairTurret;
import useful.Bundle;

public class PluginLogic {
    public static float multiplier = 1f;
    public static boolean multiplierAdjusted = false;
    public static ObjectMap<UnitType, Seq<ItemStack>> drops;
    public static ObjectMap<Tile, ForceProjector> forceProjectorTiles = new ObjectMap<>();
    public static ObjectMap<Tile, RepairTurret> repairPointTiles = new ObjectMap<>();
    public static ObjectMap<Tile, Float> repairPointCash = new ObjectMap<>();

    public static void init() {

        netServer.admins.addActionFilter(action -> {
            if (action.tile == null)
                return true;
            if (action.type == Administration.ActionType.placeBlock) {
                if (action.block instanceof ShockMine
                        || action.block instanceof Conduit || action.block instanceof CoreBlock) {
                    Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                    return false;
                }
            }
            if ((action.type == Administration.ActionType.dropPayload
                    || action.type == Administration.ActionType.pickupBlock)) {
                Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                return false;
            }
            return true;
        });


        Timer.schedule(() -> state.rules.waveTeam.data().units.each(unit -> {
            var core = unit.closestEnemyCore();
            if (core == null || unit.dst(core) > 80f || core.health <= 0)
                return; // Check if core is null, out of range, or already dead
            float damage = (unit.health + unit.shield) / Mathf.sqrt(multiplier);
            // Ensure damage does not exceed the core's health
            damage = Math.min(damage, core.health);
            core.damage(Team.crux, damage);
            Call.effect(Fx.healWaveMend, unit.x, unit.y, 80f, Color.crimson);
            core.damage(1, true);
            unit.kill();
            if (core.block.health <= 0) {
                core.block.health = 1;
            }
        }), 0f, 0.1f);

        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
        });

        Events.on(EventType.UnitSpawnEvent.class, event -> {

            if (event.unit.team == state.rules.waveTeam) {
                event.unit.type.drag = 0.1f;
            }
        });
        Events.run(EventType.Trigger.update, () -> {
            checkUnitsWithinRadius();
        });
    }

    public static void checkUnitsWithinRadius() {

        Groups.unit.each(unit -> {
            // Find the nearest player to the unit
            Player[] nearestPlayer = new Player[1];
            float[] nearestDistance = new float[1];
            nearestDistance[0] = Float.MAX_VALUE;
            Groups.player.each(player -> {
                float distance = unit.dst(player.x, player.y);
                if (distance < nearestDistance[0]) {
                    nearestDistance[0] = distance;
                    nearestPlayer[0] = player;
                }
            });

            // Check if the unit is an enemy and within the specified distance
            if (nearestPlayer[0] != null && nearestDistance[0] <= 105f && unit.team == state.rules.waveTeam) {
                // Calculate the direction from the unit to the nearest player
                Vec2 knockbackDirection = Tmp.v1.set(nearestPlayer[0].x, nearestPlayer[0].y).sub(unit.x, unit.y).nor();

                // Define the knockback strength
                float knockbackStrength = 0.5f;

                // Apply the knockback force to the unit's velocity
                unit.vel.add(knockbackDirection.x * knockbackStrength, knockbackDirection.y * knockbackStrength);

                // Optionally, limit the maximum velocity to prevent the unit from moving too
                // fast
                unit.vel.limit(unit.type.speed);
                Call.effect(Fx.healWaveMend,unit.x,unit.y,40f,Color.green);
            }
        });

    }

}
