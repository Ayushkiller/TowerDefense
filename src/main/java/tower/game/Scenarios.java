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
    public static void requestDeployment(Player player) {
        if (player == null) {
            System.out.println("Warning: Attempted to request deployment for a null player.");
            return;
        }
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
    
        executor.schedule(() -> {
            for (Player p : PlayerData.getAllPlayers()) {
                handleDeploymentOption1(p);
            }
        }, 30, TimeUnit.SECONDS);
        
    }

    private static void handleDeploymentOption1(Player player) {
        System.out.println("Handling deployment option for player: " + player.name);
        String message = globalYesVotes > globalNoVotes ? Bundle.get("deployment.success", player.locale) : Bundle.get("deployment.failure", player.locale);
        System.out.println("Message for player: " + message);
        Call.menu(player.con, DeploymentMenuClose, Bundle.get("deployment.title", player.locale), message, DeploymentButtonsClose);
        if (globalYesVotes > globalNoVotes) {
            System.out.println("Deployment successful with " + globalYesVotes + " votes.");
            PluginLogic.Help(globalYesVotes);
        }
        if (globalNoVotes > globalYesVotes) {
            System.out.println("Deployment failed with " + globalNoVotes + " votes.");
            PluginLogic.adjustMultiplierBasedOnNoVotes(globalNoVotes);
        }
        globalYesVotes = 0;
        globalNoVotes = 0;
        System.out.println("Resetting votes for player: " + player.name);
    }

    public static void requestDeploymentForAllPlayers() {
        for (Player player : PlayerData.getAllPlayers()) {
            if (player != null) {
                requestDeployment(player);
            } else {
                System.out.println("Warning: Skipping null player.");
            }
        }
    }
}