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
        effects.add(Map.of("effect", StatusEffects.overdrive, "price", 40, "healthMultiplier", 4f, "speedMultiplier", 1.2f, "damageMultiplier", 1.5f, "name", "Overdrive Enhanced"));
        effects.add(Map.of("effect", StatusEffects.fast, "price", 30, "speedMultiplier", 4f, "name", "Fast Enhanced"));
        effects.add(Map.of("effect", StatusEffects.overclock, "price", 50, "speedMultiplier", 1.3f, "damageMultiplier", 2f, "reloadMultiplier", 4f, "name", "Overclock Enhanced"));
        effects.add(Map.of("effect", StatusEffects.shielded, "price", 40, "healthMultiplier", 4f, "name", "Shielded Enhanced"));
        effects.add(Map.of("effect", StatusEffects.boss, "price", 60, "damageMultiplier", 3f, "healthMultiplier", 2f, "name", "Boss Enhanced"));
        effects.add(Map.of("effect", StatusEffects.invincible, "price", 50, "healthMultiplier", 5f, "name", "Invincible Enhanced"));
    }
}

