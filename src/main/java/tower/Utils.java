package tower;

import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.gen.Player;
import mindustry.type.UnitType;

public class Utils {

    public static void sendSuccess(Player player, String text) {
        player.sendMessage("[#00f]\uE800[green] " + text);
    }

    public static void sendFailure(Player player, String text) {
        player.sendMessage("[#f]⚠ " + text);
    }

    public static UnitType parseUnit(String s) {
        if (s.equals("oct"))
            return UnitTypes.oct;
        for (UnitType type : Vars.content.units()) {
            if (type.name.startsWith(s) || type.name.contains(s))
                return type;
            if (type.hasEmoji())
                if (s.trim().contains(type.emoji()))
                    return type;
        }
        return null;
    }
}
