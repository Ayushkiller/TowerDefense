package tower.commands.Abilityies;

import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.gen.*;
import mindustry.type.*;

public class CustomStatusFieldAbility extends StatusFieldAbility {
    public CustomStatusFieldAbility(StatusEffect effect, float duration, float reload, float range) {
        super(effect, duration, reload, range);
    }

    @Override
    public void update(Unit unit) {
        timer += Time.delta;

        if(timer >= reload && (!onShoot || unit.isShooting)){
            Units.nearby(unit.team, unit.x, unit.y, range, other -> {
                if(unit.team != other.team) {
                    other.apply(effect, duration);
                    applyEffect.at(other, parentizeEffects);
                }
            });

            float x = unit.x + Angles.trnsx(unit.rotation, effectY, effectX), y = unit.y + Angles.trnsy(unit.rotation, effectY, effectX);
            activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, parentizeEffects ? unit : null);

            timer = 0f;
        }
    }
}