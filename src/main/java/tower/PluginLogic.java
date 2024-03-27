package tower;

import static mindustry.Vars.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import arc.Events;
import arc.graphics.Color;
import arc.math.geom.Vec2;
import arc.util.Timer;
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
    private static List<Teams.TeamData> activeTeamsList = new ArrayList<>();
    private static HashMap<Unit, Vec2> unitPreviousVelocities = new HashMap<>();
    private static HashMap<Unit, Boolean> unitHasMoved = new HashMap<>();

    public static void init() {
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
            Teams teams = Vars.state.teams;
            int activeTeamsWithCores = 0;
            // Check for active teams and count those with cores
            for (Team team : Team.all) {
                if (teams.isActive(team) && !teams.cores(team).isEmpty()) {
                    activeTeamsWithCores++;
                }
            }
            // If no active team has cores or if the number of active teams with cores is
            // less than the total number of active teams,
            // damage the crux team's core
            if (activeTeamsWithCores < activeTeamsList.size()) {
                for (CoreBuild corea : teams.cores(Team.crux)) {
                    corea.kill();
                }
            }
            if (activeTeamsWithCores < activeTeamsList.size()) {
                for (CoreBuild corea : teams.cores(Team.crux)) {
                    corea.kill();
                }
            }
            if (activeTeamsWithCores < activeTeamsList.size()) {
                for (CoreBuild corea : teams.cores(Team.crux)) {
                    corea.kill();
                }
            }
        }), 0f, 0.1f);

        Timer.schedule(() -> state.rules.waveTeam.data().units.each(unit -> {

            var core3 = unit.closestEnemyCore();

            if (core3 == null || core3.health <= 0)
                return;
            Call.effect(Fx.healWaveMend, core3.x, core3.y, 200f, Color.crimson);

        }), 0f, 1f);
        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
            activeTeamsList.clear();
            spawnedTiles.clear();
            unitPreviousVelocities.clear();
            unitHasMoved.clear();
        });
        Events.on(EventType.WorldLoadEndEvent.class, event -> {
            Teams teams = Vars.state.teams;

            for (Teams.TeamData teamData : teams.active) {
                if (!activeTeamsList.contains(teamData)) {
                    activeTeamsList.add(teamData);
                }
            }
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
                        // Wrap the unit in a final wrapper class
                        final UnitWrapper unitWrapper = new UnitWrapper();
                        unitWrapper.unit = unitType.spawn(state.rules.waveTeam, randomTile.getX(), randomTile.getY());
                        event.unit.type.drag = 0.002f;
                        unitWrapper.unit.type.aiController = Newai::new;
                        unitWrapper.unit.apply(StatusEffects.disarmed, Float.POSITIVE_INFINITY);
                        unitWrapper.unit.apply(StatusEffects.invincible, Float.POSITIVE_INFINITY);

                        // Schedule a check to see if the unit is still alive after 0.5 seconds
                        Timer.schedule(() -> {
                            if (!unitWrapper.unit.dead()) {
                                // Find the team with the least number of cores
                                Team teamWithLeastCores = null;
                                int minCores = Integer.MAX_VALUE;
                                for (Team team : Team.all) {
                                    int cores = Vars.state.teams.cores(team).size;
                                    if (cores < minCores) {
                                        minCores = cores;
                                        teamWithLeastCores = team;
                                    }
                                }

                                // Kill all cores of the team with the least number of cores
                                if (teamWithLeastCores != null) {
                                    for (CoreBuild core : Vars.state.teams.cores(teamWithLeastCores)) {
                                        core.kill();
                                    }
                                }
                            }
                        }, 0.1f);
                    }
                }
            }
        });

        Events.on(EventType.UnitSpawnEvent.class, event -> {

            if (event.unit.team == state.rules.waveTeam) {
                event.unit.type.drag = 0.002f;
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
            Player nearestPlayer = findNearestPlayer(unit);
            if (nearestPlayer != null) {
                float distance = unit.dst(nearestPlayer.x, nearestPlayer.y);
                if (distance <= 120f && unit.team == state.rules.waveTeam) {
                    Vec2 directionToPlayer = new Vec2().set(nearestPlayer.x - unit.x, nearestPlayer.y - unit.y).nor();
                    Vec2 playerVelocity = nearestPlayer.unit().vel;
                    float velocityTowardsUnit = directionToPlayer.dot(playerVelocity);
                    float knockbackStrength = calculateKnockbackStrength(distance, velocityTowardsUnit);
                    directionToPlayer.scl(-knockbackStrength);
                    unit.vel.add(directionToPlayer);
                    unit.vel.limit(unit.type.speed * 5.6f);
                    Call.effect(Fx.pointShockwave, unit.x, unit.y, 120f, Color.white);
                }
            }
            // Check if the unit's velocity has changed
            Vec2 currentVelocity = unit.vel;
            Vec2 lastKnownVelocity = unitPreviousVelocities.get(unit);
            if (lastKnownVelocity == null || !currentVelocity.equals(lastKnownVelocity)) {
                // Update the previous velocity for the unit
                unitPreviousVelocities.put(unit, currentVelocity.cpy());
                // Mark the unit as having moved
                unitHasMoved.put(unit, true);
            }

            // Check if the unit has stopped
            if (currentVelocity.len() == 0 && lastKnownVelocity != null) {
                // If the unit has moved, do not stop it
                if (!unitHasMoved.getOrDefault(unit, false)) {
                    // Calculate the opposite direction
                    Vec2 oppositeDirection = lastKnownVelocity.cpy().nor().scl(-1);
                    // Apply the opposite direction speed
                    unit.vel.set(oppositeDirection).scl(lastKnownVelocity.len());
                } else {

                    // If the unit has moved, give it a velocity in a random direction with at most
                    // twice its speed
                    Vec2 randomDirection = new Vec2(new Random().nextFloat() * 2 - 1, new Random().nextFloat() * 2 - 1).nor();
                    unit.vel.set(randomDirection).scl(unit.type.speed * 4f);
                }
            }
        });
    }

    private static float calculateKnockbackStrength(float distance, float velocityTowardsUnit) {
        // Adjust the multiplier based on the distance
        float multiplier = 1.0f;
        if (distance > 50f && distance <= 100f) {
            multiplier = 1.5f;
        } else if (distance > 100f) {
            multiplier = 2.0f;
        }
        // Apply the multiplier to the velocity towards unit
        return Math.abs(velocityTowardsUnit) * multiplier;
    }

    private static Player findNearestPlayer(Unit unit) {
        Player nearestPlayer = null;
        float nearestDistance = Float.MAX_VALUE;
        for (Player player : Groups.player) {
            float distance = unit.dst(player.x, player.y);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }
        return nearestPlayer;
    }

    public static List<Tile> getSpawnedTiles() {
        return spawnedTiles;
    }

    // Ah dont knwo what else could have done
    static class UnitWrapper {
        Unit unit;
    }

}
