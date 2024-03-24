package tower.game;

import arc.Events;
import mindustry.game.EventType;
import tower.Players;

public class EventLoader {
    public static void init() {



        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
        });

    }


}
