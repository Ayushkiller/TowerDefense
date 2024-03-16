package tower.game;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.PluginLogic;
import tower.Domain.PlayerData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scenarios {

    private static final int DeploymentMenu = Menus.registerMenu(Scenarios::handleDeploymentOption);
    private static final int DeploymentMenuClose = Menus.registerMenu((player, option) -> handleDeploymentOption1(player));
    private static final String[][] DeploymentButtons = {
            {"[red]No"},
            {"[lime]Yes"}
    };
    private static final String[][] DeploymentButtonsClose = {
            {"[gray]Close"}
    };
    private static int globalYesVotes = 0;
    private static int globalNoVotes = 0;
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    public static void requestDeploymentForAllPlayers() {
        Player[] allPlayers = PlayerData.getAllPlayers();
        for (Player player : allPlayers) {
            Scenarios.requestDeployment(player);
        }
    }

    public static void requestDeployment(Player player) {
        Call.menu(player.con, DeploymentMenu, Bundle.get("deployment.title", player.locale), Bundle.get("deployment.message", player.locale), DeploymentButtons);
    }

    private static void handleDeploymentOption(Player player, int option) {
        switch (option) {
            case 0:
                globalNoVotes++;
                break;
            case 1:
                globalYesVotes++;
                break;
        }
    
        // Check if all players have voted globally
        if (globalYesVotes + globalNoVotes == PlayerData.getTotalPlayers()) {
            // Schedule handleDeploymentOption1 to be called 30 seconds later for all players
            executor.schedule(() -> {
                Player[] allPlayers = PlayerData.getAllPlayers();
                for (Player p : allPlayers) {
                    handleDeploymentOption1(p);
                }
            }, 30, TimeUnit.SECONDS);
        }
    }

    private static void handleDeploymentOption1(Player player) {
        String message = globalYesVotes > globalNoVotes ? Bundle.get("deployment.success", player.locale) : Bundle.get("deployment.failure", player.locale);
        Call.menu(player.con, DeploymentMenuClose, Bundle.get("deployment.title", player.locale), message, DeploymentButtonsClose);
        if (globalYesVotes > globalNoVotes) {
            PluginLogic.Help(globalYesVotes);
        }
        if (globalNoVotes > globalYesVotes) {
            PluginLogic.adjustMultiplierBasedOnNoVotes(globalNoVotes);
        }
        globalYesVotes = 0;
        globalNoVotes = 0;
    }
}