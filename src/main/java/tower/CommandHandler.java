package tower;

import arc.Events;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.mod.Plugin;
import mindustry.type.UnitType;

import java.util.HashMap;
import java.util.Objects;

public class CommandHandler extends Plugin {
    private final Store store = new Store();
    @Override
    public void registerClientCommands(arc.util.CommandHandler handler){
        handler.<Player>register("change-unit", "<unitType>", "Change your unit.", (args, player) -> {
            UnitType unitType = Vars.content.getByName(ContentType.unit, args[0].toLowerCase());
            if (unitType != null) {
                Unit newUnit = unitType.create(player.team());
                newUnit.set(player.x, player.y);
                newUnit.add();
                player.unit(newUnit);
            } else {
                player.sendMessage("[scarlet]No unit type named '" + args[0] + "' found!");
            }
        });

        handler.<Player>register("store", "<player/building>", "Checks the store for a upgrade/buff", (args, player) -> {
            if (args.length == 0) {
                player.sendMessage("[RED]Missing Args[BLUE] Usage: /store <player/build>");
            } else {
                HashMap<String, Boolean> user_purchased_hashmap = store.get_purchased_list_player(player);
                StringBuilder user_purchased = new StringBuilder();

                for (HashMap.Entry<String, Boolean> entry : user_purchased_hashmap.entrySet())
                    if (entry.getValue()) // if value is true
                        user_purchased.append(entry.getKey()).append(", ");

                // Remove the trailing comma and space if needed
                if (!user_purchased.isEmpty()) user_purchased.setLength(user_purchased.length() - 2); // Removes the last comma and space
                else user_purchased.append("None");

                if (Objects.equals(args[0], "player")){
                    player.sendMessage("Upgrade for player ");
                    String[][] aa = {{"Close", "View Store"}};
                    Call.menu(player.con(), 1, "Shop", "User Store\nUser has Purchased: "+user_purchased, aa);
                } else if (args[0].equalsIgnoreCase("build") || args[0].equalsIgnoreCase("building")) {
                    player.sendMessage("Upgrade for building ");
                } else {
                    player.sendMessage("[RED]Unknown Option " + args[0]);
                }
            }
        });
    }

    @Override
    public void init() {
        store.init();
        System.out.println("[Command Handler] Initiated");
        Events.on(EventType.MenuOptionChooseEvent.class, event -> {
            int MenuID = event.menuId;
            int ButtonID = event.option;
            Player player = event.player;
            switch(MenuID) {
                case 1: // Shop-1
                    /*
                    * ButtonIDS
                    * 0 - Close
                    * 1 - View Shop
                    */
                    if (ButtonID == 1) {
                        String[][] button1 = {
                                {"Purchase 'Mega' Upgrade", "Purchase 'Poly' Upgrade"},
                                {"Close", "Next Page"}
                        };
                        Call.menu(2, "View Shop [1/1]", "Beta Upgrade -- ??\nGamma Upgrade -- ??", button1);
                    }
                    break;
                case 2: // Shop Page 1
                    /*
                     * ButtonIDS
                     * 0 - beta_upgrade
                     * 1 - View gamma_upgrade
                     */
                    if (ButtonID == 0) {
                        store.FreePurchase(player, "beta_upgrade");
                        UnitType unitType = Vars.content.getByName(ContentType.unit, "mega");
                        if (unitType != null) {
                            Unit newUnit = unitType.create(player.team());
                            newUnit.set(player.x, player.y);
                            newUnit.add();
                            player.unit(newUnit);
                        }
                    }
                    else if (ButtonID == 1) {
                        store.FreePurchase(player, "gamma_upgrade");
                        UnitType unitType = Vars.content.getByName(ContentType.unit, "poly");
                        if (unitType != null) {
                            Unit newUnit = unitType.create(player.team());
                            newUnit.set(player.x, player.y);
                            newUnit.add();
                            player.unit(newUnit);
                        }
                    }
                    break;
            }
        });
    }
}
