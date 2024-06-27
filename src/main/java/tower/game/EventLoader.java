package tower.game;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import arc.Events;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;
import tower.commands.BuyPoint;
import tower.commands.Info;
import tower.commands.Statuseffects;
import tower.commands.SuperPowers;
import tower.commands.Units;

public class EventLoader {

    private static final Map<Integer, BiConsumer<Player, Integer>> dynamicListeners = new HashMap<>();
    private static int welcomeMenu;

    public static void init() {
        Units.registerTierMenu();
        Statuseffects.registerMenus();
        Info.registerMenu();
        BuyPoint.registerMenus();
        Units.initUnitsTable();
        registerWelcomeMenu();

        Events.on(EventType.PlayerJoin.class, event -> {
            Player player = event.player;
            Players.getPlayer(player); // This ensures a new PlayerData is created if necessary
            openWelcomeMenu(player);
        });
        Events.run(EventType.Trigger.update, Players::forEach);
        Events.on(EventType.PlayerLeave.class, event -> {
            Player player = event.player;
            SuperPowers.alphaAbilityLastUseTime.remove(player);
        });
    }

    private static void registerWelcomeMenu() {
        welcomeMenu = Menus.registerMenu((player, option) -> dynamicListeners.get(welcomeMenu).accept(player, option));
        dynamicListeners.put(welcomeMenu, (player, option) -> {
            // No action required for close button
        });
    }

    private static void openWelcomeMenu(Player player) {
        Call.menu(player.con, welcomeMenu, Bundle.get("welcome", player.locale),
                Bundle.get("welcome.message", player.locale),
                new String[][]{{Bundle.get("close", player.locale)}});
    }
}
