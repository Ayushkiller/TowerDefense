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
    {UnitTypes.alpha, UnitTypes.beta, UnitTypes.gamma, UnitTypes.evoke, UnitTypes.incite, UnitTypes.emanate},
    {UnitTypes.dagger, UnitTypes.mace, UnitTypes.fortress, UnitTypes.scepter, UnitTypes.reign},
    {UnitTypes.nova, UnitTypes.pulsar, UnitTypes.quasar, UnitTypes.vela,UnitTypes.corvus},
    {UnitTypes.crawler, UnitTypes.atrax, UnitTypes.spiroct, UnitTypes.arkyid, UnitTypes.toxopid},
    {UnitTypes.flare, UnitTypes.horizon, UnitTypes.zenith, UnitTypes.antumbra, UnitTypes.eclipse},
    {UnitTypes.mono, UnitTypes.poly, UnitTypes.mega, UnitTypes.quad, UnitTypes.oct},
    {UnitTypes.risso, UnitTypes.minke, UnitTypes.bryde, UnitTypes.sei, UnitTypes.omura},
    {UnitTypes.retusa, UnitTypes.oxynoe, UnitTypes.cyerce, UnitTypes.aegires, UnitTypes.navanax},
    {UnitTypes.stell, UnitTypes.locus, UnitTypes.precept, UnitTypes.vanquish, UnitTypes.conquer},
    {UnitTypes.merui, UnitTypes.cleroi, UnitTypes.anthicus, UnitTypes.tecta, UnitTypes.collaris},
    {UnitTypes.elude, UnitTypes.avert, UnitTypes.obviate, UnitTypes.quell, UnitTypes.disrupt},
    {UnitTypes.renale, UnitTypes.latum}
    };

    public static Integer[][] prices = {
        {4,  5,  6,   8,   9,   10},
        {1,  4,  12,  30,  50},
        {1,  4,  13,  32,  52},
        {1,  3,  12,  30,  50},
        {1,  3,  10,  27,  48},
        {2,  5,  13,  27,  45},
        {2,  5,  13,  32,  52},
        {2,  5,  13,  32,  52},
        {3,  8,  18,  40,  65},
        {3,  9,  20,  42,  65},
        {3,  8,  18,  40,  65},
        {12, 60}
    };
}