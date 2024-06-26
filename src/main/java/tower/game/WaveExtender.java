package tower.game;

import static mindustry.Vars.*;

import arc.Events;
import arc.func.Intc2;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.ai.WaveSpawner;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.core.World;
import mindustry.entities.Damage;
import mindustry.game.EventType.UnitSpawnEvent;
import mindustry.game.EventType.WaveEvent;
import mindustry.game.EventType.WorldLoadEvent;
import mindustry.game.SpawnGroup;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Unit;
import mindustry.world.Tile;

public class WaveExtender extends WaveSpawner {
    private static final float margin = 0f, coreMargin = tilesize * 2f, maxSteps = 30;

    private int tmpCount;
    private Seq<Tile> spawns = new Seq<>();
    private boolean spawning = false;
    private boolean any = false;
    private Tile firstSpawn = null;
   
    public void skipWave(){
        runWave();
    }
    public void runWave() {
        WaveExtender waveExtender = new WaveExtender();
        waveExtender.spawnEnemies();
        state.wave++;
        state.wavetime = state.rules.waveSpacing;

        Events.fire(new WaveEvent());
    }

    public WaveExtender() {
        Events.on(WorldLoadEvent.class, e -> reset());
    }

    @Override
    @Nullable
    public Tile getFirstSpawn() {
        firstSpawn = null;
        eachSpawn((cx, cy) -> {
            firstSpawn = world.tile(cx, cy);
        });
        return firstSpawn;
    }

    @Override
    public int countSpawns() {
        return spawns.size;
    }

    @Override
    public Seq<Tile> getSpawns() {
        return spawns;
    }

    @Override
    /** @return true if the player is near a spawn point. */
    public boolean playerNear() {
        return state.hasSpawns() && !player.dead()
                && spawns.contains(
                        g -> Mathf.dst(g.x * tilesize, g.y * tilesize, player.x, player.y) < state.rules.dropZoneRadius
                                && player.team() != state.rules.waveTeam);
    }

    @Override
    public void spawnEnemies() {
        spawning = true;

        eachSpawn(-1, (spawnX, spawnY, doShockwave) -> {
            if (doShockwave) {
                doShockwave(spawnX, spawnY);
            }
        });

        for (SpawnGroup group : state.rules.spawns) {
            if (group.type == null)
                continue;

            int spawned = group.getSpawned(state.wave - 1);
            float spread = group.type.flying ? margin / 1.5f : tilesize * 2;

            eachSpawn(group.spawn, (spawnX, spawnY, doShockwave) -> {
                for (int i = 0; i < spawned; i++) {
                    Unit unit = group.createUnit(state.rules.waveTeam, state.wave - 1);
                    Tmp.v1.rnd(spread);
                    unit.set(spawnX + Tmp.v1.x, spawnY + Tmp.v1.y);
                    spawnEffect(unit);
                }
            });
        }

        Time.run(121f, () -> spawning = false);
    }

    @Override
    public void doShockwave(float x, float y) {
        Fx.spawnShockwave.at(x, y, state.rules.dropZoneRadius);
        Damage.damage(state.rules.waveTeam, x, y, state.rules.dropZoneRadius, 99999999f, true);
    }

    public void eachSpawn(Intc2 cons) {
        eachSpawn(-1, (x, y, shock) -> cons.get(World.toTile(x), World.toTile(y)));
    }

    private void eachSpawn(int filterPos, SpawnConsumer cons) {
        if (state.hasSpawns()) {
            for (Tile spawn : spawns) {
                if (filterPos != -1 && filterPos != spawn.pos())
                    continue;

                cons.accept(spawn.worldx(), spawn.worldy(), true);
            }
        }

        if (state.rules.attackMode && state.teams.isActive(state.rules.waveTeam)
                && !state.teams.playerCores().isEmpty()) {
            Building firstCore = state.teams.playerCores().first();
            for (Building core : state.rules.waveTeam.cores()) {
                if (filterPos != -1 && filterPos != core.pos())
                    continue;

                Tmp.v1.set(firstCore).sub(core).limit(coreMargin + core.block.size * tilesize / 2f * Mathf.sqrt2);

                boolean valid = false;
                int steps = 0;

                while (steps++ < maxSteps) {
                    int tx = World.toTile(core.x + Tmp.v1.x), ty = World.toTile(core.y + Tmp.v1.y);
                    any = false;
                    Geometry.circle(tx, ty, world.width(), world.height(), 3, (x, y) -> {
                        if (world.solid(x, y)) {
                            any = true;
                        }
                    });

                    if (!any) {
                        valid = true;
                        break;
                    } else {
                        Tmp.v1.setLength(Tmp.v1.len() + tilesize * 1.1f);
                    }
                }

                if (valid) {
                    cons.accept(core.x + Tmp.v1.x, core.y + Tmp.v1.y, false);
                }
            }
        }
    }

    @Override
    public int countGroundSpawns() {
        tmpCount = 0;
        eachSpawn((x, y) -> tmpCount++);
        return tmpCount;
    }

    @Override
    public int countFlyerSpawns() {
        return countGroundSpawns(); // Use unified method
    }

    @Override
    public boolean isSpawning() {
        return spawning && !net.client();
    }

    @Override
    public void reset() {
        spawning = false;
        spawns.clear();

        for (Tile tile : world.tiles) {
            if (tile.overlay() == Blocks.spawn) {
                spawns.add(tile);
            }
        }
    }

    @Override
    /**
     * Applies the standard wave spawn effects to a unit - invincibility, unmoving.
     */
    public void spawnEffect(Unit unit) {
        spawnEffect(unit, unit.angleTo(world.width() / 2f * tilesize, world.height() / 2f * tilesize));
    }

    @Override
    /**
     * Applies the standard wave spawn effects to a unit - invincibility, unmoving.
     */
    public void spawnEffect(Unit unit, float rotation) {
        unit.rotation = rotation;
        unit.apply(StatusEffects.unmoving, 30f);
        unit.apply(StatusEffects.invincible, 60f);
        unit.add();
        unit.unloaded();

        Events.fire(new UnitSpawnEvent(unit));
        Call.spawnEffect(unit.x, unit.y, unit.rotation, unit.type);
    }

    private interface SpawnConsumer {
        void accept(float x, float y, boolean shockwave);
    }
}
