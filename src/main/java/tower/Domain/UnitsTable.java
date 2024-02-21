package tower.Domain;

import mindustry.content.UnitTypes;
import mindustry.type.UnitType;
/**
 * This class defines the structure of the units table for the tower defense game.
 * It contains arrays of unit types and their corresponding prices for each tier.
 */

public class UnitsTable {
        /**
     * A two-dimensional array representing the units available at each tier of the game.
     * Each element in the array is a UnitType object representing a unit that can be purchased.
     */
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
        /**
     * A two-dimensional array representing the prices of units at each tier of the game.
     * Each element in the array is an Integer object representing the cost of a unit.
     */
    public static Integer[][] prices = {
        // Tier   0
        {10,  10,   10,   10,   10,   10},
        // Tier   1
        {13,  15,   17,   19,   21,   23},
        // Tier   2
        {23,  25,   27,   29,   31,   33},
        // Tier   3
        {33,  35,   37,   39,   41,   43},
        // Tier   4
        {43,  45,   47,   49,   51,   53},
        // Tier   5
        {53,  55,   57,   59,   61,   63},
        {63,  65,   67,   69,   71,   73},
        {73,  75,   77,   79,   81,   83},
        {83,  85,   87,   89,   91,   93},
        {93,  95,   97,   99,   101,   103}
    };
};
