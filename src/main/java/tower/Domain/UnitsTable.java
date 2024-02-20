package tower.Domain;

import mindustry.content.UnitTypes;
import mindustry.type.UnitType;

public class UnitsTable {
    public static UnitType[][] units = {
    // Tier   0
    {UnitTypes.alpha, UnitTypes.beta, UnitTypes.gamma, UnitTypes.emanate, UnitTypes.evoke, UnitTypes.incite},
    // Tier   1
    {UnitTypes.mono, UnitTypes.dagger, UnitTypes.flare, UnitTypes.crawler, UnitTypes.nova, UnitTypes.risso},
    { UnitTypes.retusa, UnitTypes.stell, UnitTypes.elude, UnitTypes.merui,UnitTypes.poly, UnitTypes.mace},
    // Tier   2
    {UnitTypes.horizon, UnitTypes.atrax, UnitTypes.pulsar, UnitTypes.minke, UnitTypes.oxynoe, UnitTypes.locus},
    { UnitTypes.avert, UnitTypes.cleroi,UnitTypes.bryde, UnitTypes.cyerce, UnitTypes.precept, UnitTypes.anthicus},
    // Tier   3
    {UnitTypes.fortress, UnitTypes.quasar, UnitTypes.spiroct, UnitTypes.zenith, UnitTypes.mega,UnitTypes.obviate},
    // Tier   4
    {UnitTypes.scepter, UnitTypes.aegires, UnitTypes.sei, UnitTypes.quad, UnitTypes.antumbra, UnitTypes.arkyid},
    {UnitTypes.vela, UnitTypes.scepter, UnitTypes.vanquish, UnitTypes.tecta, UnitTypes.quell,UnitTypes.conquer},
    // Tier   5
    { UnitTypes.collaris, UnitTypes.disrupt, UnitTypes.reign, UnitTypes.oct, UnitTypes.eclipse, UnitTypes.corvus},
    {UnitTypes.toxopid, UnitTypes.omura, UnitTypes.navanax,UnitTypes.renale,UnitTypes.latum,UnitTypes.assemblyDrone}
    };
    // these are prices of units match it with unitype to change values
    
    public static Integer[][] prices = {
        // Tier   0
        {3,  5,  7,  9,  11,  13},
        // Tier   1
        {3,  5,  7,  9,  11,  13},
        // Tier   2
        {3,  5,  7,  9,  11,  13},
        // Tier   3
        {3,  5,  7,  9,  11,  13},
        // Tier   4
        {3,  5,  7,  9,  11,  13},
        // Tier   5
        {3,  5,  7,  9,  11,  13},
        {3,  5,  7,  9,  11,  13},
        {3,  5,  7,  9,  11,  13},
        {3,  5,  7,  9,  11,  13},
        {3,  5,  7,  9,  11,  13}

    };
}