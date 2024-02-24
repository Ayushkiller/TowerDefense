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
    {UnitTypes.alpha, UnitTypes.beta, UnitTypes.gamma, UnitTypes.emanate, UnitTypes.evoke, UnitTypes.incite},
    {UnitTypes.mono, UnitTypes.dagger, UnitTypes.flare, UnitTypes.crawler, UnitTypes.nova, UnitTypes.risso},
    {UnitTypes.retusa, UnitTypes.stell, UnitTypes.elude, UnitTypes.merui,UnitTypes.poly, UnitTypes.mace},
    {UnitTypes.horizon, UnitTypes.atrax, UnitTypes.pulsar, UnitTypes.minke, UnitTypes.oxynoe, UnitTypes.locus},
    {UnitTypes.avert, UnitTypes.cleroi,UnitTypes.bryde, UnitTypes.cyerce, UnitTypes.precept, UnitTypes.anthicus},
    {UnitTypes.fortress, UnitTypes.quasar, UnitTypes.spiroct, UnitTypes.zenith, UnitTypes.mega,UnitTypes.obviate},
    {UnitTypes.scepter, UnitTypes.aegires, UnitTypes.sei, UnitTypes.quad, UnitTypes.antumbra, UnitTypes.arkyid},
    {UnitTypes.vela, UnitTypes.scepter, UnitTypes.vanquish, UnitTypes.tecta, UnitTypes.quell,UnitTypes.conquer},
    {UnitTypes.collaris, UnitTypes.disrupt, UnitTypes.reign, UnitTypes.oct, UnitTypes.eclipse, UnitTypes.corvus},
    {UnitTypes.toxopid, UnitTypes.omura, UnitTypes.navanax,UnitTypes.renale,UnitTypes.latum,UnitTypes.assemblyDrone}
    };

    public static Integer[][] prices = {
        {10,  10,   10,   10,   10,   10},
        {13,  15,   17,   19,   21,   23},
        {23,  25,   27,   29,   31,   33},
        {33,  35,   37,   39,   41,   43},
        {43,  45,   47,   49,   51,   53},
        {53,  55,   57,   59,   61,   63},
        {63,  65,   67,   69,   71,   73},
        {73,  75,   77,   79,   81,   83},
        {83,  85,   87,   89,   91,   93},
        {93,  95,   97,   99,   101,   103}
    };

    public static int getPriceForUnitType(int unitTypeId) {
        // Determine the tier and position within the tier based on the unitTypeId
        int tier = unitTypeId / prices[0].length;
        int position = unitTypeId % prices[0].length;
    
        // Check if the tier and position are within the bounds of the prices array
        if (tier >=   0 && tier < prices.length && position >=   0 && position < prices[tier].length) {
            // Return the price for the unit type within the specified tier and position
            return prices[tier][position];
        } else {
            // Handle the case where the tier or position is out of bounds
            throw new IllegalArgumentException("Unit type ID out of bounds: " + unitTypeId);
        }
    }
}