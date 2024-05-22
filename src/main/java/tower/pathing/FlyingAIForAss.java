package tower.pathing;

import static mindustry.Vars.*;

import arc.math.Mathf;
import mindustry.ai.Pathfinder;
import mindustry.entities.units.AIController;
import mindustry.gen.Teamc;
import mindustry.world.Tile;
import mindustry.world.meta.BlockFlag;

public class FlyingAIForAss extends AIController {
    private boolean pathingSuccess;

    @Override
    public void updateMovement() {
        unloadPayloads();

        if (target != null && unit.hasWeapons()) {
            if (unit.type.circleTarget) {
                pathfind(Pathfinder.fieldCore);
                if (!wasPathingSuccessful()) {
                    circleAttack(60f);
                    moveTo(target, unit.type.range * 0.8f);
                }

            } else {
                pathfind(Pathfinder.fieldCore);
                unit.lookAt(target);
                if (!wasPathingSuccessful()) {
                    moveTo(target, unit.type.range * 0.8f);
                }
            }
        }

        if (target == null && state.rules.waves && unit.team == state.rules.defaultTeam) {
            pathfind(Pathfinder.fieldCore);
        }
    }

    @Override
    public Teamc findTarget(float x, float y, float range, boolean air, boolean ground) {
        var result = findMainTarget(x, y, range, air, ground);

        // if the main target is in range, use it, otherwise target whatever is closest
        return checkTarget(result, x, y, range) ? target(x, y, range, air, ground) : result;
    }

    @Override
    public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
        var core = targetFlag(x, y, BlockFlag.core, true);

        if (core != null && Mathf.within(x, y, core.getX(), core.getY(), range)) {
            return core;
        }

        for (var flag : unit.type.targetFlags) {
            if (flag == null) {
                Teamc result = target(x, y, range, air, ground);
                if (result != null)
                    return result;
            } else if (ground) {
                Teamc result = targetFlag(x, y, flag, true);
                if (result != null)
                    return result;
            }
        }

        return core;
    }

    @Override
    public void pathfind(int pathTarget) {
        pathingSuccess = false; // Reset the flag at the start of pathfinding
        int costType = unit.pathType();

        Tile tile = unit.tileOn();
        if (tile == null)
            return;
        Tile targetTile = pathfinder.getTargetTile(tile, pathfinder.getField(unit.team, costType, pathTarget));

        if (tile == targetTile || (costType == Pathfinder.costNaval && !targetTile.floor().isLiquid))
            return;
        
        unit.movePref(vec.trns(unit.angleTo(targetTile.worldx(), targetTile.worldy()), prefSpeed()));
        pathingSuccess = true; // Pathing was successful
    }

    // Method to check if the last pathfinding attempt was successful
    public boolean wasPathingSuccessful() {
        return pathingSuccess;
    }

    public float prefSpeed() {
        return unit.speed();
    }

}