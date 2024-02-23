package tower;

import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;
import java.util.HashMap;
import java.util.Map;


public class Players {
    private static Map<String, PlayerData> players = new HashMap<>();

    public static PlayerData getPlayer(Player player) {
        if (!players.containsKey(player.uuid())) {
            players.put(player.uuid(), new PlayerData(player));
        }
        return players.get(player.uuid());
    }

    public static void clearMap() {
        players.clear();
    }

    public static void forEach() {
        Groups.player.each(player -> {
            StringBuilder hud = new StringBuilder();

            hud.append("[green]").append((int) getPlayer(player).getPoints());

            Call.setHudText(hud.toString());
        });
    }
}

