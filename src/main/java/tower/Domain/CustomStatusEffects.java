package tower.Domain;

import mindustry.content.Fx;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;

public class CustomStatusEffects {
    public static StatusEffect overdriveBuffed, overclockBuffed, shieldedBuffed, bossBuffed, speedBuffed, slowAsShit,
            invincibleBuffed;

    public static void load() {
        overdriveBuffed = new StatusEffect("overdriveBuffed") {
            {
                color = Pal.accent;
                healthMultiplier = 1.2f;
                speedMultiplier = 1.4f;
                damageMultiplier = 1.8f;
                damage = -0.02f;
                effect = Fx.overdriven;
                permanent = true;
            }
        };

        overclockBuffed = new StatusEffect("overclockBuffed") {
            {
                color = Pal.accent;
                speedMultiplier = 1.5f;
                damageMultiplier = 2f;
                reloadMultiplier = 3f;
                effectChance = 0.1f;
                effect = Fx.overclocked;
            }
        };

        shieldedBuffed = new StatusEffect("shieldedBuffed") {
            {
                color = Pal.accent;
                healthMultiplier = 5f;
            }
        };

        bossBuffed = new StatusEffect("bossBuffed") {
            {
                color = Team.crux.color;
                permanent = true;
                damageMultiplier = 1.6f;
                healthMultiplier = 2f;
            }
        };
        speedBuffed = new StatusEffect("speedBuffed") {
            {
                color = Team.sharded.color;
                permanent = true;
                damageMultiplier = 1.6f;
                speedMultiplier = 4f;
                healthMultiplier = 2f;
            }
        };

        invincibleBuffed = new StatusEffect("invincibleBuffed") {
            {
                healthMultiplier = Float.POSITIVE_INFINITY;
                reloadMultiplier = 2f;
            }
        };
        slowAsShit = new StatusEffect("slowAsShit") {
            {
                speedMultiplier = 0.5f;
            }
        };
    }
}