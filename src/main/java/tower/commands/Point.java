package tower.commands;


import mindustry.gen.Call;
import mindustry.gen.Player;
import tower.Bundle;


public class Point {
    public static void point(Player player) {
        Call.label("[scarlet]\uE805", 5, player.mouseX, player.mouseY);
        Call.label(Bundle.format("commands.point", Bundle.findLocale(player.locale), player.name), 5, player.mouseX, player.mouseY-5);
    }

    public static void init() {
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }
}
