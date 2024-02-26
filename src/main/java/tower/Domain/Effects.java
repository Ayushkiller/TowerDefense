package tower.Domain;

import mindustry.content.StatusEffects;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Effects {
    public static List<Map<String, Object>> effects = new ArrayList<>();

    static {
        effects.add(Map.of("effect", StatusEffects.overdrive, "price",  20));
        effects.add(Map.of("effect", StatusEffects.fast, "price",  15));
        effects.add(Map.of("effect", StatusEffects.overclock, "price",  30));
        effects.add(Map.of("effect", StatusEffects.shielded, "price",  20));
        effects.add(Map.of("effect", StatusEffects.boss, "price",  45));
        effects.add(Map.of("effect", StatusEffects.invincible, "price",  30));
        effects.add(Map.of("effect", StatusEffects.unmoving, "price",  0));
    }
}

