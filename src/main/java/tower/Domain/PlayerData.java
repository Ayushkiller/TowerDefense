package tower.Domain;

import mindustry.gen.Player;

public class PlayerData {
    private String uuid;
    private float Cash;

    public void addCash(float amount, Player player) {
        this.Cash += amount;

    }

    public void subtractCash(float amount, Player player) {
        this.Cash -= amount;
    }

    public void setCash(float Cash, Player player) {
        this.Cash = Cash;

    }

    public float getCash() {
        return Cash;
    }

    public String getUuid() {
        return this.uuid;
    }

    public PlayerData(Player player) {
        this.uuid = player.uuid();
        this.Cash = 0;
    }
}