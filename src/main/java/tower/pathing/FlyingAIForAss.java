package tower.pathing;

import arc.math.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import static mindustry.Vars.*;

public class FlyingAIForAss extends AIController {


    @Override
    public void updateMovement() {
        unloadPayloads();

        Building core = unit.closestEnemyCore();

        if (core != null && unit.within(core, unit.range() / 1.3f + core.block.size * tilesize / 2f)) {
            target = core;
            for (var mount : unit.mounts) {
                if (mount.weapon.controllable && mount.weapon.bullet.collidesGround) {
                    mount.target = core;
                }
            }
        }

        boolean move = true;

        if ((core == null || !unit.within(core, unit.type.range * 0.5f))) {
            if (state.rules.waves && unit.team == state.rules.defaultTeam) {
                Tile spawner = getClosestSpawner();
                if (spawner != null && unit.within(spawner, state.rules.dropZoneRadius + 12f)) {
                    moveTo(getClosestSpawner(), state.rules.dropZoneRadius + 10f);
                }
                if (spawner == null && core == null) move = false;
            }

            if (core == null && (!state.rules.waves || getClosestSpawner() == null)) {
                move = false;
            }

            if (move) pathfind(TowerPathfinder.fieldCore);
        }

        if (unit.type.canBoost && unit.elevation > 0.001f && !unit.onSolid()) {
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, unit.type.riseSpeed);
        }
        faceTarget();
        if (!move) {
            if (target != null && unit.hasWeapons()) {
                if (unit.type.circleTarget) {
                    circleAttack(120f);
                } else {
                    moveTo(target, unit.type.range * 0.8f);
                    unit.lookAt(target);
                }
            }

            if (target == null && state.rules.waves && unit.team == state.rules.defaultTeam) {
                moveTo(getClosestSpawner(), state.rules.dropZoneRadius + 130f);
            }
        }
    }
}