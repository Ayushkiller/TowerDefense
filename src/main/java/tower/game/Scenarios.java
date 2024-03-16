package tower.game;

import java.util.Timer;
import java.util.TimerTask;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.PluginLogic;
import tower.Domain.PlayerData;

public class Scenarios {
    private static final int deploymentMenuClose = Menus.registerMenu(Scenarios::handleDeploymentOptionClose);
    private static final int deploymentMenu = Menus.registerMenu(Scenarios::handleDeploymentOption);
    private static final String[][] deploymentButtons = {
            {"[red]No"},
            {"[lime]Yes"}
    };
    private static final String[][] deploymentButtonsClose = {
            {"[gray]Close"}
    };
    private static int globalYesVotes = 0;
    private static int globalNoVotes = 0;

    public static void requestDeployment(Player player) {
        if (player == null) {
            System.out.println("Warning: Attempted to request deployment for a null player.");
            return;
        }

        Call.menu(player.con, deploymentMenu, Bundle.get("deployment.title", player.locale), Bundle.get("deployment.message", player.locale), deploymentButtons);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleDeploymentOption1(player);
            }
        }, 30000); // = 30 seconds
    }
    private static void handleDeploymentOptionClose(Player player, int option) {
       
        System.out.println("Close menu option selected by player: " + player.name);
    }
    private static void handleDeploymentOption(Player player, int option) {
        switch (option) {
            case 0:
                globalNoVotes++;
                System.out.println("No vote added. Current No votes: " + globalNoVotes);
                break;
            case 1:
                globalYesVotes++;
                System.out.println("Yes vote added. Current Yes votes: " + globalYesVotes);
                break;
        }
    }

    private static void handleDeploymentOption1(Player player) {
        System.out.println("Handling deployment option for player: " + player.name);
        String message = globalYesVotes > globalNoVotes ? Bundle.get("deployment.success", player.locale) : Bundle.get("deployment.failure", player.locale);
        System.out.println("Message for player: " + message);
        Call.menu(player.con, deploymentMenuClose, Bundle.get("deployment.title", player.locale), message, deploymentButtonsClose);
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
        System.out.println("Current Yes votes: " + globalYesVotes + ", Current No votes: " + globalNoVotes);
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