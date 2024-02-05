package tower;

import mindustry.gen.Player;

import java.util.HashMap;

public class Store {
    public HashMap<String, Boolean> default_purchase = new HashMap<String, Boolean>();
    public HashMap<Player, HashMap<String, Boolean>> purchased = new HashMap<Player, HashMap<String, Boolean>>();
    public void init() {
        default_purchase.put("beta_upgrade", false);
        default_purchase.put("gamma_upgrade", false);
        System.out.println("[Store] Initialized");
    }
    public boolean FreePurchase(Player player, String item) {
        try {
            HashMap<String, Boolean> user_purchased = get_purchased_list(player);
            user_purchased.put(item, !user_purchased.get(item));
            purchased.put(player, user_purchased);
            return true;
        } catch(Exception e) {
            System.out.println("Error: " + e.toString());
            return false;
        }
    }
    public HashMap<String, Boolean> get_purchased_list(Player player) {
        if (purchased.containsKey(player)) return purchased.get(player);
        else {
            purchased.put(player, default_purchase);
            return purchased.get(player);
        }
    }
}
