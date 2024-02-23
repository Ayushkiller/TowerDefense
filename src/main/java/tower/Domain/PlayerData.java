package tower.Domain;


import arc.struct.ObjectMap;
import mindustry.gen.Player;


/**
 * Represents the data associated with a player in the tower defense game.
 * This class stores information such as the player's name, UUID, IP address, health points,
 * the unit they are controlling, points accumulated, and other game-related data.
 */
public class PlayerData {
    
    public static ObjectMap<String, PlayerData> allPlayerDataInstances = new ObjectMap<>();
    private float hp;


    private boolean showStats;
    private float points;

    // In PlayerData.java, within the addPoints method
    public void addPoints (float amount) {
        this.points += amount;

    }
    
    

    public void subtractPoints (float amount) {
        this.points -= amount;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public float getPoints() {
        return points;
    }
    
    public void setStats(boolean value) {
        this.showStats = value;
    }
    
    public boolean stats() {
        return showStats;
    }


    public float getHp() {
        return hp;
    }

    public PlayerData(Player player) {

        player.ip();

        this.hp = player.unit().health();

        this.points = 0;
    }
}

