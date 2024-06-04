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
    private long lastMoveTime;

    @Override
    public void updateMovement() {
        // If the unit has a target and weapons, execute attack logic
        if (target != null && unit.hasWeapons()) {
            if (unit.type.circleTarget) {
                executeCircleAttack();
            } else {
                pursueTarget();
            }
        }

        // If no target is found, pathfind to the core
        if (target == null && state.rules.waves && unit.team == state.rules.defaultTeam) {
            pathfindToCore();
        }
    }

    private void executeCircleAttack() {
        pathfind(Pathfinder.fieldCore);
        if (!wasPathingSuccessful() || timeSinceLastMove() > 1000) {
            performCircleAttack(60f);
            pursueTarget();
        }
    }

    private void pursueTarget() {
        pathfind(Pathfinder.fieldCore);
        unit.lookAt(target);
        if (!wasPathingSuccessful() || timeSinceLastMove() > 1000) {
            performCircleAttack(60f);
            moveTo(target, unit.type.range * 800f);
        }
    }

    @Override
    public Teamc findTarget(float x, float y, float range, boolean air, boolean ground) {
        Teamc result = findMainTarget(x, y, range, air, ground);
        return checkTarget(result, x, y, range) ? target(x, y, range, air, ground) : result;
    }

    @Override
    public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
        Teamc core = targetFlag(x, y, BlockFlag.core, true);

        if (core != null && Mathf.within(x, y, core.getX(), core.getY(), range)) {
            return core;
        }

        for (BlockFlag flag : unit.type.targetFlags) {
            if (flag == null) {
                Teamc result = target(x, y, range, air, ground);
                if (result != null) return result;
            } else if (ground) {
                Teamc result = targetFlag(x, y, flag, true);
                if (result != null) return result;
            }
        }

        return core;
    }

    @Override
    public void pathfind(int pathTarget) {
        Tile currentTile = unit.tileOn();
        if (currentTile == null) return;

        Tile targetTile = pathfinder.getTargetTile(currentTile, pathfinder.getField(unit.team, unit.pathType(), pathTarget));
        if (targetTile == null || currentTile.equals(targetTile) || unit.pathType() == Pathfinder.costNaval || targetTile.isDarkened()) return;

        unit.movePref(vec.trns(unit.angleTo(targetTile.worldx(), targetTile.worldy()), preferredSpeed()));
        pathingSuccess = true;
        lastMoveTime = System.currentTimeMillis();
    }

    private void pathfindToCore() {
        pathfind(Pathfinder.fieldCore);
    }

    private void performCircleAttack(float radius) {
        if (target == null) return;
        float angleToTarget = unit.angleTo(target);
        float circlingSpeed = 5f; 
        float newAngle = angleToTarget + circlingSpeed;
        // Calculate the new position based on the new angle and desired radius
        float targetX = target.getX() + Mathf.cosDeg(newAngle) * radius;
        float targetY = target.getY() + Mathf.sinDeg(newAngle) * radius;
        // Move the unit to the calculated position
        unit.movePref(vec.set(targetX, targetY).sub(unit.x, unit.y).limit(preferredSpeed()));
        // Face the unit towards the target
        unit.lookAt(target);
    }
    

    private long timeSinceLastMove() {
        return System.currentTimeMillis() - lastMoveTime;
    }

    public boolean wasPathingSuccessful() {
        return pathingSuccess;
    }

    public float preferredSpeed() {
        return unit.speed();
    }
}
