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
        Log.info("updateMovement called");
        Log.info("Unit: " + unit);
        unloadPayloads();
        lastMoveTime = System.currentTimeMillis();
        if (target!= null && unit.hasWeapons()) {
            Log.info("Unit has weapons and target exists");
            Log.info("Target: " + target);
            if (unit.type.circleTarget) {
                Log.info("Unit is a circle target");
                pathfind(Pathfinder.fieldCore);
                if (!wasPathingSuccessful()) {
                    Log.info("Pathing was not successful");
                } else if (System.currentTimeMillis() - lastMoveTime > 5000) {
                    Log.info("Time since last move exceeds 5000 milliseconds");
                }
                if (!wasPathingSuccessful() || System.currentTimeMillis() - lastMoveTime > 5000) {
                    Log.info("Executing circle attack");
                    circleAttack(60f);
                    Log.info("Moving to target");
                    moveTo(target, unit.type.range * 0.8f);
                }
            } else {
                Log.info("Unit is not a circle target");
                pathfind(Pathfinder.fieldCore);
                unit.lookAt(target);
                if (!wasPathingSuccessful()) {
                    Log.info("Pathing was not successful");
                } else if (System.currentTimeMillis() - lastMoveTime > 5000) {
                    Log.info("Time since last move exceeds 5000 milliseconds");
                }
                if (!wasPathingSuccessful() || System.currentTimeMillis() - lastMoveTime > 5000) {
                    Log.info("Executing circle attack");
                    circleAttack(60f);
                    Log.info("Moving to target");
                    moveTo(target, unit.type.range * 0.8f);
                }
            }
        }

        if (target == null && state.rules.waves && unit.team == state.rules.defaultTeam) {
            Log.info("Target is null and waves are enabled");
            pathfind(Pathfinder.fieldCore);
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
        Log.info("pathfind called");
        Log.info("pathTarget: " + pathTarget);
        pathingSuccess = false; // Reset the flag at the start of pathfinding
        int costType = unit.pathType();

        Tile tile = unit.tileOn();
        if (tile == null) {
            Log.info("Tile is null");
            return;
        }
        Log.info("Tile: " + tile.toString());
        Tile targetTile = pathfinder.getTargetTile(tile, pathfinder.getField(unit.team, costType, pathTarget));

        if (tile == targetTile || (costType == Pathfinder.costNaval &&!targetTile.floor().isLiquid)) {
            Log.info("Target tile is same as current tile or target tile is not a liquid");
            return;
        }
        
        Log.info("Target tile: " + targetTile.toString());
        unit.movePref(vec.trns(unit.angleTo(targetTile.worldx(), targetTile.worldy()), prefSpeed()));
        pathingSuccess = true; // Pathing was successful
        if (wasPathingSuccessful()) {
            Log.info("Pathing was successful");
            lastMoveTime = System.currentTimeMillis(); // Update the last move time
        }
    }

    // Method to check if the last pathfinding attempt was successful
    public boolean wasPathingSuccessful() {
        Log.info("wasPathingSuccessful called");
        return pathingSuccess;
    }

    public float prefSpeed() {
        Log.info("prefSpeed called");
        return unit.speed();
    }
}