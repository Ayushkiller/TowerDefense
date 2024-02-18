package tower.Domain;

import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.world.Tile;
@SuppressWarnings("unused")
public class PlayerData {
    
    private String name;
    private String uuid;
    private String ip;

    private float hp;
    private Unit unit;

    private boolean showStats;
    private int kills;
    private float points;

    private Tile lastTile;
    private Team team;


    public void addKills (int amount) {
        this.kills += amount;
    }

    public void setKills (int amount) {
        this.kills = amount;
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

    public Unit getUnit() {
        return unit;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public PlayerData(Player player) {
        this.name = player.name();
        this.uuid = player.uuid();
        this.ip = player.ip();

        this.hp = player.unit().health();

        this.points = 5;
        this.team = player.team();
    }
}

