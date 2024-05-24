package tower.game;

import arc.Events;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;

public class EventLoader {
    public static void init() {

        Events.on(EventType.PlayerJoin.class, event -> {
            Player player = event.player;
            Players.getPlayer(player); // This ensures a new PlayerData is created if necessary
            Call.menu(player.con, menu, Bundle.get("welcome", player.locale),
                    Bundle.get("welcome.message", player.locale),
                    new String[][] { { Bundle.get("close", player.locale) } });
        });

        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
        });
        Events.run(EventType.Trigger.update, Players::forEach);

    }

    private static final int menu = Menus.registerMenu(((player1, option) -> {
    }));
}
