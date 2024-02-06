package tower.commands;

import arc.struct.ObjectMap;
import mindustry.gen.Player;
import mindustry.type.UnitType;
import tower.menus.MenuHandler;

import static tower.Utils.*;

public class ClientCommands {


    public static void register(arc.util.CommandHandler handler) {
        handler.<Player>register("changeunit", "<unitType>", "Change your unit.", (args, player)->{
            UnitType unitType = parseUnit(args[0]);

            if(unitType == null){
                sendFailure(player, "That is not a valid unit type.");
                return;
            }

            player.unit(unitType.spawn(player.team(), player.x, player.y));
        });

        handler.<Player>register("store", "<player/building>", "Checks the store for a upgrade/buff", (args, player)->{
            ObjectMap<String, Boolean> userPurchaseObjectMap = Store.getPurchasedList(player);
            StringBuilder userPurchasedString = new StringBuilder();

            for(var e : userPurchaseObjectMap) if(e.value) userPurchasedString.append(e.key).append(", ");

            // Remove the trailing comma and space if needed
            if(!userPurchasedString.isEmpty()) userPurchasedString.setLength(userPurchasedString.length() - 2); // Removes the last comma and space
            else userPurchasedString.append("None");

            switch(args[0]){
                case "player" -> {
                    player.sendMessage("[accent]Upgrades for units");
                    MenuHandler.alphaUpgrade.setMessage("User Store\nUser has Purchased: " + userPurchasedString).show(player);
                }
                case "build", "building" -> {
                    player.sendMessage("[accent]Upgrades for buildings");
                    //Menu???
                }
                default -> {
                    sendFailure(player, "Invalid option.");
                }
            }
        });
    }
}
