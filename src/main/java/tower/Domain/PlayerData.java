package tower.Domain;



import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;



/**
 * Represents the data associated with a player in the tower defense game.
 * This class stores information such as the player's name, UUID, IP address, health points,
 * the unit they are controlling, points accumulated, and other game-related data.
 */
public class PlayerData {
    
    private Unit unit;
    private float hp;
    private Team team;

    private boolean showStats;
    private float points;

    public void addKills (int amount) {
    }

    public void setKills (int amount) {
    }

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
    public Team getTeam() {
        return team;
    }

    public float getHp() {
        return hp;
    }

    public PlayerData(Player player) {
        player.uuid();
        player.ip();
        this.hp = player.unit().health();
        this.team = player.team();
        this.points = 5;
    }
}