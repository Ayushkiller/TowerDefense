package tower;

import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;
import java.util.HashMap;
import java.util.Map;

public class Players {
    private static final Map<String, PlayerData> players = new HashMap<>();

    public static PlayerData getPlayer(Player player) {
        if (!players.containsKey(player.uuid())) {
            players.put(player.uuid(), new PlayerData(player));
            @SuppressWarnings("unused")
            PlayerData playerData = Players.getPlayer(player);
        }
        return players.get(player.uuid());
    }

    public static void clearMap() {
        players.clear();
    }

    public static void forEach() {
        Groups.player.each(player -> {
            if(Players.getPlayer(player).getUnit() != null && player.unit() != getPlayer(player).getUnit()) {
                player.unit(getPlayer(player).getUnit());
            }
            if(player.team() != getPlayer(player).getTeam()) {
                player.team(getPlayer(player).getTeam());
            }

            if(getPlayer(player).stats()) {
                Groups.player.each(p -> {
                    if (!p.dead()) {
                        String message = "[scarlet]" + (int) p.unit().health + "/" + (int) p.unit().type.health + "\n" +
                                "[accent]" + (int) p.unit().shield + " " +
                                "[green]" + (int) getPlayer(p).getPoints() + "\n  ";
                        Call.label(player.con, message.replace("  ", getPlayer(p).stats()? "[lime]true" : "[scarlet]false"), 0.017f, p.x, p.y-16);
                    }
                });
            }
            StringBuilder hud = new StringBuilder();

            hud.append("[green]").append((int) getPlayer(player).getPoints());

            Call.setHudText(hud.toString());
        });
    }
}