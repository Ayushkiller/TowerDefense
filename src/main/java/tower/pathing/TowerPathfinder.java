package tower.pathing;

import mindustry.ai.Pathfinder;
import mindustry.gen.PathTile;
import mindustry.world.Tile;

import static mindustry.Vars.pathfinder;
import static mindustry.Vars.state;
import static tower.PluginLogic.isPath;

public class TowerPathfinder extends Pathfinder {

    public static final int impassable = -1, notPath = 999999;

    public TowerPathfinder() {
      costTypes.set(costGround, (team, tile)->{
            if (team != state.rules.waveTeam.id) {
                return  1; // Default cost for non-waveTeam units
            }
            return (PathTile.allDeep(tile) || ((PathTile.team(tile) ==  0 || PathTile.team(tile) == team) && PathTile.solid(tile))) ? impassable :  1 +
                (PathTile.deep(tile) ? notPath :  0) +
                (PathTile.damages(tile) ?  50 :  0) +
                (PathTile.nearSolid(tile) ?  50 :  0) +
                (PathTile.nearLiquid(tile) ?  10 :  0);
        });

        costTypes.set(costLegs, (team, tile)->{
            if (team != state.rules.waveTeam.id) {
                return  1; // Default cost for non-waveTeam units
            }
            return (PathTile.allDeep(tile) || ((PathTile.team(tile) ==  0 || PathTile.team(tile) == team) && PathTile.solid(tile))) ? impassable :  1 +
                (PathTile.deep(tile) ? notPath :  0) +
                (PathTile.damages(tile) ?  50 :  0) +
                (PathTile.nearSolid(tile) ?  50 :  0) +
                (PathTile.nearLiquid(tile) ?  10 :  0);
        });

        costTypes.set(costLegs, (team, tile)->{
            if (team != state.rules.waveTeam.id) {
                return  1; // Default cost for non-waveTeam units
            }
            return (PathTile.allDeep(tile) || ((PathTile.team(tile) ==  0 || PathTile.team(tile) == team) && PathTile.solid(tile))) ? impassable :  1 +
                (PathTile.deep(tile) ? notPath :  0) +
                (PathTile.damages(tile) ?  50 :  0) +
                (PathTile.nearSolid(tile) ?  50 :  0);
        });
    }

    public static void init() {
        pathfinder = new TowerPathfinder();
    }

    @Override
    public int packTile(Tile tile) {
        boolean nearLiquid = false, nearSolid = false, nearLegSolid = false, nearGround = false, allDeep = tile.floor().isDeep();

        for(int i = 0; i < 4; i++){
            var other = tile.nearby(i);
            if(other == null) continue;

            if(other.floor().isLiquid) nearLiquid = true;
            if(other.solid() || !isPath(other)) nearSolid = true;
            if(other.legSolid() || !isPath(other)) nearLegSolid = true;
            if(!other.floor().isLiquid) nearGround = true;
            if(!other.floor().isDeep()) allDeep = false;
        }

        return PathTile.get(
            0,
            tile.getTeamID(),
            tile.solid(),
            tile.floor().isLiquid,
            tile.legSolid(),
            nearLiquid,
            nearGround,
            nearSolid,
            nearLegSolid,
            tile.floor().isDeep() || !isPath(tile),
            tile.floor().damageTaken > 0f || !isPath(tile),
            allDeep,
            tile.block().teamPassable
        );
    }
}