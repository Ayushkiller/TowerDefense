package tower.pathing;

import mindustry.ai.WaveSpawner;
import mindustry.core.World;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.world.Tile;
import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.Tmp;

public class FlyerWaves extends WaveSpawner {
    private static final float  coreMargin1 = tilesize * 2f, maxSteps = 30;
    private boolean any1 = false;
    @Override
    public void spawnEnemies() {
        System.out.println("Spawning enemies for wave: " + state.wave); // Log the wave number
    
        for(mindustry.game.SpawnGroup group : state.rules.spawns){
            if(group.type == null) continue;
    
            int spawned = group.getSpawned(state.wave - 1);
            System.out.println("Spawn group: " + group.type.name + ", Spawned: " + spawned); // Log the spawn group and number of units spawned
    
            if(group.type.flying){
                float spread = tilesize * 2;
    
                eachGroundSpawnWithMagic(group.spawn, (spawnX, spawnY, doShockwave) -> {
    
                    for(int i = 0; i < spawned; i++){
                        Tmp.v1.rnd(spread);
    
                        Unit unit = group.createUnit(state.rules.waveTeam, state.wave - 1);
                        unit.set(spawnX + Tmp.v1.x, spawnY + Tmp.v1.y);
                        spawnEffect(unit);
                        System.out.println("Spawned flying unit: " + unit.type.name); // Log the type of flying unit spawned
                    }
                });
            }
            else{
                float spread = tilesize * 2;
    
                eachGroundSpawnWithMagic(group.spawn, (spawnX, spawnY, doShockwave) -> {
    
                    for(int i = 0; i < spawned; i++){
                        Tmp.v1.rnd(spread);
    
                        Unit unit = group.createUnit(state.rules.waveTeam, state.wave - 1);
                        unit.set(spawnX + Tmp.v1.x, spawnY + Tmp.v1.y);
                        spawnEffect(unit);
                        System.out.println("Spawned ground unit: " + unit.type.name); // Log the type of ground unit spawned
                    }
                });
            }
        }
    }
    private void eachGroundSpawnWithMagic(int filterPos, SpawnConsumerG cons){
        if(state.hasSpawns()){
            for(Tile spawn : getSpawns()){
                if(filterPos != -1 && filterPos != spawn.pos()) continue;

                cons.accept(spawn.worldx(), spawn.worldy(), true);
            }
        }

        if(state.rules.attackMode && state.teams.isActive(state.rules.waveTeam) && !state.teams.playerCores().isEmpty()){
            Building firstCore = state.teams.playerCores().first();
            for(Building core : state.rules.waveTeam.cores()){
                if(filterPos != -1 && filterPos != core.pos()) continue;

                Tmp.v1.set(firstCore).sub(core).limit(coreMargin1 + core.block.size * tilesize /2f * Mathf.sqrt2);

                boolean valid = false;
                int steps = 0;

                //keep moving forward until the max step amount is reached
                while(steps++ < maxSteps){
                    int tx = World.toTile(core.x + Tmp.v1.x), ty = World.toTile(core.y + Tmp.v1.y);
                    any1 = false;
                    Geometry.circle(tx, ty, world.width(), world.height(), 3, (x, y) -> {
                        if(world.solid(x, y)){
                            any1 = true;
                        }
                    });

                    //nothing is in the way, spawn it
                    if(!any1){
                        valid = true;
                        break;
                    }else{
                        //make the vector longer
                        Tmp.v1.setLength(Tmp.v1.len() + tilesize*1.1f);
                    }
                }

                if(valid){
                    cons.accept(core.x + Tmp.v1.x, core.y + Tmp.v1.y, false);
                }
            }
        }
    }
        private interface SpawnConsumerG{
        void accept(float x, float y, boolean shockwave);
    }

}