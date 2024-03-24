package tower;

import static mindustry.Vars.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import arc.Events;
import arc.graphics.Color;
import arc.math.geom.Vec2;
import arc.util.Timer;
import arc.util.Tmp;
import mindustry.ai.types.FlyingFollowAI;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.core.NetServer.TeamAssigner;
import mindustry.game.EventType;
import mindustry.game.EventType.PlayerJoin;
import mindustry.game.Team;
import mindustry.game.Teams.TeamData;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.Tile;

public class PluginLogic {
    private static List<Tile> spawnedTiles = new ArrayList<>();

    public static void init() {

        Timer.schedule(() -> state.rules.waveTeam.data().units.each(unit -> {
            var core = unit.closestEnemyCore();
            if (core == null || unit.dst(core) > 80f || core.health <= 0)
                return; // Check if core is null, out of range, or already dead
            float damage = (100000000);
            core.damage(Team.crux, damage);
            Call.effect(Fx.healWaveMend, unit.x, unit.y, 80f, Color.crimson);
            core.damage(1, true);
            unit.kill();
            if (core.block.health <= 0) {
                core.block.health = 1;
            }
        }), 0f, 0.1f);
        Events.on(PlayerJoin.class, event -> {
            Team assignedTeam = teamAssigner.assign(event.player, Groups.player);
            event.player.team(assignedTeam);
        });

        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
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
                        Unit unit = unitType.spawn(randomTile.getX(), randomTile.getY());
                        unit.type.drag = 0.1f;
                        unit.type.aiController = FlyingFollowAI::new;
                        event.unit.apply(StatusEffects.disarmed,Float.POSITIVE_INFINITY);
                        event.unit.apply(StatusEffects.invincible,Float.POSITIVE_INFINITY);
                    }
                }
            }
        });

        Events.on(EventType.UnitSpawnEvent.class, event -> {

            if (event.unit.team == state.rules.waveTeam) {
                event.unit.type.drag = 0.1f;
                event.unit.type.aiController = FlyingFollowAI::new;
                event.unit.apply(StatusEffects.invincible,Float.POSITIVE_INFINITY);
                spawnedTiles.add(event.unit.tileOn());
                         event.unit.apply(StatusEffects.disarmed,Float.POSITIVE_INFINITY);
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
                Vec2 directionToPlayer = Tmp.v1.set(nearestPlayer[0].x, nearestPlayer[0].y).sub(unit.x, unit.y).nor();
                Vec2 playerVelocity = nearestPlayer[0].unit().vel;

                float angle = directionToPlayer.dot(playerVelocity);

                float knockbackStrength = 0.5f + Math.abs(angle) * 0.5f;

                // Reverse the direction to apply the knockback away from the player
                directionToPlayer.scl(-1);

                // Apply the knockback force to the unit's velocity
                unit.vel.add(directionToPlayer.x * knockbackStrength, directionToPlayer.y * knockbackStrength);

                // Optionally, limit the maximum velocity to prevent the unit from moving too
                // fast
                unit.vel.limit(unit.type.speed);
                Call.effect(Fx.healWaveMend, unit.x, unit.y, 40f, Color.green);
            }
        });

    }

    public static List<Tile> getSpawnedTiles() {
        return spawnedTiles;
    }
    private static TeamAssigner teamAssigner = (player, players) -> {
        //find team with minimum amount of players and auto-assign player to that.
        TeamData re = state.teams.getActive().min(data -> {
         if((state.rules.waveTeam == data.team && state.rules.waves) || !data.team.active() || data.team == Team.derelict) return Integer.MAX_VALUE;

         int count = 0;
         for(Player other : players){
             if(other.team() == data.team && other != player){
                 count++;
             }
         }
         return count;
     });
     // If no team is found, return the default team
     return re == null ? state.rules.defaultTeam : re.team;
 };

}
