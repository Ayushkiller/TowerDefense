package tower;

import static mindustry.Vars.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import arc.Events;
import arc.graphics.Color;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Timer;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.game.Teams;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import tower.game.Newai;

public class PluginLogic {
    private static List<Tile> spawnedTiles = new ArrayList<>();

    public static void init() {
        Timer.schedule(() -> {
            Teams teams = Vars.state.teams;
            int activeTeamsCount = 0;
            boolean anyTeamHasCores = false;

            // Check for active teams
            for (Team team : Team.all) {
                if (teams.isActive(team)) {
                    activeTeamsCount++;
                    if (teams.cores(team).size > 0) {
                        anyTeamHasCores = true;
                    }
                }
            }

            // Damage core of waveTeam if the number of active teams drops
            if (activeTeamsCount < 2) { // Assuming at least 2 teams are always active
                CoreBuild waveTeamCore = teams.cores(state.rules.waveTeam).first();
                if (waveTeamCore != null) {
                    waveTeamCore.damage(10000);
                }
            }
            // Kill core of waveTeam if any active team doesn't have a core
            boolean allActiveTeamsHaveCores = true;
            for (Team team : Team.all) {
                if (teams.isActive(team) && teams.cores(team).size == 0) {
                    allActiveTeamsHaveCores = false;
                    break;
                }
            }

            if (!allActiveTeamsHaveCores) {
                Seq<CoreBuild> waveTeamCores = teams.cores(state.rules.waveTeam);
                if (!waveTeamCores.isEmpty()) {
                    CoreBuild waveTeamCore = waveTeamCores.first();
                    if (waveTeamCore != null) {
                        waveTeamCore.kill();
                    }
                }
            }
            // Kill core of waveTeam if no active teams have cores
            if (!anyTeamHasCores) {
                CoreBuild waveTeamCore = teams.cores(state.rules.waveTeam).first();
                if (waveTeamCore != null) {
                    waveTeamCore.kill();
                }
            }
        }, 0f, 1f);
        Timer.schedule(() -> state.rules.waveTeam.data().units.each(unit -> {
            var core = unit.closestEnemyCore();

            if (core == null || unit.dst(core) > 200f || core.health <= 0)
                return; // Check if core is null, out of range, or already dead
            float damage = (100000000);
            core.damage(Team.crux, damage);
            Call.effect(Fx.healWaveMend, unit.x, unit.y, 80f, Color.crimson);
            core.damage(1, true);
            unit.kill();
            if (core.block.health <= 0) {
                core.block.health = 1;
            }
            unit.kill();

        }), 0f, 0.1f);

        Timer.schedule(() -> state.rules.waveTeam.data().units.each(unit -> {

            var core3 = unit.closestEnemyCore();

            if (core3 == null || core3.health <= 0)
                return;
            Call.effect(Fx.healWaveMend, core3.x, core3.y, 200f, Color.crimson);

        }), 0f, 1f);
        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
            spawnedTiles.clear();
        });
        Events.on(EventType.UnitDestroyEvent.class, event -> {
            if (event.unit.team == state.rules.waveTeam) {
                // Check if there are any spawned tiles
                if (!spawnedTiles.isEmpty()) {
                    // Select a random tile from the list
                    Random random = new Random();
                    Tile randomTile = spawnedTiles.get(random.nextInt(spawnedTiles.size()));
                    if (randomTile != null) { // Ensure randomTile is not null
                        UnitType unitType = UnitTypes.oct;
                        Unit unit = unitType.spawn(state.rules.waveTeam, randomTile.getX(), randomTile.getY());
                        event.unit.type.drag = 0.01f;
                        unit.type.aiController = Newai::new;
                        unit.apply(StatusEffects.disarmed, Float.POSITIVE_INFINITY);
                        unit.apply(StatusEffects.invincible, Float.POSITIVE_INFINITY);
                    }
                }
            }
        });

        Events.on(EventType.UnitSpawnEvent.class, event -> {

            if (event.unit.team == state.rules.waveTeam) {
                event.unit.type.drag = 0.01f;
                event.unit.type.aiController = Newai::new;
                event.unit.apply(StatusEffects.invincible, Float.POSITIVE_INFINITY);
                spawnedTiles.add(event.unit.tileOn());
                event.unit.apply(StatusEffects.disarmed, Float.POSITIVE_INFINITY);
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
            if (nearestPlayer[0] != null && nearestDistance[0] <= 120f && unit.team == state.rules.waveTeam) {
                // Calculate the direction from the unit to the nearest player
                Vec2 directionToPlayer = Tmp.v1.set(nearestPlayer[0].x, nearestPlayer[0].y).sub(unit.x, unit.y).nor();
                Vec2 playerVelocity = nearestPlayer[0].unit().vel;

                // Calculate the dot product of the direction to the player and the player's
                // velocity
                float velocityTowardsUnit = directionToPlayer.dot(playerVelocity);

                // Calculate the knockback strength based on the player's velocity towards the
                // unit
                float knockbackStrength = Math.abs(velocityTowardsUnit);

                // Reverse the direction to apply the knockback away from the player
                directionToPlayer.scl(-1);

                // Apply the knockback force to the unit's velocity
                unit.vel.add(directionToPlayer.x * knockbackStrength, directionToPlayer.y * knockbackStrength);

                unit.vel.limit(unit.type.speed * 5.6f);
                Call.effect(Fx.healWaveMend, unit.x, unit.y, 120f, Color.green);
            }
        });

    }

    public static List<Tile> getSpawnedTiles() {
        return spawnedTiles;
    }

}
