package tower.Domain;

import mindustry.content.UnitTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnitsTable {
    public static List<Map<String, Object>> units = new ArrayList<>();

    static {
        
        units.add(Map.of("unit", UnitTypes.alpha, "price", 4, "name", "Alpha", "tier", 0));
        units.add(Map.of("unit", UnitTypes.beta, "price",   5, "name", "Beta", "tier", 0));
        units.add(Map.of("unit", UnitTypes.gamma, "price",   6, "name", "Gamma", "tier", 0));
        units.add(Map.of("unit", UnitTypes.evoke, "price",   8, "name", "Evoke", "tier", 0));
        units.add(Map.of("unit", UnitTypes.incite, "price",   9, "name", "Incite", "tier", 0));
        units.add(Map.of("unit", UnitTypes.emanate, "price",   10, "name", "Emanate", "tier", 0));
        units.add(Map.of("unit", UnitTypes.dagger, "price",   1, "name", "Dagger", "tier", 1));
        units.add(Map.of("unit", UnitTypes.mace, "price",   4, "name", "Mace", "tier", 2));
        units.add(Map.of("unit", UnitTypes.fortress, "price",   12, "name", "Fortress", "tier", 3));
        units.add(Map.of("unit", UnitTypes.scepter, "price",   30, "name", "Scepter", "tier", 4));
        units.add(Map.of("unit", UnitTypes.reign, "price",   50, "name", "Reign", "tier", 5));
        units.add(Map.of("unit", UnitTypes.nova, "price",   1, "name", "Nova", "tier", 1));
        units.add(Map.of("unit", UnitTypes.pulsar, "price",   4, "name", "Pulsar", "tier", 2));
        units.add(Map.of("unit", UnitTypes.quasar, "price",   13, "name", "Quasar", "tier", 3));
        units.add(Map.of("unit", UnitTypes.vela, "price",   32, "name", "Vela", "tier", 4));
        units.add(Map.of("unit", UnitTypes.corvus, "price",   52, "name", "Corvus", "tier", 5));
        units.add(Map.of("unit", UnitTypes.crawler, "price",   1, "name", "Crawler", "tier", 1));
        units.add(Map.of("unit", UnitTypes.atrax, "price",   3, "name", "Atrax", "tier", 2));
        units.add(Map.of("unit", UnitTypes.spiroct, "price",   12, "name", "Spiroct", "tier", 3));
        units.add(Map.of("unit", UnitTypes.arkyid, "price",   30, "name", "Arkyid", "tier", 4));
        units.add(Map.of("unit", UnitTypes.toxopid, "price",   50, "name", "Toxopid", "tier", 5));
        units.add(Map.of("unit", UnitTypes.flare, "price",   1, "name", "Flare", "tier", 1));
        units.add(Map.of("unit", UnitTypes.horizon, "price",   3, "name", "Horizon", "tier", 2));
        units.add(Map.of("unit", UnitTypes.zenith, "price",   10, "name", "Zenith", "tier", 3));
        units.add(Map.of("unit", UnitTypes.antumbra, "price",   27, "name", "Antumbra", "tier", 4));
        units.add(Map.of("unit", UnitTypes.eclipse, "price",   48, "name", "Eclipse", "tier", 5));
        units.add(Map.of("unit", UnitTypes.mono, "price",   2, "name", "Mono", "tier", 1));
        units.add(Map.of("unit", UnitTypes.poly, "price",   5, "name", "Poly", "tier", 2));
        units.add(Map.of("unit", UnitTypes.mega, "price",   13, "name", "Mega", "tier", 3));
        units.add(Map.of("unit", UnitTypes.quad, "price",   27, "name", "Quad", "tier", 4));
        units.add(Map.of("unit", UnitTypes.oct, "price",   45, "name", "Oct", "tier", 5));
        units.add(Map.of("unit", UnitTypes.risso, "price",   2, "name", "Risso", "tier", 1));
        units.add(Map.of("unit", UnitTypes.minke, "price",   5, "name", "Minke", "tier", 2));
        units.add(Map.of("unit", UnitTypes.bryde, "price",   13, "name", "Bryde", "tier", 3));
        units.add(Map.of("unit", UnitTypes.sei, "price",   32, "name", "Sei", "tier", 4));
        units.add(Map.of("unit", UnitTypes.omura, "price",   52, "name", "Omura", "tier", 5));
        units.add(Map.of("unit", UnitTypes.retusa, "price",   2, "name", "Retusa", "tier", 1));
        units.add(Map.of("unit", UnitTypes.oxynoe, "price",   5, "name", "Oxynoe", "tier", 2));
        units.add(Map.of("unit", UnitTypes.cyerce, "price",   13, "name", "Cyerce", "tier", 3));
        units.add(Map.of("unit", UnitTypes.aegires, "price",   32, "name", "Aegires", "tier", 4));
        units.add(Map.of("unit", UnitTypes.navanax, "price",   52, "name", "Navanax", "tier", 5));
        units.add(Map.of("unit", UnitTypes.stell, "price",   3, "name", "Stell", "tier", 1));
        units.add(Map.of("unit", UnitTypes.locus, "price",   8, "name", "Locus", "tier", 2));
        units.add(Map.of("unit", UnitTypes.precept, "price",   18, "name", "Precept", "tier", 3));
        units.add(Map.of("unit", UnitTypes.vanquish, "price",   40, "name", "Vanquish", "tier", 4));
        units.add(Map.of("unit", UnitTypes.conquer, "price",   65, "name", "Conquer", "tier", 5));
        units.add(Map.of("unit", UnitTypes.merui, "price",   3, "name", "Merui", "tier", 1));
        units.add(Map.of("unit", UnitTypes.cleroi, "price",   9, "name", "Cleroi", "tier", 2));
        units.add(Map.of("unit", UnitTypes.anthicus, "price",   20, "name", "Anthicus", "tier", 3));
        units.add(Map.of("unit", UnitTypes.tecta, "price",   42, "name", "Tecta", "tier", 4));
        units.add(Map.of("unit", UnitTypes.collaris, "price",   65, "name", "Collaris", "tier", 5));
        units.add(Map.of("unit", UnitTypes.elude, "price",   3, "name", "Elude", "tier", 1));
        units.add(Map.of("unit", UnitTypes.avert, "price",   8, "name", "Avert", "tier", 2));
        units.add(Map.of("unit", UnitTypes.obviate, "price",   18, "name", "Obviate", "tier", 3));
        units.add(Map.of("unit", UnitTypes.quell, "price",   40, "name", "Quell", "tier", 4));
        units.add(Map.of("unit", UnitTypes.disrupt, "price",   65, "name", "Disrupt", "tier", 5));
        units.add(Map.of("unit", UnitTypes.renale, "price",   12, "name", "Renale", "tier", 6));
        units.add(Map.of("unit", UnitTypes.latum, "price",   60, "name", "Latum", "tier", 6));
    }
}