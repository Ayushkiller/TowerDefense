package tower.menus;

import mindustry.core.World;

public class MenuManager {
    private World world;
    private float playerX;
    private float playerY;

    public MenuManager(World world, float playerX, float playerY) {
        this.world = world;
        this.playerX = playerX;
        this.playerY = playerY;
    }

    // Getter methods for world, playerX, and playerY
    public World getWorld() {
        return world;
    }

    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }
}