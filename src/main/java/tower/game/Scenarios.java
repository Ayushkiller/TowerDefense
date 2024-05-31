package tower.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;
import tower.PluginLogic;
import tower.Domain.PlayerData;

public class Scenarios {
    private static boolean isTaskScheduled = false;
    private static final int deploymentMenuClose = Menus.registerMenu(Scenarios::handleDeploymentOptionClose);
    private static final int deploymentMenu = Menus.registerMenu(Scenarios::handleDeploymentOption);
    private static final String[][] deploymentButtons = { { "[red]No" }, { "[lime]Yes" } };
    private static final String[][] deploymentButtonsClose = { { "[gray]Close" } };
    private static int globalYesVotes = 0;
    private static int globalNoVotes = 0;

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void requestDeployment(Player player) {
        if (player == null) {
            return;
        }
        Call.menu(player.con, deploymentMenu, Bundle.get("deployment.title", player.locale),
                Bundle.get("deployment.message", player.locale), deploymentButtons);
    }

    private static void handleDeploymentOptionClose(Player player, int option) {

    }

    private static synchronized void handleDeploymentOption(Player player, int option) {
        if (option == 0) {
            globalNoVotes++;
        } else if (option == 1) {
            globalYesVotes++;
        }
    }

    private static void handleDeploymentResult() {
        Groups.player.each(player -> {
            String message = globalYesVotes > globalNoVotes ? Bundle.get("deployment.success", player.locale)
                    : Bundle.get("deployment.failure", player.locale);
            Call.menu(player.con, deploymentMenuClose, Bundle.get("deployment.title", player.locale), message,
                    deploymentButtonsClose);
        });

        if (globalYesVotes > globalNoVotes) {
            deployHelp(globalYesVotes);
        } else {
            adjustMultiplierBasedOnNoVotes(globalNoVotes);
        }

        globalYesVotes = 0;
        globalNoVotes = 0;
    }

    public static void adjustMultiplierBasedOnNoVotes(int noVotes) {
        if (noVotes > 0) {
            float increaseAmount = Mathf.random(0.0f, 2.0f);
            PluginLogic.multiplier += increaseAmount;
            Call.sendMessage(Bundle.get("enemyBuffMessage"));
            PluginLogic.multiplierAdjusted = true;
        }
    }

    public static void deployHelp(int yesVotes) {
        if (yesVotes > 0) {
            scheduler.schedule(() -> {
                Groups.player.each(player -> {
                    PlayerData playerData = Players.getPlayer(player);
                    if (playerData != null) {
                        playerData.addCash(300);
                        Team team = Team.sharded;
                        Rules.TeamRule teamRule = Vars.state.rules.teams.get(team);
                        teamRule.blockDamageMultiplier = 1.2f;
                    }
                });
            }, 120, TimeUnit.SECONDS);

            Team team = Team.sharded;
            Rules.TeamRule teamRule = Vars.state.rules.teams.get(team);
            teamRule.blockDamageMultiplier = 0.5f;
            Call.sendMessage(Bundle.get("turretsDamageReducedMessage"));
            Call.sendMessage(Bundle.get("newEngineersArrivalMessage"));
        }
    }

    public static void requestDeploymentForAllPlayers() {
        Groups.player.each(Scenarios::requestDeployment);

        if (!isTaskScheduled) {
            isTaskScheduled = true;
            scheduler.schedule(() -> {
                handleDeploymentResult();
                isTaskScheduled = false;
            }, 40, TimeUnit.SECONDS);
        }
    }
}
