package tower.Domain;

import java.util.HashMap;

import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;

public class Unitsdrops {
    public static HashMap<UnitType, Seq<ItemStack>> drops = new HashMap<>();

    static {
        addDrop(UnitTypes.crawler, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.silicon, 5)));
        addDrop(UnitTypes.atrax, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.graphite, 10), new ItemStack(Items.titanium, 5)));
        addDrop(UnitTypes.spiroct, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.thorium, 10)));
        addDrop(UnitTypes.arkyid, Seq.with(new ItemStack(Items.copper, 300), new ItemStack(Items.graphite, 80), new ItemStack(Items.metaglass, 80), new ItemStack(Items.titanium, 80), new ItemStack(Items.thorium, 20), new ItemStack(Items.phaseFabric, 10)));
        addDrop(UnitTypes.toxopid, Seq.with(new ItemStack(Items.copper, 400), new ItemStack(Items.lead, 400), new ItemStack(Items.graphite, 120), new ItemStack(Items.silicon, 120), new ItemStack(Items.thorium, 40), new ItemStack(Items.plastanium, 40), new ItemStack(Items.surgeAlloy, 15), new ItemStack(Items.phaseFabric, 5)));
        addDrop(UnitTypes.dagger, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.silicon, 5)));
        addDrop(UnitTypes.mace, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.graphite, 10), new ItemStack(Items.titanium, 5)));
        addDrop(UnitTypes.fortress, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.thorium, 10)));
        addDrop(UnitTypes.scepter, Seq.with(new ItemStack(Items.copper, 300), new ItemStack(Items.silicon, 80), new ItemStack(Items.metaglass, 80), new ItemStack(Items.titanium, 80), new ItemStack(Items.thorium, 20), new ItemStack(Items.phaseFabric, 10)));
        addDrop(UnitTypes.reign, Seq.with(new ItemStack(Items.copper, 400), new ItemStack(Items.lead, 400), new ItemStack(Items.graphite, 120), new ItemStack(Items.silicon, 120), new ItemStack(Items.thorium, 40), new ItemStack(Items.plastanium, 40), new ItemStack(Items.surgeAlloy, 15), new ItemStack(Items.phaseFabric, 5)));
        addDrop(UnitTypes.nova, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.metaglass, 3)));
        addDrop(UnitTypes.pulsar, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.metaglass, 10)));
        addDrop(UnitTypes.quasar, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.metaglass, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.titanium, 30), new ItemStack(Items.thorium, 10)));
        addDrop(UnitTypes.vela, Seq.with(new ItemStack(Items.copper, 300), new ItemStack(Items.metaglass, 80), new ItemStack(Items.graphite, 80), new ItemStack(Items.titanium, 60), new ItemStack(Items.plastanium, 20), new ItemStack(Items.surgeAlloy, 5)));
        addDrop(UnitTypes.corvus, Seq.with(new ItemStack(Items.copper, 400), new ItemStack(Items.lead, 400), new ItemStack(Items.graphite, 100), new ItemStack(Items.silicon, 100), new ItemStack(Items.metaglass, 120), new ItemStack(Items.titanium, 120), new ItemStack(Items.thorium, 60), new ItemStack(Items.surgeAlloy, 10), new ItemStack(Items.phaseFabric, 10)));
        addDrop(UnitTypes.flare, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.graphite, 3)));
        addDrop(UnitTypes.horizon, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.graphite, 10)));
        addDrop(UnitTypes.zenith, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.titanium, 30), new ItemStack(Items.plastanium, 10)));
        addDrop(UnitTypes.antumbra, Seq.with(new ItemStack(Items.copper, 300), new ItemStack(Items.graphite, 80), new ItemStack(Items.metaglass, 80), new ItemStack(Items.titanium, 60), new ItemStack(Items.surgeAlloy, 15)));
        addDrop(UnitTypes.eclipse, Seq.with(new ItemStack(Items.copper, 400), new ItemStack(Items.lead, 400), new ItemStack(Items.graphite, 120), new ItemStack(Items.silicon, 120), new ItemStack(Items.thorium, 120), new ItemStack(Items.plastanium, 40), new ItemStack(Items.surgeAlloy, 5), new ItemStack(Items.phaseFabric, 10)));
        addDrop(UnitTypes.mono, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.silicon, 5)));
        addDrop(UnitTypes.poly, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.silicon, 10), new ItemStack(Items.titanium, 5)));
        addDrop(UnitTypes.mega, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.silicon, 40), new ItemStack(Items.graphite, 40), new ItemStack(Items.thorium, 10)));
        addDrop(UnitTypes.quad, Seq.with(new ItemStack(Items.copper, 300), new ItemStack(Items.silicon, 80), new ItemStack(Items.metaglass, 80), new ItemStack(Items.titanium, 80), new ItemStack(Items.thorium, 20), new ItemStack(Items.phaseFabric, 10)));
        addDrop(UnitTypes.oct, Seq.with(new ItemStack(Items.copper, 400), new ItemStack(Items.lead, 400), new ItemStack(Items.graphite, 120), new ItemStack(Items.silicon, 120), new ItemStack(Items.thorium, 40), new ItemStack(Items.plastanium, 40), new ItemStack(Items.surgeAlloy, 15), new ItemStack(Items.phaseFabric, 5)));
        addDrop(UnitTypes.risso, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.metaglass, 3)));
        addDrop(UnitTypes.minke, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.metaglass, 10)));
        addDrop(UnitTypes.bryde, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.metaglass, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.titanium, 80), new ItemStack(Items.thorium, 10)));
        addDrop(UnitTypes.sei, Seq.with(new ItemStack(Items.copper, 300), new ItemStack(Items.metaglass, 80), new ItemStack(Items.graphite, 80), new ItemStack(Items.titanium, 60), new ItemStack(Items.plastanium, 20), new ItemStack(Items.surgeAlloy, 5)));
        addDrop(UnitTypes.omura, Seq.with(new ItemStack(Items.copper, 400), new ItemStack(Items.lead, 400), new ItemStack(Items.graphite, 100), new ItemStack(Items.silicon, 100), new ItemStack(Items.metaglass, 120), new ItemStack(Items.titanium, 120), new ItemStack(Items.thorium, 60), new ItemStack(Items.surgeAlloy, 10), new ItemStack(Items.phaseFabric, 10)));
        addDrop(UnitTypes.retusa, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.graphite, 3)));
        addDrop(UnitTypes.oxynoe, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.graphite, 10)));
        addDrop(UnitTypes.cyerce, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.titanium, 30), new ItemStack(Items.plastanium, 10)));
        addDrop(UnitTypes.aegires, Seq.with(new ItemStack(Items.copper, 300), new ItemStack(Items.graphite, 80), new ItemStack(Items.metaglass, 80), new ItemStack(Items.titanium, 60), new ItemStack(Items.surgeAlloy, 15)));
        addDrop(UnitTypes.navanax, Seq.with(new ItemStack(Items.copper, 400), new ItemStack(Items.lead, 400), new ItemStack(Items.graphite, 120), new ItemStack(Items.silicon, 120), new ItemStack(Items.thorium, 120), new ItemStack(Items.plastanium, 40), new ItemStack(Items.surgeAlloy, 5), new ItemStack(Items.phaseFabric, 10)));
        addDrop(UnitTypes.alpha, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.silicon, 5)));
        addDrop(UnitTypes.beta, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.silicon, 10), new ItemStack(Items.titanium, 5)));
        addDrop(UnitTypes.gamma, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.silicon, 40), new ItemStack(Items.graphite, 40), new ItemStack(Items.thorium, 10)));

        // Erekir units
        addDrop(UnitTypes.elude, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.silicon, 25)));
        addDrop(UnitTypes.avert, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.graphite, 20), new ItemStack(Items.silicon, 20), new ItemStack(Items.tungsten, 15)));
        addDrop(UnitTypes.obviate, Seq.with(new ItemStack(Items.beryllium, 40), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.tungsten, 40), new ItemStack(Items.oxide, 15)));
        addDrop(UnitTypes.quell, Seq.with(new ItemStack(Items.beryllium, 120), new ItemStack(Items.graphite, 100), new ItemStack(Items.silicon, 100), new ItemStack(Items.tungsten, 80), new ItemStack(Items.oxide, 40), new ItemStack(Items.carbide, 20)));
        addDrop(UnitTypes.disrupt, Seq.with(new ItemStack(Items.beryllium, 300), new ItemStack(Items.graphite, 300), new ItemStack(Items.silicon, 300), new ItemStack(Items.tungsten, 200), new ItemStack(Items.thorium, 100), new ItemStack(Items.oxide, 80), new ItemStack(Items.carbide, 40), new ItemStack(Items.surgeAlloy, 10), new ItemStack(Items.phaseFabric, 10)));

        addDrop(UnitTypes.merui, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.silicon, 25)));
        addDrop(UnitTypes.cleroi, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.graphite, 20), new ItemStack(Items.silicon, 20), new ItemStack(Items.tungsten, 15)));
        addDrop(UnitTypes.anthicus, Seq.with(new ItemStack(Items.beryllium, 40), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.tungsten, 40), new ItemStack(Items.oxide, 15)));
        addDrop(UnitTypes.tecta, Seq.with(new ItemStack(Items.beryllium, 120), new ItemStack(Items.graphite, 100), new ItemStack(Items.silicon, 100), new ItemStack(Items.tungsten, 80), new ItemStack(Items.oxide, 40), new ItemStack(Items.carbide, 20)));
        addDrop(UnitTypes.collaris, Seq.with(new ItemStack(Items.beryllium, 300), new ItemStack(Items.graphite, 300), new ItemStack(Items.silicon, 300), new ItemStack(Items.tungsten, 200), new ItemStack(Items.thorium, 100), new ItemStack(Items.oxide, 80), new ItemStack(Items.carbide, 40), new ItemStack(Items.surgeAlloy, 10), new ItemStack(Items.phaseFabric, 10)));

        addDrop(UnitTypes.latum, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.graphite, 20), new ItemStack(Items.silicon, 20), new ItemStack(Items.tungsten, 15)));
        // Naval Erekir units
        addDrop(UnitTypes.stell, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.graphite, 25)));
        addDrop(UnitTypes.locus, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.graphite, 20), new ItemStack(Items.silicon, 20), new ItemStack(Items.tungsten, 15)));
        addDrop(UnitTypes.precept, Seq.with(new ItemStack(Items.beryllium, 40), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.tungsten, 40), new ItemStack(Items.oxide, 15)));
        addDrop(UnitTypes.vanquish, Seq.with(new ItemStack(Items.beryllium, 120), new ItemStack(Items.graphite, 100), new ItemStack(Items.silicon, 100), new ItemStack(Items.tungsten, 80), new ItemStack(Items.oxide, 40), new ItemStack(Items.carbide, 20)));
        addDrop(UnitTypes.conquer, Seq.with(new ItemStack(Items.beryllium, 300), new ItemStack(Items.graphite, 300), new ItemStack(Items.silicon, 300), new ItemStack(Items.tungsten, 200), new ItemStack(Items.thorium, 100), new ItemStack(Items.oxide, 80), new ItemStack(Items.carbide, 40), new ItemStack(Items.surgeAlloy, 10), new ItemStack(Items.phaseFabric, 10)));

        // Air Erekir units
        addDrop(UnitTypes.elude,
         Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.silicon, 25)));
        addDrop(UnitTypes.avert, Seq.with(new ItemStack(Items.beryllium, 20), new ItemStack(Items.graphite, 20), new ItemStack(Items.silicon, 20), new ItemStack(Items.tungsten, 15)));
        addDrop(UnitTypes.obviate, Seq.with(new ItemStack(Items.beryllium, 40), new ItemStack(Items.graphite, 40), new ItemStack(Items.silicon, 40), new ItemStack(Items.tungsten, 40), new ItemStack(Items.oxide, 15)));
        addDrop(UnitTypes.quell, Seq.with(new ItemStack(Items.beryllium, 120), new ItemStack(Items.graphite, 100), new ItemStack(Items.silicon, 100), new ItemStack(Items.tungsten, 80), new ItemStack(Items.oxide, 40), new ItemStack(Items.carbide, 20)));
        addDrop(UnitTypes.disrupt, Seq.with(new ItemStack(Items.beryllium, 300), new ItemStack(Items.graphite, 300), new ItemStack(Items.silicon, 300), new ItemStack(Items.tungsten, 200), new ItemStack(Items.thorium, 100), new ItemStack(Items.oxide, 80), new ItemStack(Items.carbide, 40), new ItemStack(Items.surgeAlloy, 10), new ItemStack(Items.phaseFabric, 10)));

        // Core units
        addDrop(UnitTypes.alpha, Seq.with(new ItemStack(Items.copper, 20), new ItemStack(Items.lead, 15), new ItemStack(Items.silicon, 5)));
        addDrop(UnitTypes.beta, Seq.with(new ItemStack(Items.copper, 30), new ItemStack(Items.lead, 40), new ItemStack(Items.silicon, 10), new ItemStack(Items.titanium, 5)));
        addDrop(UnitTypes.gamma, Seq.with(new ItemStack(Items.lead, 100), new ItemStack(Items.silicon, 40), new ItemStack(Items.graphite, 40), new ItemStack(Items.thorium, 10)));
    }

    private static void addDrop(UnitType unit, Seq<ItemStack> stacks) {
        drops.put(unit, stacks);
    }
}