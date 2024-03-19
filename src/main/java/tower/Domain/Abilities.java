package tower.Domain;

import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.entities.abilities.UnitSpawnAbility;
import tower.commands.Abilityies.CustomStatusFieldAbility;
import java.util.List;
import java.util.Arrays;

public class Abilities {
    public static List<Ability> getAbility1() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 20f;
        return Arrays.asList(
                new StatusFieldAbility(StatusEffects.boss, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.sporeSlowed, duration, cooldown, range));
    }

    public static List<Ability> getAbility2() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 30f;
        return Arrays.asList(
                new StatusFieldAbility(StatusEffects.overclock, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.electrified, duration, cooldown, range));
    }

    public static List<Ability> getAbility3() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 60f;
        return Arrays.asList(
                new CustomStatusFieldAbility(StatusEffects.sapped, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.burning, duration, cooldown, range));
    }

    public static List<Ability> getAbility4() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 60f;
        return Arrays.asList(
                new CustomStatusFieldAbility(StatusEffects.sporeSlowed, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.blasted, duration, cooldown, range));
    }

    public static List<Ability> getAbility5() {

        return Arrays.asList(
                new ForceFieldAbility(140f, 4f, 7000f, 60f * 8, 12, 0f),
                new UnitSpawnAbility(UnitTypes.poly, 60 * 40, 80, 40),
                new UnitSpawnAbility(UnitTypes.poly, 60 * 40, 40, 80),
                new UnitSpawnAbility(UnitTypes.poly, 60 * 40, 30, 40),
                new UnitSpawnAbility(UnitTypes.poly, 60 * 40, 40, 30),
                new UnitSpawnAbility(UnitTypes.poly, 60 * 40, 50, 40),
                new UnitSpawnAbility(UnitTypes.poly, 60 * 40, 40, 50));

    }

    public static List<Ability> getAbility6() {
        float spawnTime = 60f * 45f;
        float x1 = 19.25f;
        float y1 = -31.75f;
        float x2 = -19.25f;
        float y2 = -31.75f;

        return Arrays.asList(
                new UnitSpawnAbility(UnitTypes.zenith, spawnTime, x1, y1),
                new UnitSpawnAbility(UnitTypes.zenith, spawnTime, x2, y2));
    }

    public static List<Ability> getAbility7() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 30f;
        return Arrays.asList(
                new StatusFieldAbility(StatusEffects.overclock, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.melting, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.electrified, duration, cooldown, range));
    }

    public static List<Ability> getAbility8() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 15f;
        return Arrays.asList(
                new CustomStatusFieldAbility(StatusEffects.melting, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.shocked, duration, cooldown, range));
    }

    public static List<Ability> getAbility9() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 30f;
        return Arrays.asList(
                new StatusFieldAbility(StatusEffects.fast, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.freezing, duration, cooldown, range));
    }

    public static List<Ability> getAbility10() {
        float duration = 60f * 6;
        float cooldown = 60f * 6f;
        float range = 30f;
        return Arrays.asList(
                new StatusFieldAbility(StatusEffects.overdrive, duration, cooldown, range),
                new CustomStatusFieldAbility(StatusEffects.blasted, duration, cooldown, range));
    }
}