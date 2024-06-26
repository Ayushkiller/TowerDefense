package tower.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;

public class Info {

    private static final Map<Integer, BiConsumer<Player, Integer>> dynamicListeners = new HashMap<>();
    private static int menu;
    private static final String[][] buttons = {
            { "[gray]Close" },
            { "[accent]Next" }
    };
    private static int currentMessageIndex = 0;
    private static String[] messages = {
            "\ue837[#FFD700] ABOUT TOWER DEFENSE\n\n[white]Engage in strategic warfare as enemies relentlessly advance towards your core. Defend using a variety of towers, each with unique abilities and upgrade paths. Balance offense with resource management to ensure survival in intense battles.",
            "\ue837[#FFD700] ABOUT CASH\n[white]Cash, a [pink]distinct currency, is crucial for building and upgrading defensive structures. Earn cash by defeating enemies and strategically placing support structures like repair points and shockwave towers. Manage your resources efficiently to strengthen your defenses.",
            "\ue837[#FFD700] ABOUT POWER-UPS\n[white]Power-Ups provide temporary boosts to your defenses or offensive capabilities. Experiment with different power-ups such as force projectors to slow down enemies or regeneration projectors to heal structures. Use them strategically to turn the tide of battle in your favor.",
            "\ue837[#FFD700] ABOUT POWERS\n[white]Superpowers are powerful abilities that can be activated during critical moments in battle. These abilities range from devastating attacks to defensive buffs. Collect and deploy them wisely to overcome challenging enemy waves and protect your core from destruction.",
            "\ue837[#FFD700] ABOUT CASH\n[white]Cash is not just currency; it's your lifeline in battle. Convert surplus resources into cash to unlock new technologies and upgrade existing defenses. Invest wisely to access exclusive abilities and perks that can turn the tide of battle in your favor.",
            "\ue837[#FFD700] ABOUT UNIT PROCUREMENT\n[white]Browse and purchase units through the shop system to bolster your defenses. Choose from a variety of units, each with unique strengths and weaknesses. Strategically deploy units to complement your defensive strategy and maximize your chances of victory."
    };

    static {
        registerMenu();
    }

    public static void execute(Player player) {
        openGui(player);
    }

    public static void registerMenu() {
        menu = Menus.registerMenu((player, option) -> dynamicListeners.get(menu).accept(player, option));
    }

    public static void openGui(Player player) {
        dynamicListeners.put(menu, Info::handleMenuOption);
        Call.menu(player.con, menu, "Info", messages[currentMessageIndex], buttons);
    }

    private static void nextMessage(Player player) {
        currentMessageIndex = (currentMessageIndex + 1) % messages.length;
        openGui(player);
    }

    private static void handleMenuOption(Player player, int option) {
        if (option == 1) {
            nextMessage(player); // Next
        }
    }
}
