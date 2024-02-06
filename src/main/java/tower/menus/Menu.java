package tower.menus;

import mindustry.gen.*;
import mindustry.ui.Menus;

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
}
