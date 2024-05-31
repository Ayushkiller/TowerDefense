package tower.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mindustry.content.StatusEffects;

public class Effects {
    public static List<Map<String, Object>> effects = new ArrayList<>();

    static {
        effects.add(Map.of("name", "Overdrive", "effect", StatusEffects.overdrive, "price", 20));
        effects.add(Map.of("name", "Fast", "effect", StatusEffects.fast, "price", 15));
        effects.add(Map.of("name", "Overclock", "effect", StatusEffects.overclock, "price", 30));
        effects.add(Map.of("name", "Shielded", "effect", StatusEffects.shielded, "price", 20));
        effects.add(Map.of("name", "Boss", "effect", StatusEffects.boss, "price", 45));
        effects.add(Map.of("name", "Invincible", "effect", StatusEffects.invincible, "price", 30));
        effects.add(Map.of("name", "Unmoving", "effect", StatusEffects.unmoving, "price", 0));
    }
}
