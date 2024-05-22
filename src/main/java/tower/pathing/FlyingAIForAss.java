package tower.pathing;

import static mindustry.Vars.*;

import arc.math.Mathf;
import arc.util.Log;
import mindustry.ai.Pathfinder;
import mindustry.entities.units.AIController;
import mindustry.gen.Teamc;
import mindustry.world.Tile;
import mindustry.world.meta.BlockFlag;

public class FlyingAIForAss extends AIController {
    private boolean pathingSuccess;
    private long lastMoveTime;

    @Override
    public void updateMovement() {
        if (target != null && unit.hasWeapons()) {
            if (unit.type.circleTarget) {
                moveToTargetCircleAttack();
            } else {
                moveToTarget();
            }
        }

        if (target == null && state.rules.waves && unit.team == state.rules.defaultTeam) {
            pathfind(Pathfinder.fieldCore);
        }
    }

    private void moveToTargetCircleAttack() {
        pathfind(Pathfinder.fieldCore);
        if (!wasPathingSuccessful() || System.currentTimeMillis() - lastMoveTime > 5000) {
            circleAttack(60f);
            moveToTarget();
        }
    }

    private void moveToTarget() {
        pathfind(Pathfinder.fieldCore);
        unit.lookAt(target);
        if (!wasPathingSuccessful() || System.currentTimeMillis() - lastMoveTime > 5000) {
            circleAttack(60f);
            moveTo(target, unit.type.range * 0.8f);
        }
    }

    @Override
    public Teamc findTarget(float x, float y, float range, boolean air, boolean ground) {
        Log.info("findTarget called");
        var result = findMainTarget(x, y, range, air, ground);

        // if the main target is in range, use it, otherwise target whatever is closest
        return checkTarget(result, x, y, range)? target(x, y, range, air, ground) : result;
    }

    @Override
    public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
        Log.info("findMainTarget called");
        var core = targetFlag(x, y, BlockFlag.core, true);

        if (core!= null && Mathf.within(x, y, core.getX(), core.getY(), range)) {
            return core;
        }

        for (var flag : unit.type.targetFlags) {
            if (flag == null) {
                Teamc result = target(x, y, range, air, ground);
                if (result!= null)
                    return result;
            } else if (ground) {
                Teamc result = targetFlag(x, y, flag, true);
                if (result!= null)
                    return result;
            }
        }

        return core;
    }

    @Override
    public void pathfind(int pathTarget) {
        Tile currentTile = unit.tileOn();
        if (currentTile == null) {
            return;
        }
        Tile targetTile = pathfinder.getTargetTile(currentTile,
                pathfinder.getField(unit.team, unit.pathType(), pathTarget));

        if (currentTile.equals(targetTile) || (unit.pathType() == Pathfinder.costNaval || targetTile.isDarkened())) {
            return;
        }
        unit.movePref(vec.trns(unit.angleTo(targetTile.worldx(), targetTile.worldy()), prefSpeed()));
        pathingSuccess = true;
        if (wasPathingSuccessful()) {
            lastMoveTime = System.currentTimeMillis();
        }
    }

    // Method to check if the last pathfinding attempt was successful
    public boolean wasPathingSuccessful() {
        return pathingSuccess;
    }

    public float prefSpeed() {
        return unit.speed();
    }
}