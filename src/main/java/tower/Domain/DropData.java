package tower.Domain;

import arc.struct.Seq;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;

public class DropData {
    private final UnitType unit;
    private final Seq<ItemStack> drops;

    public DropData(UnitType unit, Seq<ItemStack> drops) {
        this.unit = unit;
        this.drops = drops;
    }

    public UnitType getUnit() {
        return unit;
    }

    public Seq<ItemStack> getDrops() {
        return drops;
    }
}
