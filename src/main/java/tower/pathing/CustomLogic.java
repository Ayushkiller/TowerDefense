package tower.pathing;

import static mindustry.Vars.state;

import arc.Events;
import mindustry.core.Logic;
import mindustry.game.EventType.WaveEvent;

public class CustomLogic extends Logic {
    @Override
    public void runWave() {
    
        System.out.println("Running wave...");
    
        FlyerWaves flyerWavesInstance = new FlyerWaves();
        flyerWavesInstance.spawnEnemies();
        state.wave++;
        state.wavetime = state.rules.waveSpacing;
    
        System.out.println("Wave completed. Current wave: " + state.wave);
    
        Events.fire(new WaveEvent());
    }


}