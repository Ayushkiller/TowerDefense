package tower.Domain;

import mindustry.content.UnitTypes;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UnitsTable {
    public static List<Map<String, Object>> units = new ArrayList<>();

    static {
        units.add(Map.of("unit", UnitTypes.alpha, "price",   4));
        units.add(Map.of("unit", UnitTypes.beta, "price",   5));
        units.add(Map.of("unit", UnitTypes.gamma, "price",   6));
        units.add(Map.of("unit", UnitTypes.evoke, "price",   8));
        units.add(Map.of("unit", UnitTypes.incite, "price",   9));
        units.add(Map.of("unit", UnitTypes.emanate, "price",   10));
        units.add(Map.of("unit", UnitTypes.dagger, "price",   1));
        units.add(Map.of("unit", UnitTypes.mace, "price",   4));
        units.add(Map.of("unit", UnitTypes.fortress, "price",   12));
        units.add(Map.of("unit", UnitTypes.scepter, "price",   30));
        units.add(Map.of("unit", UnitTypes.reign, "price",   50));
        units.add(Map.of("unit", UnitTypes.nova, "price",   1));
        units.add(Map.of("unit", UnitTypes.pulsar, "price",   4));
        units.add(Map.of("unit", UnitTypes.quasar, "price",   13));
        units.add(Map.of("unit", UnitTypes.vela, "price",   32));
        units.add(Map.of("unit", UnitTypes.corvus, "price",   52));
        units.add(Map.of("unit", UnitTypes.crawler, "price",   1));
        units.add(Map.of("unit", UnitTypes.atrax, "price",   3));
        units.add(Map.of("unit", UnitTypes.spiroct, "price",   12));
        units.add(Map.of("unit", UnitTypes.arkyid, "price",   30));
        units.add(Map.of("unit", UnitTypes.toxopid, "price",   50));
        units.add(Map.of("unit", UnitTypes.flare, "price",   1));
        units.add(Map.of("unit", UnitTypes.horizon, "price",   3));
        units.add(Map.of("unit", UnitTypes.zenith, "price",   10));
        units.add(Map.of("unit", UnitTypes.antumbra, "price",   27));
        units.add(Map.of("unit", UnitTypes.eclipse, "price",   48));
        units.add(Map.of("unit", UnitTypes.mono, "price",   2));
        units.add(Map.of("unit", UnitTypes.poly, "price",   5));
        units.add(Map.of("unit", UnitTypes.mega, "price",   13));
        units.add(Map.of("unit", UnitTypes.quad, "price",   27));
        units.add(Map.of("unit", UnitTypes.oct, "price",   45));
        units.add(Map.of("unit", UnitTypes.risso, "price",   2));
        units.add(Map.of("unit", UnitTypes.minke, "price",   5));
        units.add(Map.of("unit", UnitTypes.bryde, "price",   13));
        units.add(Map.of("unit", UnitTypes.sei, "price",   32));
        units.add(Map.of("unit", UnitTypes.omura, "price",   52));
        units.add(Map.of("unit", UnitTypes.retusa, "price",   2));
        units.add(Map.of("unit", UnitTypes.oxynoe, "price",   5));
        units.add(Map.of("unit", UnitTypes.cyerce, "price",   13));
        units.add(Map.of("unit", UnitTypes.aegires, "price",   32));
        units.add(Map.of("unit", UnitTypes.navanax, "price",   52));
        units.add(Map.of("unit", UnitTypes.stell, "price",   3));
        units.add(Map.of("unit", UnitTypes.locus, "price",   8));
        units.add(Map.of("unit", UnitTypes.precept, "price",   18));
        units.add(Map.of("unit", UnitTypes.vanquish, "price",   40));
        units.add(Map.of("unit", UnitTypes.conquer, "price",   65));
        units.add(Map.of("unit", UnitTypes.merui, "price",   3));
        units.add(Map.of("unit", UnitTypes.cleroi, "price",   9));
        units.add(Map.of("unit", UnitTypes.anthicus, "price",   20));
        units.add(Map.of("unit", UnitTypes.tecta, "price",   42));
        units.add(Map.of("unit", UnitTypes.collaris, "price",   65));
        units.add(Map.of("unit", UnitTypes.elude, "price",   3));
        units.add(Map.of("unit", UnitTypes.avert, "price",   8));
        units.add(Map.of("unit", UnitTypes.obviate, "price",   18));
        units.add(Map.of("unit", UnitTypes.quell, "price",   40));
        units.add(Map.of("unit", UnitTypes.disrupt, "price",   65));
        units.add(Map.of("unit", UnitTypes.renale, "price",   12));
        units.add(Map.of("unit", UnitTypes.latum, "price",   60));
    }
  public static Unit spawnUnit(Tile tile, Player player) {

    Random random = new Random();
    int index = random.nextInt(units.size());
    Map<String, Object> unitMap = units.get(index);
    UnitType unitType = (UnitType) unitMap.get("unit");
    return unitType.spawn(tile.worldx(), tile.worldy());
 }
}
