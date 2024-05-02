package tower;

import java.util.List;

import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class BlockConsumersCache {
    final List<ItemStack> items;
    final List<LiquidStack> liquids;

    public BlockConsumersCache(List<ItemStack> items, List<LiquidStack> liquids) {
        this.items = items;
        this.liquids = liquids;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public List<LiquidStack> getLiquids() {
        return liquids;
    }
}