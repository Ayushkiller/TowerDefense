package tower.Domain;

import mindustry.content.UnitTypes;
import mindustry.type.UnitType;

public class UnitsTable {
    public static UnitType[][] units = {
            // Tier 0
            {UnitTypes.alpha,UnitTypes.beta,UnitTypes.gamma,UnitTypes.emanate,UnitTypes.evoke,UnitTypes.incite},
            // Tier 1 : mono,dagger,flare,crawler,nova,risso,retusa,stell,elude,merui
            {UnitTypes.mono,UnitTypes.dagger,UnitTypes.flare,UnitTypes.crawler,UnitTypes.nova,UnitTypes.risso,UnitTypes.retusa,UnitTypes.stell,UnitTypes.elude,UnitTypes.merui},
            // Tier 2 : poly,mace,horizon,atrax,,pulsar,minke,oxynoe,locus,avert,cleroi
            {UnitTypes.poly,UnitTypes.mace,UnitTypes.horizon,UnitTypes.atrax,UnitTypes.pulsar,UnitTypes.minke,UnitTypes.oxynoe,UnitTypes.locus,UnitTypes.avert,UnitTypes.cleroi},
            // Tier 3 : Fortress,Quasar,Spiroct,Zenith,Mega,Bryde,Cyerce,percept,anthicus,obliviate
            {UnitTypes.fortress,UnitTypes.quasar,UnitTypes.spiroct,UnitTypes.zenith,UnitTypes.mega,UnitTypes.bryde,UnitTypes.cyerce,UnitTypes.precept,UnitTypes.anthicus,UnitTypes.obviate},
            // Tier 4 : Scepter,Aegires,Sei,Quad,Antumbra,Arkyid,Vela,Scepter,vanquish,tecta,quell
            {UnitTypes.scepter,UnitTypes.aegires,UnitTypes.sei,UnitTypes.quad,UnitTypes.antumbra,UnitTypes.arkyid,UnitTypes.vela,UnitTypes.scepter,UnitTypes.vanquish,UnitTypes.tecta,UnitTypes.quell},
            // Tier 5 : Conquer,Collaris,Disrupt,Reign,Oct,Eclipse,Corvus,Toxopid,Omura,Navanax
            {UnitTypes.conquer,UnitTypes.collaris,UnitTypes.disrupt,UnitTypes.reign,UnitTypes.oct,UnitTypes.eclipse,UnitTypes.corvus,UnitTypes.toxopid,UnitTypes.omura,UnitTypes.navanax},
    };

    public static Integer[][] prices = {
        // Tier  0
        {3,   3,   3,   3,   3,   3},
        // Tier  1
        {5,   5,   5,   5,   5,   5,   5,   5,   5,   5},
        // Tier  2
        {7,   7,   7,   7,   7,   7,   7,   7,   7,   7},
        // Tier  3
        {9,   9,   9,   9,   9,   9,   9,   9,   9,   9},
        // Tier  4
        {11,   11,   11,   11,   11,   11,   11,   11,   11,   11},
        // Tier  5
        {13,   13,   13,   13,   13,   13,   13,   13,   13,   13}
    };
    
}