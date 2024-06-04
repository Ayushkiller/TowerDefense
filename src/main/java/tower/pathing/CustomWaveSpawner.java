package tower.pathing;

import static mindustry.Vars.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.ai.WaveSpawner;
import mindustry.content.Blocks;
import mindustry.game.SpawnGroup;
import mindustry.gen.Unit;
import mindustry.world.Tile;

public class CustomWaveSpawner extends WaveSpawner {
    private Field spawningField;
    private Field spawnsField;
    private Method eachGroundSpawnMethod;

    public CustomWaveSpawner() {
        try {
            // Accessing the private field 'spawning'
            spawningField = WaveSpawner.class.getDeclaredField("spawning");
            spawningField.setAccessible(true);

            // Accessing the private field 'spawns'
            spawnsField = WaveSpawner.class.getDeclaredField("spawns");
            spawnsField.setAccessible(true);

            // Accessing the private method 'eachGroundSpawn'
            eachGroundSpawnMethod = WaveSpawner.class.getDeclaredMethod("eachGroundSpawn", int.class, Object.class);
            eachGroundSpawnMethod.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSpawning(boolean value) {
        try {
            spawningField.set(this, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSpawning() {
        try {
            return (boolean) spawningField.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void eachGroundSpawn(int filterPos, CustomSpawnConsumer cons) {
        try {
            // Wrapping the cons in an anonymous inner class implementing the original
            // interface
            eachGroundSpawnMethod.invoke(this, filterPos, (Object) new Object() {
                @SuppressWarnings("unused")
                public void accept(float x, float y, boolean shockwave) {
                    cons.accept(x, y, shockwave);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Seq<Tile> getSpawns() {
        try {
            return (Seq<Tile>) spawnsField.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void spawnEnemies() {
        setSpawning(true);

        eachGroundSpawn(-1, (spawnX, spawnY, doShockwave) -> {
            if (doShockwave) {
                doShockwave(spawnX, spawnY);
            }
        });

        for (SpawnGroup group : state.rules.spawns) {
            if (group.type == null)
                continue;

            int spawned = group.getSpawned(state.wave - 1);
            float spread = tilesize * 2;

            eachGroundSpawn(group.spawn, (spawnX, spawnY, doShockwave) -> {
                for (int i = 0; i < spawned; i++) {
                    Tmp.v1.rnd(spread);

                    Unit unit = group.createUnit(state.rules.waveTeam, state.wave - 1);
                    unit.set(spawnX + Tmp.v1.x, spawnY + Tmp.v1.y);
                    spawnEffect(unit);
                }
            });
        }

        Time.run(121f, () -> setSpawning(false));
    }

    @Override
    public void reset() {
        setSpawning(false);
        getSpawns().clear();

        for (Tile tile : world.tiles) {
            if (tile.overlay() == Blocks.spawn) {
                getSpawns().add(tile);
            }
        }
    }

    // Define a compatible interface
    @FunctionalInterface
    private interface CustomSpawnConsumer {
        void accept(float x, float y, boolean shockwave);
    }
}
