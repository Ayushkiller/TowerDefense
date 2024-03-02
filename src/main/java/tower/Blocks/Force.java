package tower.Blocks;

import arc.struct.Seq;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.ForceProjector;
import tower.PluginLogic;

public class Force {

    public static Seq<Unit> getNearbyUnits(Tile tile, float radius) {
        Seq<Unit> nearbyUnits = new Seq<>();
        for (Unit unit : Groups.unit) {
            if (unit.dst(tile.worldx(), tile.worldy()) <= radius) {
                nearbyUnits.add(unit);
            }
        }
        return nearbyUnits;
    }

    public static void applySlowEffectToUnits(Tile tile) {
        if (tile.build instanceof ForceProjector.ForceBuild) {
            ForceProjector.ForceBuild forceProjector = (ForceProjector.ForceBuild) tile.build;
            float realRadius = forceProjector.realRadius();
            if (realRadius > 0 && !forceProjector.broken) {
                Seq<Unit> units = getNearbyUnits(tile, realRadius);
                for (Unit unit : units) {
                    if (unit.team != forceProjector.team) {
                        unit.speedMultiplier(0.4f * PluginLogic.multiplier);
                    }
                }
            }
        }
    }
}