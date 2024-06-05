package tower;

import static mindustry.Vars.*;

import arc.Events;
import arc.util.Log;
import mindustry.core.Logic;
import mindustry.game.EventType.WaveEvent;
import tower.pathing.CustomWaveSpawner;

public class Comp extends Logic {
    @Override
    public void runWave() {
        Log.info("RunWave called");
        CustomWaveSpawner spawner = new CustomWaveSpawner();
        spawner.spawnEnemies();
        Log.info("Enemies spawned");
        state.wave++;
        state.wavetime = state.rules.waveSpacing;
        Log.info("Wave incremented, wavetime set");

        Events.fire(new WaveEvent());
        Log.info("Wave event fired");
    }
}
