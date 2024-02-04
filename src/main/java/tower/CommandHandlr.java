package tower;

import arc.Events;
import arc.util.CommandHandler;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.mod.Plugin;

import java.util.Objects;

public class CommandHandlr extends Plugin {
    @Override
    public void registerClientCommands(CommandHandler handler){
        handler.<Player>register("store", "<player/building>", "Checks the store for a upgrade/buff", (args, player) -> {
            if (args.length == 0) {
                player.sendMessage("[RED]Missing Args[BLUE] Usage: /store <player/build>");
            } else {
                if (Objects.equals(args[0], "player")){
                    player.sendMessage("Upgrade for player ");
                    String[][] aa = {{"1", "DESC", "DESC1"}, {"2", "DESC2", "DESC3"}};
                    Call.menu(1, "Title", "MEssage1\nMessage2\nMessage3", aa);

                } else if (args[0].equalsIgnoreCase("build") || args[0].equalsIgnoreCase("building")) {
                    player.sendMessage("Upgrade for building ");
                } else {
                    player.sendMessage("[RED]Unknwon Option " + args[0]);
                }
            }
        });
    }

    @Override
    public void init() {
        Events.on(EventType.MenuOptionChooseEvent.class, event -> {
            System.out.println(event.player);
            System.out.println(event.menuId);
            System.out.println(event.option);
        });
    }
}
