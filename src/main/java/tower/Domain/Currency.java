package tower.Domain;

import mindustry.content.Items;
import mindustry.type.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Currency {
    public static List<Map<String, Object>> items = new ArrayList<>();

    static {
        // Tier  1
        items.add(Map.of("item", Items.copper, "gain",  1, "price",  400, "minQuantity",  1500));
        items.add(Map.of("item", Items.lead, "gain",  1, "price",  400, "minQuantity",  1500));
        items.add(Map.of("item", Items.graphite, "gain",  1, "price",  350, "minQuantity",  1500));
        items.add(Map.of("item", Items.beryllium, "gain",  1, "price",  500, "minQuantity",  1500));

        // Tier  2
        items.add(Map.of("item", Items.metaglass, "gain",  1, "price",  320, "minQuantity",  1500));
        items.add(Map.of("item", Items.silicon, "gain",  1, "price",  320, "minQuantity",  1500));
        items.add(Map.of("item", Items.titanium, "gain",  1, "price",  270, "minQuantity",  1500));
        items.add(Map.of("item", Items.tungsten, "gain",  1, "price",  300, "minQuantity",  1500));

        // Tier  3
        items.add(Map.of("item", Items.thorium, "gain",  1, "price",  240, "minQuantity",  1500));
        items.add(Map.of("item", Items.plastanium, "gain",  1, "price",  200, "minQuantity",  1500));
        items.add(Map.of("item", Items.oxide, "gain",  1, "price",  240, "minQuantity",  1500));

        // Tier  4
        items.add(Map.of("item", Items.phaseFabric, "gain",  1, "price",  150, "minQuantity",  1500));
        items.add(Map.of("item", Items.surgeAlloy, "gain",  1, "price",  150, "minQuantity",  1500));
        items.add(Map.of("item", Items.carbide, "gain",  1, "price",  150, "minQuantity",  1500));
    }
}