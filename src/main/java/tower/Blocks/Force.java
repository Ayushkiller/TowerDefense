package tower.Blocks;

import mindustry.gen.Unit;
import java.util.Map;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.ForceProjector;
import tower.PluginLogic;
import tower.Domain.UnitsTable;
public class Force {

   

    public static void applySlowEffectToUnits(Tile tile) {
        if (tile.build instanceof ForceProjector.ForceBuild) {
            ForceProjector.ForceBuild forceProjector = (ForceProjector.ForceBuild) tile.build;
            float realRadius = forceProjector.realRadius();
            if (realRadius > 0 && !forceProjector.broken) {
                for (Map<String, Object> unitMap : UnitsTable.units) {
                    // Assuming you have a way to get the Unit object from the map
                    Unit unit = (Unit) unitMap.get("unit");
                    if (unit.team != forceProjector.team) {
                        unit.speedMultiplier(0f * PluginLogic.multiplier);
                    }
                }
            }
        }
    }
}