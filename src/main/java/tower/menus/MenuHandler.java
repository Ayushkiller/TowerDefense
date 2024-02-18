package tower.menus;



import arc.util.Log;
import mindustry.Vars;
import mindustry.content.StatusEffects;
import mindustry.ctype.ContentType;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import tower.commands.Store;


public class MenuHandler {

    public static Menu alphaUpgrade, betaUpgrade; //TODO replace this event garbage with menu

    public static void init() {
        Log.info("[Menu Handler] Initiated");

        alphaUpgrade = new Menu("shop", "User Store \n User has purchased: ", new String[][]{{"Close", "View Store"}}, (p, i)->betaUpgrade.show(p));

        betaUpgrade = new Menu("View Shop [1/1]", "Beta Upgrade -- ?? \nGamma Upgrade -- ??", new String[][]{
            {"Purchase 'Mega' Upgrade", "Purchase 'Poly' Upgrade"},
            {"Close", "Next Page"}
        }, (player, option)->{
            switch(option){
                case 0 -> {
                    Store.freePurchase(player, "beta_upgrade");
                    UnitType unitType = Vars.content.getByName(ContentType.unit, "mega");
                    if(unitType != null){
                        Unit newUnit = unitType.create(player.team());
                        newUnit.set(player.x, player.y);
                        newUnit.add();
                        newUnit.apply(StatusEffects.overclock, Float.POSITIVE_INFINITY);
                        player.unit(newUnit);
                    }
                }

                case 1 -> {
                    Store.freePurchase(player, "gamma_upgrade");
                    UnitType unitType = Vars.content.getByName(ContentType.unit, "poly");
                    if(unitType != null){
                        Unit newUnit = unitType.create(player.team());
                        newUnit.set(player.x, player.y);
                        newUnit.add();
                        newUnit.apply(StatusEffects.overclock, Float.POSITIVE_INFINITY);
                        player.unit(newUnit);
                    }
                }
            }
        });
    }
}
