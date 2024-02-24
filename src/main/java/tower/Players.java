package tower;


import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import tower.Domain.PlayerData;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Players {
    private static final Logger logger = Logger.getLogger(Players.class.getName());
    private static final Map<String, PlayerData> players = new HashMap<>();

    public static PlayerData getPlayer(Player player) {
        if (!players.containsKey(player.uuid())) {
            PlayerData playerData = new PlayerData(player);
            players.put(player.uuid(), playerData);
            logger.info("Created new PlayerData for player " + player.uuid());
        }
        logger.info("Retrieved PlayerData for player " + player.uuid());
        return players.get(player.uuid());
    }


    public static void clearMap() {
        players.clear();
    }

    public static void forEach() {
        Groups.player.each(player -> {
            if(Players.getPlayer(player).getUnit() != null && player.unit() != Players.getPlayer(player).getUnit()) {
                player.unit(Players.getPlayer(player).getUnit());
            }

            if(Players.getPlayer(player).stats()) {
                Groups.player.each(p -> {
                    if (!p.dead()) {
                        String message = "[scarlet]" + (int) p.unit().health + "/" + (int) p.unit().type.health + "\n" +
                                "[accent]" + (int) p.unit().shield + " " +
                                "[green]" + (int) Players.getPlayer(p).getPoints() + "\n  ";
                        Call.label(player.con, message.replace("  ", Players.getPlayer(p).stats()? "[lime]true" : "[scarlet]false"),  0.017f, p.x, p.y-16);
                    }
                });
            }
            StringBuilder hud = new StringBuilder();

            hud.append("[green]").append((int) Players.getPlayer(player).getPoints());

            Call.setHudText(hud.toString());
        });
    }
}
