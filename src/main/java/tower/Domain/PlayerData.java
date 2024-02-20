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
    

    private float hp;
    private Unit unit;

    private boolean showStats;
    private float points;

    private Team team;


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
    
    public void setStats(boolean value) {
        this.showStats = value;
    }
    
    public boolean stats() {
        return showStats;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
/**
     * Sets the player's current unit.
     *
     * @param unit The new unit the player is controlling.
     */
    public Unit getUnit() {
        return unit;
    }
       /**
     * Returns the player's current unit.
     *
     * @return The player's current unit.
     */

    public float getHp() {
        return hp;
    }
   /**
     * Returns the player's current health points.
     *
     * @return The player's health points.
     */
    public void setHp(float hp) {
        this.hp = hp;
    }
    /**
     * Sets the player's health points to the specified value.
     *
     * @param hp The new health points value.
     */
    public Team getTeam() {
        return team;
    }
        /**
     * Returns the player's current team.
     *
     * @return The player's team.
     */

    public void setTeam(Team team) {
        this.team = team;
    }
    /**
     * Sets the player's team to the specified team.
     *
     * @param team The new team the player is part of.
     */
    public PlayerData(Player player) {

        player.ip();

        this.hp = player.unit().health();

        this.points = 5;
        this.team = player.team();
    }
}

