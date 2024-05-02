package tower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import arc.Events;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.game.EventType;
import mindustry.game.EventType.BlockBuildEndEvent;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.mod.Plugin;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.power.NuclearReactor;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.Separator;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItemFilter;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumeLiquidFilter;
import mindustry.world.consumers.ConsumeLiquids;

public class TowerDefensePlugin extends Plugin {
    private final Set<Item> allItems = new HashSet<>();
    private final Set<Liquid> allLiquids = new HashSet<>();

    private final ConcurrentHashMap<Block, BlockConsumersCache> blockConsumersCaches = new ConcurrentHashMap<>();
    public TowerDefensePlugin() {
         Items.serpuloItems.each(item -> allItems.add(item));
         Items.erekirOnlyItems.each(item -> allItems.add(item));

        allLiquids.add(Liquids.water);
        allLiquids.add(Liquids.slag);
        allLiquids.add(Liquids.oil);
        allLiquids.add(Liquids.cryofluid);
        allLiquids.add(Liquids.arkycite);
        allLiquids.add(Liquids.gallium);
        allLiquids.add(Liquids.neoplasm);
        allLiquids.add(Liquids.ozone);
        allLiquids.add(Liquids.hydrogen);
        allLiquids.add(Liquids.nitrogen);
        allLiquids.add(Liquids.cyanogen);
        Events.run(EventType.Trigger.update, () -> onUpdate());
        Events.on(EventType.BlockBuildEndEvent.class, this::onBlockBuildEndEvent);
    }

    private BlockConsumersCache buildCache(Block block) {
        List<Item> items = new ArrayList<>();
        List<Liquid> liquids = new ArrayList<>();

        for (Consume consumer : block.consumers) {
            if (consumer instanceof ConsumeItems) {
                items.addAll(Arrays.stream(((ConsumeItems) consumer).items).map(itemStack -> itemStack.item)
                        .collect(Collectors.toList()));
            } else if (consumer instanceof ConsumeItemFilter) {
                ConsumeItemFilter itemFilter = (ConsumeItemFilter) consumer;
                allItems.stream().filter(itemFilter.filter::get).forEach(items::add);
            } else if (consumer instanceof ConsumeLiquidFilter) {
                ConsumeLiquidFilter liquidFilter = (ConsumeLiquidFilter) consumer;
                allLiquids.stream().filter(liquidFilter.filter::get).forEach(liquids::add);
            } else if (consumer instanceof ConsumeLiquid) {
                liquids.add(((ConsumeLiquid) consumer).liquid);
            } else if (consumer instanceof ConsumeLiquids) {
                liquids.addAll(Arrays.stream(((ConsumeLiquids) consumer).liquids).map(liquidStack -> liquidStack.liquid)
                        .collect(Collectors.toList()));
            }
        }

        List<ItemStack> itemsCache = new ArrayList<>();
        List<LiquidStack> liquidsCache = new ArrayList<>();

        for (Item item : items) {
            if (block instanceof NuclearReactor) {
                itemsCache.add(new ItemStack(item, ((NuclearReactor) block).itemCapacity));
            } else {
                itemsCache.add(new ItemStack(item, Integer.MAX_VALUE));
            }
        }

        for (Liquid liquid : liquids) {
            liquidsCache.add(new LiquidStack(liquid, Float.MAX_VALUE));
        }

        return new BlockConsumersCache(itemsCache, liquidsCache);
    }

    private void updateBuilding(Building building) {
        BlockConsumersCache cachedConsumers = blockConsumersCaches.computeIfAbsent(building.block, this::buildCache);

        for (ItemStack itemStack : cachedConsumers.items) {
            building.items.set(itemStack.item, itemStack.amount);
        }

        for (LiquidStack liquidStack : cachedConsumers.liquids) {
            building.liquids.set(liquidStack.liquid, liquidStack.amount);
        }
    }

   public void onBlockBuildEndEvent(BlockBuildEndEvent event) {
    if (event.breaking)
        return;

    if (filledBlockFilter(event.tile.build.block)) {
        updateBuilding(event.tile.build);
    }
}

    public void onUpdate() {
        Groups.build.each(building -> {
            if (filledBlockFilter(building.block)) {
                updateBuilding(building);
            }
        });
    }

    private boolean filledBlockFilter(Block block) {
        return block instanceof Separator || block instanceof GenericCrafter || block instanceof PowerGenerator;
    }
}