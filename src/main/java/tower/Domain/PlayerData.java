package tower.Domain;

import java.util.HashMap;
import java.util.Map;

import mindustry.gen.Player;

public class PlayerData {
    private float Cash;
 private static Map<String, PlayerData> playerDataInstances = new HashMap<>();
    public void addCash(float amount) {
        this.Cash += amount;

    }

    public void subtractCash(float amount) {
        this.Cash -= amount;
    }

    public void setCash(float Cash) {
        this.Cash = Cash;

    }

    public float getCash() {
        return Cash;
    }

    public static PlayerData get(Player player) {
        String playerId = player.uuid().toString(); 
        return playerDataInstances.computeIfAbsent(playerId, id -> new PlayerData(player));
    }
    public PlayerData(Player player) {
        player.uuid();
        this.Cash = 0;
    }

}