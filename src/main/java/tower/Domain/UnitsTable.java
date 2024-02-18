package tower.Domain;

import mindustry.content.UnitTypes;
import mindustry.type.UnitType;

public class UnitsTable {
    public static UnitType[][] units = {
            // Tier 1
            { UnitTypes.nova, UnitTypes.dagger, UnitTypes.merui}
            , // Tier 2
            {UnitTypes.pulsar, UnitTypes.stell, UnitTypes.cleroi}
            , // Tier 3
            {UnitTypes.poly, UnitTypes.fortress, UnitTypes.spiroct}
            , // Tier 4
            {UnitTypes.quasar, UnitTypes.precept, UnitTypes.tecta}
            , // Tier 5
            {UnitTypes.vela, UnitTypes.scepter, UnitTypes.arkyid}
            , // Tier 6
            { UnitTypes.anthicus, UnitTypes.vanquish,  UnitTypes.quell}
            // Tier 7
    };

    public static Integer[][] prices = {
            {3, 1, 10}
            ,
            {25, 10, 30}
            ,
            {20, 30, 25}
            ,
            {35, 40, 325}
            ,
            {165, 185, 250}
            ,
            {600, 255, 1200}
    };
}