package tower.Domain;



import mindustry.gen.Player;
import mindustry.gen.Unit;
import java.util.logging.Logger;


/**
 * Represents the data associated with a player in the tower defense game.
 * This class stores information such as the player's name, UUID, IP address, health points,
 * the unit they are controlling, points accumulated, and other game-related data.
 */
public class PlayerData {
    private static final Logger logger = Logger.getLogger(PlayerData.class.getName());
    private Unit unit;
    private float hp;
    private String uuid;
    private float lastUpdatedPoints;

    private boolean showStats;
    private float points;

    public void addKills (int amount) {
    }

    public void setKills (int amount) {
    }

    public void addPoints(float amount, Player player) {
        this.points += amount;
        logger.info("Player " + player.uuid() + " added " + amount + " points. Total points: " + this.points);
    }

    public void subtractPoints(float amount, Player player) {
        this.points -= amount;
        logger.info("Player " + player.uuid() + " subtracted " + amount + " points. Total points: " + this.points);
    }
    public float getLastUpdatedPoints() {
        return lastUpdatedPoints;
    }
    
    public void setLastUpdatedPoints(float lastUpdatedPoints) {
        this.lastUpdatedPoints = lastUpdatedPoints;
    }
    public void setPoints(float points, Player player) {
        this.points = points;
        logger.info("Player " + player.uuid() + " set points to " + points);
    }


    public float getPoints() {
        return points;
    }
    public Unit getUnit() {
        return unit;
    }
    
    public void setStats(boolean value) {
        this.showStats = value;
    }
    
    public boolean stats() {
        return showStats;
    }
    public void setHp(float hp) {
        this.hp = hp;
    }
    public String getUuid() {
        return this.uuid;
    }

    public float getHp() {
        return hp;
    }

    public PlayerData(Player player) {
        this.uuid = player.uuid();
        player.ip();
        this.hp = player.unit().health();
        this.points = 5;
    }
}