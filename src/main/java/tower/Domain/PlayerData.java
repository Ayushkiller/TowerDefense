package tower.Domain;



import mindustry.gen.Player;
import mindustry.gen.Unit;



/**
 * Represents the data associated with a player in the tower defense game.
 * This class stores information such as the player's name, UUID, IP address, health Cash,
 * the unit they are controlling, Cash accumulated, and other game-related data.
 */
public class PlayerData {

    private Unit unit;
    private float hp;
    private String uuid;
    private float lastUpdatedCash;
    private String name;
    private boolean showStats;
    private float Cash;

    public void addCashWithReduction(float reductionPercentage) {
        float baseCash =   1f; // Base Cash to be added
        float reduction = this.Cash * reductionPercentage /   100; // Calculate reduction
        float CashToAdd = baseCash - reduction; // Calculate Cash to add after reduction

        // Check if Cash to add is negative
        if (CashToAdd <=  0) {
            // If Cash to add is negative, do not add any Cash
            return;
        }

        // If Cash to add is positive, add them to the player's Cash
        this.Cash += CashToAdd;
    }
    public void addKills (int amount) {
    }

    public void setKills (int amount) {
    }

    public void addCash(float amount, Player player) {
        this.Cash += amount;
   
    }
    public float calculateReductionPercentage(float Cash) {

        return Cash *   0.9f;
    }
    public void subtractCash(float amount, Player player) {
        this.Cash -= amount;
    }
    public float getLastUpdatedCash() {
        return lastUpdatedCash;
    }
    
    public void setLastUpdatedCash(float lastUpdatedCash) {
        this.lastUpdatedCash = lastUpdatedCash;
    }
    public void setCash(float Cash, Player player) {
        this.Cash = Cash;

    }


    public float getCash() {
        return Cash;
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
    public String getName() {
        return name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public float getHp() {
        return hp;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }

    public PlayerData(Player player) {
        this.uuid = player.uuid();
        player.ip();
        this.hp = player.unit().health();
        this.name = player.name();
        this.Cash= 10;
    }
}