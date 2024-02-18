package tower.menus;


import tower.commands.Settings;
import mindustry.gen.*;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.commands.Units;

public class Menu {
    private final int register;
    private final Menus.MenuListener listener;
    private final String[][] options;
    private String message;
    private String title;

    public Menu(String title, String message, String[][] options, Menus.MenuListener listener) {
        register = Menus.registerMenu(listener);
        this.listener = listener;
        this.message = message;
        this.options = options;
        this.title = title;
    }

    public void show(Player p) {
        Call.menu(p.con, register, title, message, options);
    }

    public void showAll() {
        Call.menu(register, title, message, options);
    }

    public int getRegister() {
        return register;
    }

    public String getMessage() {
        return message;
    }

    public Menu setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Menu setTitle(String title) {
        this.title = title;
        return this;
    }

    public String[][] getOptions() {
        return options;
    }

    public Menus.MenuListener getListener() {
        return listener;
    }
    private static final int menu = Menus.registerMenu((player, option) -> {
        switch (option) {
            case 0 -> Units.execute(player);
            case 1 -> Settings.execute(player);
            case 2 -> {}
        }
    });

    private static final String[][] buttons = {
            {"[lime]Units", "[red]Settings"},
            {"[cyan]Donate", "[lightgray]Close"}
    };

    public static void execute(Player player) {
        openGui(player);
    }

    public static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("menu.title", player.locale), "", buttons);
    }

    public static void init() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }
}
