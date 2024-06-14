package tower.game;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import arc.Events;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;
import tower.Domain.PlayerData;
import tower.commands.BuyPoint;
import tower.commands.Info;
import tower.commands.Statuseffects;
import tower.commands.SuperPowers;
import tower.commands.Units;

public class EventLoader {

    private static final Map<Integer, BiConsumer<Player, Integer>> dynamicListeners = new HashMap<>();
    private static int welcomeMenu;

    public static void init() {
        Units.registerMenus();
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

        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
            Groups.player.each(player -> {
                PlayerData playerData = Players.getPlayer(player);
                if (playerData != null) {
                    playerData.setCash(0);
                }
            });
        });

        Events.run(EventType.Trigger.update, Players::forEach);

        Events.on(EventType.PlayerLeave.class, event -> {
            Player player = event.player;
            SuperPowers.alphaAbilityLastUseTime.remove(player);
            SuperPowers.registeredForTapEvent.remove(player);
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
