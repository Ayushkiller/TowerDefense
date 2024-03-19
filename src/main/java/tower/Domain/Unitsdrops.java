package tower.Domain;

import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.type.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import arc.struct.Seq;

public class Unitsdrops {
    public static List<Map<String, Object>> drops = new ArrayList<>();

    static {
        drops.add(Map.of("unit", UnitTypes.crawler, "drops",
                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 10, Items.silicon, 3))));
        drops.add(Map.of("unit", UnitTypes.atrax, "drops",
                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.graphite, 10, Items.titanium, 5))));
        drops.add(Map.of("unit", UnitTypes.spiroct, "drops",
                Seq.with(ItemStack.list(Items.lead, 100, Items.graphite, 40, Items.silicon, 40, Items.thorium, 10))));
        drops.add(Map.of("unit", UnitTypes.arkyid, "drops", Seq.with(ItemStack.list(Items.copper, 300, Items.graphite,
                80, Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.toxopid, "drops",
                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120, Items.silicon, 120,
                        Items.thorium, 40, Items.plastanium, 40, Items.surgeAlloy, 15, Items.phaseFabric, 5))));
        drops.add(Map.of("unit", UnitTypes.dagger, "drops",
                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 10, Items.silicon, 3))));
        drops.add(Map.of("unit", UnitTypes.mace, "drops",
                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.graphite, 10, Items.titanium, 5))));
        drops.add(Map.of("unit", UnitTypes.fortress, "drops",
                Seq.with(ItemStack.list(Items.lead, 100, Items.graphite, 40, Items.silicon, 40, Items.thorium, 10))));
        drops.add(Map.of("unit", UnitTypes.scepter, "drops", Seq.with(ItemStack.list(Items.copper, 300, Items.silicon,
                80, Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.reign, "drops",
                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120, Items.silicon, 120,
                        Items.thorium, 40, Items.plastanium, 40, Items.surgeAlloy, 15, Items.phaseFabric, 5))));
        drops.add(Map.of("unit", UnitTypes.nova, "drops",
                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 10, Items.metaglass, 3))));
        drops.add(Map.of("unit", UnitTypes.pulsar, "drops",
                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.metaglass, 10))));
        drops.add(Map.of("unit", UnitTypes.quasar, "drops", Seq.with(ItemStack.list(Items.lead, 100, Items.metaglass,
                40, Items.silicon, 40, Items.titanium, 80, Items.thorium, 10))));
        drops.add(Map.of("unit", UnitTypes.vela, "drops", Seq.with(ItemStack.list(Items.copper, 300, Items.metaglass,
                80, Items.graphite, 80, Items.titanium, 60, Items.plastanium, 20, Items.surgeAlloy, 5))));
        drops.add(Map.of("unit", UnitTypes.corvus, "drops",
                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 100, Items.silicon, 100,
                        Items.metaglass, 120, Items.titanium, 120, Items.thorium, 60, Items.surgeAlloy, 10,
                        Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.flare, "drops",
                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 10, Items.graphite, 3))));
        drops.add(Map.of("unit", UnitTypes.horizon, "drops",
                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.graphite, 10))));
        drops.add(Map.of("unit", UnitTypes.zenith, "drops", Seq.with(ItemStack.list(Items.lead, 100, Items.graphite, 40,
                Items.silicon, 40, Items.titanium, 30, Items.plastanium, 10))));
        drops.add(Map.of("unit", UnitTypes.antumbra, "drops", Seq.with(ItemStack.list(Items.copper, 300, Items.graphite,
                80, Items.metaglass, 80, Items.titanium, 60, Items.surgeAlloy, 15))));
        drops.add(Map.of("unit", UnitTypes.eclipse, "drops",
                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120, Items.silicon, 120,
                        Items.thorium, 120, Items.plastanium, 40, Items.surgeAlloy, 5, Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.mono, "drops",
                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 10, Items.silicon, 3))));
        drops.add(Map.of("unit", UnitTypes.poly, "drops",
                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.silicon, 10, Items.titanium, 5))));
        drops.add(Map.of("unit", UnitTypes.mega, "drops",
                Seq.with(ItemStack.list(Items.lead, 100, Items.silicon, 40, Items.graphite, 40, Items.thorium, 10))));
        drops.add(Map.of("unit", UnitTypes.quad, "drops", Seq.with(ItemStack.list(Items.copper, 300, Items.silicon, 80,
                Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.oct, "drops",
                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 120, Items.silicon, 120,
                        Items.thorium, 40, Items.plastanium, 40, Items.surgeAlloy, 15, Items.phaseFabric, 5))));
        drops.add(Map.of("unit", UnitTypes.risso, "drops",
                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 10, Items.metaglass, 3))));
        drops.add(Map.of("unit", UnitTypes.minke, "drops",
                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.metaglass, 10))));
        drops.add(Map.of("unit", UnitTypes.bryde, "drops", Seq.with(ItemStack.list(Items.lead, 100, Items.metaglass, 40,
                Items.silicon, 40, Items.titanium, 80, Items.thorium, 10))));
        drops.add(Map.of("unit", UnitTypes.sei, "drops", Seq.with(ItemStack.list(Items.copper, 300, Items.metaglass, 80,
                Items.graphite, 80, Items.titanium, 60, Items.plastanium, 20, Items.surgeAlloy, 5))));
        drops.add(Map.of("unit", UnitTypes.omura, "drops",
                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 100, Items.silicon, 100,
                        Items.metaglass, 120, Items.titanium, 120, Items.thorium, 60, Items.surgeAlloy, 10,
                        Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.retusa, "drops",
                Seq.with(ItemStack.list(Items.copper, 20, Items.lead, 10, Items.metaglass, 3))));
        drops.add(Map.of("unit", UnitTypes.oxynoe, "drops",
                Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 40, Items.metaglass, 10))));
        drops.add(Map.of("unit", UnitTypes.cyerce, "drops", Seq.with(ItemStack.list(Items.lead, 100, Items.metaglass,
                40, Items.silicon, 40, Items.titanium, 80, Items.thorium, 10))));
        drops.add(Map.of("unit", UnitTypes.aegires, "drops", Seq.with(ItemStack.list(Items.copper, 300, Items.metaglass,
                80, Items.graphite, 80, Items.titanium, 60, Items.plastanium, 20, Items.surgeAlloy, 5))));
        drops.add(Map.of("unit", UnitTypes.navanax, "drops",
                Seq.with(ItemStack.list(Items.copper, 400, Items.lead, 400, Items.graphite, 100, Items.silicon, 100,
                        Items.metaglass, 120, Items.titanium, 120, Items.thorium, 60, Items.surgeAlloy, 10,
                        Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.alpha, "drops", Seq.with(ItemStack.list(Items.copper, 30, Items.lead, 30,
                Items.graphite, 20, Items.silicon, 20, Items.metaglass, 20))));
        drops.add(Map.of("unit", UnitTypes.beta, "drops",
                Seq.with(ItemStack.list(Items.titanium, 40, Items.thorium, 20))));
        drops.add(Map.of("unit", UnitTypes.gamma, "drops",
                Seq.with(ItemStack.list(Items.plastanium, 20, Items.surgeAlloy, 10, Items.phaseFabric, 10))));
        drops.add(Map.of("unit", UnitTypes.stell, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 20, Items.silicon, 25))));
        drops.add(Map.of("unit", UnitTypes.locus, "drops", Seq
                .with(ItemStack.list(Items.beryllium, 20, Items.graphite, 20, Items.silicon, 20, Items.tungsten, 15))));
        drops.add(Map.of("unit", UnitTypes.precept, "drops", Seq.with(ItemStack.list(Items.beryllium, 45,
                Items.graphite, 25, Items.silicon, 50, Items.tungsten, 50, Items.surgeAlloy, 75, Items.thorium, 40))));
        drops.add(Map.of("unit", UnitTypes.vanquish, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 80, Items.graphite, 50, Items.silicon, 100, Items.tungsten,
                        120, Items.oxide, 60, Items.surgeAlloy, 125, Items.thorium, 100, Items.phaseFabric, 60))));
        drops.add(Map.of("unit", UnitTypes.conquer, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 250, Items.graphite, 225, Items.silicon, 125, Items.tungsten,
                        140, Items.oxide, 120, Items.carbide, 240, Items.surgeAlloy, 250, Items.thorium, 240,
                        Items.phaseFabric, 120))));
        drops.add(Map.of("unit", UnitTypes.elude, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 6, Items.graphite, 25, Items.silicon, 35))));
        drops.add(Map.of("unit", UnitTypes.avert, "drops", Seq.with(ItemStack.list(Items.beryllium, 24, Items.graphite,
                50, Items.silicon, 30, Items.tungsten, 20, Items.oxide, 20))));
        drops.add(Map.of("unit", UnitTypes.obviate, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 48, Items.graphite, 75, Items.silicon, 50, Items.tungsten, 45,
                        Items.carbide, 50, Items.thorium, 40, Items.phaseFabric, 75))));
        drops.add(Map.of("unit", UnitTypes.quell, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 96, Items.graphite, 100, Items.silicon, 140, Items.tungsten,
                        70, Items.oxide, 60, Items.carbide, 75, Items.surgeAlloy, 60, Items.thorium, 100,
                        Items.phaseFabric, 125))));
        drops.add(Map.of("unit", UnitTypes.disrupt, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 122, Items.graphite, 125, Items.silicon, 155, Items.tungsten,
                        100, Items.oxide, 120, Items.carbide, 240, Items.surgeAlloy, 120, Items.thorium, 240,
                        Items.phaseFabric, 250))));
        drops.add(Map.of("unit", UnitTypes.merui, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 25, Items.silicon, 35, Items.tungsten, 10))));
        drops.add(Map.of("unit", UnitTypes.cleroi, "drops", Seq.with(ItemStack.list(Items.beryllium, 35, Items.graphite,
                20, Items.silicon, 25, Items.tungsten, 20, Items.oxide, 20))));
        drops.add(Map.of("unit", UnitTypes.anthicus, "drops", Seq.with(ItemStack.list(Items.beryllium, 50,
                Items.graphite, 25, Items.silicon, 50, Items.tungsten, 65, Items.oxide, 75, Items.thorium, 40))));
        drops.add(Map.of("unit", UnitTypes.tecta, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 100, Items.graphite, 50, Items.silicon, 140, Items.tungsten,
                        120, Items.oxide, 125, Items.surgeAlloy, 60, Items.thorium, 100, Items.phaseFabric, 125))));
        drops.add(Map.of("unit", UnitTypes.collaris, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 135, Items.graphite, 90, Items.silicon, 175, Items.tungsten,
                        155, Items.oxide, 250, Items.carbide, 240, Items.surgeAlloy, 120, Items.thorium, 240,
                        Items.phaseFabric, 120))));
        drops.add(Map.of("unit", UnitTypes.evoke, "drops",
                Seq.with(ItemStack.list(Items.beryllium, 50, Items.graphite, 50, Items.silicon, 50))));
        drops.add(Map.of("unit", UnitTypes.incite, "drops",
                Seq.with(ItemStack.list(Items.tungsten, 25, Items.oxide, 25, Items.carbide, 50))));
        drops.add(Map.of("unit", UnitTypes.emanate, "drops",
                Seq.with(ItemStack.list(Items.surgeAlloy, 25, Items.thorium, 25, Items.phaseFabric, 50))));
    }
}