package tower.Domain;

import mindustry.content.Items;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Currency {
    public static List<Map<String, Object>> items = new ArrayList<>();

    static {
        // Tier 1
        items.add(Map.of("item", Items.copper, "gain", 1, "price", 400));
        items.add(Map.of("item", Items.lead, "gain", 1, "price", 400));
        items.add(Map.of("item", Items.graphite, "gain", 1, "price", 350));
        items.add(Map.of("item", Items.beryllium, "gain", 1, "price", 500));

        // Tier 2
        items.add(Map.of("item", Items.metaglass, "gain", 1, "price", 320));
        items.add(Map.of("item", Items.silicon, "gain", 1, "price", 320));
        items.add(Map.of("item", Items.titanium, "gain", 1, "price", 270));
        items.add(Map.of("item", Items.tungsten, "gain", 1, "price", 300));

        // Tier 3
        items.add(Map.of("item", Items.thorium, "gain", 1, "price", 240));
        items.add(Map.of("item", Items.plastanium, "gain", 1, "price", 200));
        items.add(Map.of("item", Items.oxide, "gain", 1, "price", 240));

        // Tier 4
        items.add(Map.of("item", Items.phaseFabric, "gain", 1, "price", 150));
        items.add(Map.of("item", Items.surgeAlloy, "gain", 1, "price", 150));
        items.add(Map.of("item", Items.carbide, "gain", 1, "price", 150));
    }
}