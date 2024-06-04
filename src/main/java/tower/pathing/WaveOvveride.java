package tower.pathing;

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
import mindustry.game.EventType.WorldLoadEvent;
import mindustry.game.SpawnGroup;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Unit;
import mindustry.world.Tile;

public class WaveOvveride extends WaveSpawner{
    private static final float coreMargin = tilesize * 2f, maxSteps = 30;

    private int tmpCount;
    private Seq<Tile> spawns = new Seq<>();
    private boolean spawning = false;
    private boolean any = false;
    private Tile firstSpawn = null;

    public void WaveSpawner(){
        Events.on(WorldLoadEvent.class, e -> reset());
    }

    @Nullable
    public Tile getFirstSpawn(){
        firstSpawn = null;
        eachGroundSpawn((cx, cy) -> {
            firstSpawn = world.tile(cx, cy);
        });
        return firstSpawn;
    }

    public int countSpawns(){
        return spawns.size;
    }

    public Seq<Tile> getSpawns(){
        return spawns;
    }

    /** @return true if the player is near a ground spawn point. */
    public boolean playerNear(){
        return state.hasSpawns() && !player.dead() && spawns.contains(g -> Mathf.dst(g.x * tilesize, g.y * tilesize, player.x, player.y) < state.rules.dropZoneRadius && player.team() != state.rules.waveTeam);
    }
    @Override
    public void spawnEnemies(){
        spawning = true;
    
        eachGroundSpawn(-1, (spawnX, spawnY, doShockwave) -> {
            if(doShockwave){
                doShockwave(spawnX, spawnY);
            }
        });
    
        for(SpawnGroup group : state.rules.spawns){
            if(group.type == null) continue;
    
            int spawned = group.getSpawned(state.wave - 1);
    
            float spread = tilesize * 2; // Adjust spread as needed
    
            eachGroundSpawn(group.spawn, (spawnX, spawnY, doShockwave) -> {
    
                for(int i = 0; i < spawned; i++){
                    Tmp.v1.rnd(spread);
    
                    Unit unit = group.createUnit(state.rules.waveTeam, state.wave - 1);
                    unit.set(spawnX + Tmp.v1.x, spawnY + Tmp.v1.y);
                    spawnEffect(unit);
                }
            });
        }
    
        Time.run(121f, () -> spawning = false);
    }

    public void doShockwave(float x, float y){
        Fx.spawnShockwave.at(x, y, state.rules.dropZoneRadius);
        Damage.damage(state.rules.waveTeam, x, y, state.rules.dropZoneRadius, 99999999f, true);
    }

    public void eachGroundSpawn(Intc2 cons){
        eachGroundSpawn(-1, (x, y, shock) -> cons.get(World.toTile(x), World.toTile(y)));
    }

    private void eachGroundSpawn(int filterPos, SpawnConsumer cons){
        if(state.hasSpawns()){
            for(Tile spawn : spawns){
                if(filterPos != -1 && filterPos != spawn.pos()) continue;

                cons.accept(spawn.worldx(), spawn.worldy(), true);
            }
        }

        if(state.rules.attackMode && state.teams.isActive(state.rules.waveTeam) && !state.teams.playerCores().isEmpty()){
            Building firstCore = state.teams.playerCores().first();
            for(Building core : state.rules.waveTeam.cores()){
                if(filterPos != -1 && filterPos != core.pos()) continue;

                Tmp.v1.set(firstCore).sub(core).limit(coreMargin + core.block.size * tilesize /2f * Mathf.sqrt2);

                boolean valid = false;
                int steps = 0;

                //keep moving forward until the max step amount is reached
                while(steps++ < maxSteps){
                    int tx = World.toTile(core.x + Tmp.v1.x), ty = World.toTile(core.y + Tmp.v1.y);
                    any = false;
                    Geometry.circle(tx, ty, world.width(), world.height(), 3, (x, y) -> {
                        if(world.solid(x, y)){
                            any = true;
                        }
                    });

                    //nothing is in the way, spawn it
                    if(!any){
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


    public int countGroundSpawns(){
        tmpCount = 0;
        eachGroundSpawn((x, y) -> tmpCount ++);
        return tmpCount;
    }

    
    public boolean isSpawning(){
        return spawning && !net.client();
    }

    public void reset(){
        spawning = false;
        spawns.clear();

        for(Tile tile : world.tiles){
            if(tile.overlay() == Blocks.spawn){
                spawns.add(tile);
            }
        }
    }

    /** Applies the standard wave spawn effects to a unit - invincibility, unmoving. */
    public void spawnEffect(Unit unit){
        spawnEffect(unit, unit.angleTo(world.width()/2f * tilesize, world.height()/2f * tilesize));
    }

    /** Applies the standard wave spawn effects to a unit - invincibility, unmoving. */
    public void spawnEffect(Unit unit, float rotation){
        unit.rotation = rotation;
        unit.apply(StatusEffects.unmoving, 30f);
        unit.apply(StatusEffects.invincible, 60f);
        unit.add();
        unit.unloaded();

        Events.fire(new UnitSpawnEvent(unit));
        Call.spawnEffect(unit.x, unit.y, unit.rotation, unit.type);
    }

    private interface SpawnConsumer{
        void accept(float x, float y, boolean shockwave);
    }

}