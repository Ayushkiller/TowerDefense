package tower.Domain;

import java.util.HashMap;
import java.util.Map;

import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;

public class Unitsdrops {
        public static Map<UnitType, Seq<ItemStack>> drops = new HashMap<>();

        static {
                addDrop(UnitTypes.crawler,
                                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.silicon, 5)));
                addDrop(UnitTypes.atrax, Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.graphite, 10,
                                Items.titanium, 5)));
                addDrop(UnitTypes.spiroct, Seq.with(ItemStack.list(Items.lead, 100, Items.graphite, 40, Items.silicon,
                                40, Items.thorium, 10)));
                addDrop(UnitTypes.arkyid, Seq.with(ItemStack.list(Items.copper, 300, Items.graphite, 80,
                                Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)));
                addDrop(UnitTypes.toxopid,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120,
                                                Items.silicon, 120, Items.thorium, 40, Items.plastanium, 40,
                                                Items.surgeAlloy, 15, Items.phaseFabric, 5)));
                addDrop(UnitTypes.dagger, Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.silicon, 5)));
                addDrop(UnitTypes.mace, Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.graphite, 10,
                                Items.titanium, 5)));
                addDrop(UnitTypes.fortress, Seq.with(ItemStack.list(Items.lead, 100, Items.graphite, 40, Items.silicon,
                                40, Items.thorium, 10)));
                addDrop(UnitTypes.scepter, Seq.with(ItemStack.list(Items.copper, 300, Items.silicon, 80,
                                Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)));
                addDrop(UnitTypes.reign,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120,
                                                Items.silicon, 120, Items.thorium, 40, Items.plastanium, 40,
                                                Items.surgeAlloy, 15, Items.phaseFabric, 5)));
                addDrop(UnitTypes.nova, Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.metaglass, 3)));
                addDrop(UnitTypes.pulsar,
                                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.metaglass, 10)));
                addDrop(UnitTypes.quasar, Seq.with(ItemStack.list(Items.lead, 100, Items.metaglass, 40, Items.silicon,
                                40, Items.titanium, 30, Items.thorium, 10)));
                addDrop(UnitTypes.vela, Seq.with(ItemStack.list(Items.copper, 300, Items.metaglass, 80, Items.graphite,
                                80, Items.titanium, 60, Items.plastanium, 20, Items.surgeAlloy, 5)));
                addDrop(UnitTypes.corvus,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 100,
                                                Items.silicon, 100, Items.metaglass, 120, Items.titanium, 120,
                                                Items.thorium, 60, Items.surgeAlloy, 10, Items.phaseFabric, 10)));
                addDrop(UnitTypes.flare, Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.graphite, 3)));
                addDrop(UnitTypes.horizon,
                                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.graphite, 10)));
                addDrop(UnitTypes.zenith, Seq.with(ItemStack.list(Items.lead, 100, Items.graphite, 40, Items.silicon,
                                40, Items.titanium, 30, Items.plastanium, 10)));
                addDrop(UnitTypes.antumbra, Seq.with(ItemStack.list(Items.copper, 300, Items.graphite, 80,
                                Items.metaglass, 80, Items.titanium, 60, Items.surgeAlloy, 15)));
                addDrop(UnitTypes.eclipse,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120,
                                                Items.silicon, 120, Items.thorium, 120, Items.plastanium, 40,
                                                Items.surgeAlloy, 5, Items.phaseFabric, 10)));
                addDrop(UnitTypes.mono, Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.silicon, 5)));
                addDrop(UnitTypes.poly, Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.silicon, 10,
                                Items.titanium, 5)));
                addDrop(UnitTypes.mega, Seq.with(ItemStack.list(Items.lead, 100, Items.silicon, 40, Items.graphite, 40,
                                Items.thorium, 10)));
                addDrop(UnitTypes.quad, Seq.with(ItemStack.list(Items.copper, 300, Items.silicon, 80, Items.metaglass,
                                80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)));
                addDrop(UnitTypes.oct,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120,
                                                Items.silicon, 120, Items.thorium, 40, Items.plastanium, 40,
                                                Items.surgeAlloy, 15, Items.phaseFabric, 5)));
                addDrop(UnitTypes.risso,
                                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.metaglass, 3)));
                addDrop(UnitTypes.minke,
                                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.metaglass, 10)));
                addDrop(UnitTypes.bryde, Seq.with(ItemStack.list(Items.lead, 100, Items.metaglass, 40, Items.silicon,
                                40, Items.titanium, 80, Items.thorium, 10)));
                addDrop(UnitTypes.sei, Seq.with(ItemStack.list(Items.copper, 300, Items.metaglass, 80, Items.graphite,
                                80, Items.titanium, 60, Items.plastanium, 20, Items.surgeAlloy, 5)));
                addDrop(UnitTypes.omura,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 100,
                                                Items.silicon, 100, Items.metaglass, 120, Items.titanium, 120,
                                                Items.thorium, 60, Items.surgeAlloy, 10, Items.phaseFabric, 10)));
                addDrop(UnitTypes.retusa,
                                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.graphite, 3)));
                addDrop(UnitTypes.oxynoe,
                                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.graphite, 10)));
                addDrop(UnitTypes.cyerce, Seq.with(ItemStack.list(Items.lead, 100, Items.graphite, 40, Items.silicon,
                                40, Items.titanium, 30, Items.plastanium, 10)));
                addDrop(UnitTypes.aegires, Seq.with(ItemStack.list(Items.copper, 300, Items.graphite, 80,
                                Items.metaglass, 80, Items.titanium, 60, Items.surgeAlloy, 15)));
                addDrop(UnitTypes.navanax,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120,
                                                Items.silicon, 120, Items.thorium, 120, Items.plastanium, 40,
                                                Items.surgeAlloy, 5, Items.phaseFabric, 10)));
                addDrop(UnitTypes.alpha, Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.silicon, 5)));
                addDrop(UnitTypes.beta, Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.silicon, 10,
                                Items.titanium, 5)));
                addDrop(UnitTypes.gamma, Seq.with(ItemStack.list(Items.lead, 100, Items.silicon, 40, Items.graphite, 40,
                                Items.thorium, 10)));
                addDrop(UnitTypes.obviate, Seq.with(ItemStack.list(Items.copper, 300, Items.silicon, 80,
                                Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)));
                addDrop(UnitTypes.quell,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120,
                                                Items.silicon, 120, Items.thorium, 40, Items.plastanium, 40,
                                                Items.surgeAlloy, 15, Items.phaseFabric, 5)));
                addDrop(UnitTypes.disrupt,
                                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 15, Items.silicon, 5)));
                addDrop(UnitTypes.locus, Seq.with(ItemStack.list(Items.copper, 300, Items.graphite, 80, Items.metaglass,
                                80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)));
                addDrop(UnitTypes.pulsar,
                                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120,
                                                Items.silicon, 120, Items.thorium, 40, Items.plastanium, 40,
                                                Items.surgeAlloy, 15, Items.phaseFabric, 5)));
                addDrop(UnitTypes.pulsar, Seq.with(ItemStack.list(Items.copper, 20, Items.beryllium, 25)));
                addDrop(UnitTypes.quasar, Seq.with(ItemStack.list(Items.beryllium, 20, Items.graphite, 20,
                                Items.silicon, 20, Items.tungsten, 15)));
                addDrop(UnitTypes.vela, Seq.with(ItemStack.list(Items.beryllium, 40, Items.graphite, 40, Items.silicon,
                                40, Items.tungsten, 40, Items.oxide, 15)));
                addDrop(UnitTypes.antumbra, Seq.with(ItemStack.list(Items.beryllium, 120, Items.graphite, 100,
                                Items.silicon, 100, Items.tungsten, 80, Items.oxide, 40, Items.carbide, 20)));
                addDrop(UnitTypes.eclipse,
                                Seq.with(ItemStack.list(Items.beryllium, 300, Items.graphite, 300, Items.silicon, 300,
                                                Items.tungsten, 200, Items.thorium, 100, Items.oxide, 80, Items.carbide,
                                                40, Items.surgeAlloy, 10, Items.phaseFabric, 10)));
                addDrop(UnitTypes.merui, Seq.with(ItemStack.list(Items.beryllium, 20, Items.silicon, 25)));
                addDrop(UnitTypes.cleroi, Seq.with(ItemStack.list(Items.beryllium, 20, Items.graphite, 20,
                                Items.silicon, 20, Items.tungsten, 15)));
                addDrop(UnitTypes.anthicus, Seq.with(ItemStack.list(Items.beryllium, 40, Items.graphite, 40,
                                Items.silicon, 40, Items.tungsten, 40, Items.oxide, 15)));
                addDrop(UnitTypes.tecta, Seq.with(ItemStack.list(Items.beryllium, 120, Items.graphite, 100,
                                Items.silicon, 100, Items.tungsten, 80, Items.oxide, 40, Items.carbide, 20)));
                addDrop(UnitTypes.collaris,
                                Seq.with(ItemStack.list(Items.beryllium, 300, Items.graphite, 300, Items.silicon, 300,
                                                Items.tungsten, 200, Items.thorium, 100, Items.oxide, 80, Items.carbide,
                                                40, Items.surgeAlloy, 10, Items.phaseFabric, 10)));
                addDrop(UnitTypes.elude, Seq.with(ItemStack.list(Items.beryllium, 20, Items.silicon, 25)));
                addDrop(UnitTypes.avert, Seq.with(ItemStack.list(Items.beryllium, 20, Items.graphite, 20, Items.silicon,
                                20, Items.tungsten, 15)));
                addDrop(UnitTypes.obviate, Seq.with(ItemStack.list(Items.beryllium, 40, Items.graphite, 40,
                                Items.silicon, 40, Items.tungsten, 40, Items.oxide, 15)));
                addDrop(UnitTypes.quell, Seq.with(ItemStack.list(Items.beryllium, 120, Items.graphite, 100,
                                Items.silicon, 100, Items.tungsten, 80, Items.oxide, 40, Items.carbide, 20)));
                addDrop(UnitTypes.disrupt,
                                Seq.with(ItemStack.list(Items.beryllium, 300, Items.graphite, 300, Items.silicon, 300,
                                                Items.tungsten, 200, Items.thorium, 100, Items.oxide, 80, Items.carbide,
                                                40, Items.surgeAlloy, 10, Items.phaseFabric, 10)));
                addDrop(UnitTypes.latum, Seq.with(ItemStack.list(Items.beryllium, 20, Items.graphite, 20, Items.silicon,
                                20, Items.tungsten, 15)));
                addDrop(UnitTypes.vela, Seq.with(ItemStack.list(Items.beryllium, 40, Items.graphite, 40, Items.silicon,
                                40, Items.tungsten, 40, Items.oxide, 15)));
                addDrop(UnitTypes.antumbra, Seq.with(ItemStack.list(Items.beryllium, 120, Items.graphite, 100,
                                Items.silicon, 100, Items.tungsten, 80, Items.oxide, 40, Items.carbide, 20)));
                addDrop(UnitTypes.eclipse,
                                Seq.with(ItemStack.list(Items.beryllium, 300, Items.graphite, 300, Items.silicon, 300,
                                                Items.tungsten, 200, Items.thorium, 100, Items.oxide, 80, Items.carbide,
                                                40, Items.surgeAlloy, 10, Items.phaseFabric, 10)));
                addDrop(UnitTypes.risso, Seq.with(ItemStack.list(Items.beryllium, 20, Items.graphite, 25)));
                addDrop(UnitTypes.minke, Seq.with(ItemStack.list(Items.beryllium, 20, Items.graphite, 20, Items.silicon,
                                20, Items.tungsten, 15)));
                addDrop(UnitTypes.bryde, Seq.with(ItemStack.list(Items.beryllium, 40, Items.graphite, 40, Items.silicon,
                                40, Items.tungsten, 40, Items.oxide, 15)));
                addDrop(UnitTypes.sei, Seq.with(ItemStack.list(Items.beryllium, 120, Items.graphite, 100, Items.silicon,
                                100, Items.tungsten, 80, Items.oxide, 40, Items.carbide, 20)));
                addDrop(UnitTypes.omura,
                                Seq.with(ItemStack.list(Items.beryllium, 300, Items.graphite, 300, Items.silicon, 300,
                                                Items.tungsten, 200, Items.thorium, 100, Items.oxide, 80, Items.carbide,
                                                40, Items.surgeAlloy, 10, Items.phaseFabric, 10)));
        }

        private static void addDrop(UnitType unit, Seq<ItemStack> stacks) {
                drops.put(unit, stacks);
        }
}
