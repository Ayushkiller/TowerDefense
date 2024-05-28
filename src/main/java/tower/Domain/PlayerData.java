package tower.Domain;

import mindustry.gen.Player;

public class PlayerData {
    private final String uuid;
    private float Cash;

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


    public PlayerData(Player player) {
        this.uuid = player.uuid();
        this.Cash = 0;
    }
}