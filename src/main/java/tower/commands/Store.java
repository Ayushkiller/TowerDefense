package tower.commands;

import arc.struct.ObjectMap;
import arc.util.Log;
import mindustry.gen.Player;

public class Store {
    public static final ObjectMap<String, Boolean> defaultPurchases = new ObjectMap<>();
    public static final ObjectMap<Player, ObjectMap<String, Boolean>> purchases = new ObjectMap<>();

    public static void init() {
        defaultPurchases.put("beta_upgrade", false);
        defaultPurchases.put("gamma_upgrade", false);
        Log.info("[Store] Initialized");
    }

    public static void freePurchase(Player player, String item) {
        try{
            ObjectMap<String, Boolean> userPurchase = getPurchasedList(player);
            userPurchase.put(item, !userPurchase.get(item));
            purchases.put(player, userPurchase);
        }catch(Exception e){
            Log.err("Error:", e);
        }
    }

    public static ObjectMap<String, Boolean> getPurchasedList(Player player) {
        return purchases.get(player, defaultPurchases::copy);
    }
}
