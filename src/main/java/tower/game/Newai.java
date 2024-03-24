package tower.game;

import static mindustry.Vars.*;

import arc.math.Mathf;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;
import mindustry.world.Tile;

public class Newai extends AIController{

    @Override
    public void updateMovement(){

        Building core = unit.closestEnemyCore();

        if(core != null && unit.within(core, unit.range() / 1.3f + core.block.size * tilesize / 2f)){
            target = core;
            for(var mount : unit.mounts){
                if(mount.weapon.controllable && mount.weapon.bullet.collidesGround){
                    mount.target = core;
                }
            }
        }

        if((core == null || !unit.within(core, unit.type.range * 0.5f))){


            if(state.rules.waves && unit.team == state.rules.defaultTeam){
                Tile spawner = getClosestSpawner();
                if(spawner != null && unit.within(spawner, state.rules.dropZoneRadius + 120f));
                if(spawner == null && core == null);
            }

            //no reason to move if there's nothing there
            if(core == null && (!state.rules.waves || getClosestSpawner() == null)){
                ;
            }


        }

        if(unit.type.canBoost && unit.elevation > 0.001f && !unit.onSolid()){
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, unit.type.riseSpeed);
        }

        faceTarget();
    }
}