package tower.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import arc.Events;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Player;
import tower.Players;
import tower.Domain.PlayerData;

public class EventLoader {
    public static void init() {

        Events.on(EventType.PlayerJoin.class, event -> {
            Player player = event.player;
            List<Team> activeTeams = Arrays.stream(Team.all)
                    .filter(Team::active)
                    .collect(Collectors.toList());
            Team leastPlayersTeam = null;
            int minPlayers = Integer.MAX_VALUE;

            for (Team team : activeTeams) {
                int teamPlayers = getTeamPlayers(team);
                if (teamPlayers < minPlayers) {
                    minPlayers = teamPlayers;
                    leastPlayersTeam = team;
                }
            }

            if (leastPlayersTeam != null) {

                setPlayerTeam(player, leastPlayersTeam);
            }
        });

        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
        });

    }

    private static int getTeamPlayers(Team team) {
        int playerCount = 0;
        for (PlayerData playerData : PlayerData.getAllPlayers()) {
            if (playerData.getTeam().equals(team)) {
                playerCount++;
            }
        }
        return playerCount;
    }

    private static void setPlayerTeam(Player player, Team leastPlayersTeam) {
        player.team(leastPlayersTeam);
    }
}
