package tower.Domain;

import java.util.Collection;
import java.util.Map;

import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import tower.Players;

public class PlayerData {
    private static int totalPlayers = 0;
    private Unit unit;
    private float hp;
    private String uuid;
    private float lastUpdatedCash;
    private String name;
    private boolean showStats;
    private float Cash;
    private Team team;
   public static int getTotalPlayers() {
        return totalPlayers;
    }

    public void addCash(float amount, Player player) {
        this.Cash += amount;

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
    public Team getTeam() {
        return team;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerData(Player player) {
        this.uuid = player.uuid();
        this.team= player.team();
        totalPlayers++;
        player.ip();

    }
public static Collection<PlayerData> getAllPlayers() {
    // Retrieve the map of all players from Players.java
    Map<String, PlayerData> allPlayersMap = Players.players;

    // Return the values of the map, which are PlayerData instances
    return allPlayersMap.values();
}
}