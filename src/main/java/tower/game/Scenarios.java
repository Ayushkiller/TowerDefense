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
    private static boolean isTaskScheduled = false;
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

            return;
        }

        Call.menu(player.con, deploymentMenu, Bundle.get("deployment.title", player.locale), Bundle.get("deployment.message", player.locale), deploymentButtons);
    }
    private static void handleDeploymentOptionClose(Player player, int option) {
       

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
    }

    private static void handleDeploymentOption1(Player player) {
        String message = globalYesVotes > globalNoVotes ? Bundle.get("deployment.success", player.locale) : Bundle.get("deployment.failure", player.locale);
        Call.menu(player.con, deploymentMenuClose, Bundle.get("deployment.title", player.locale), message, deploymentButtonsClose);
        if (globalYesVotes > globalNoVotes) {
            PluginLogic.Help(globalYesVotes);
        }
        if (globalNoVotes > globalYesVotes) {
            PluginLogic.adjustMultiplierBasedOnNoVotes(globalNoVotes);
        }
        globalYesVotes = 0;
        globalNoVotes = 0;
    }

    public static void requestDeploymentForAllPlayers() {
        if (!isTaskScheduled) { // Check if the task is already scheduled
            isTaskScheduled = true; // Set the flag to true to indicate the task is scheduled
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (Player player : PlayerData.getAllPlayers()) {
                        if (player != null) {
                            handleDeploymentOption1(player);
                        }
                    }
                    // Reset the flag after the task has been executed
                    isTaskScheduled = false;
                }
            }, 40000); // = 40 seconds
        }
    }
}