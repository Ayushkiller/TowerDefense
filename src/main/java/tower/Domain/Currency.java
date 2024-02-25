package tower.Domain;

import mindustry.content.Items;
import mindustry.type.Item;

/**
 * Represents the currency system for the tower defense game.
 * This class contains static arrays that define the items used as currency,
 * the points gained for each item, and the prices for purchasing items.
 */
public class Currency {
    /**
     * A  2D array representing the items that can be used as currency.
     * Each row represents a tier of items, and each column within a row represents an item within that tier.
     */
    public static Item[][] itemsforcore = {
        // Tier  1
        { Items.copper, Items.lead, Items.beryllium},
        // Tier  2
        {Items.metaglass, Items.graphite, Items.silicon},
        // Tier  3
        {Items.titanium, Items.thorium, Items.tungsten},
        // Tier  4
        {Items.oxide, Items.phaseFabric, Items.surgeAlloy}
    };

    /**
     * A  2D array representing the points gained for each item in the currency system.
     * Each row corresponds to a tier of items, and each column within a row represents the points gained for an item within that tier.
     */
    public static Integer[][] Gain = {
        {1,  1,  1},
        {5,  5,  5},
        {8,  8,  8},
        {15,  15,  15}
    };

    /**
     * A  2D array representing the prices for purchasing items in the currency system.
     * Each row corresponds to a tier of items, and each column within a row represents the price for an item within that tier.
     */
    public static Integer[][] Priceforitems = {
        {25,  25,  25},
        {15,  15,  15},
        {50,  50,  50},
        {30,  30,  30}
    };
  public static Integer[][] MinQuantity = {
    {1500,  1500,  1500}, // Tier  1
    {1500,  1500,  1500}, // Tier  2
    {1500,  1500,  1500}, // Tier  3
    {1500,  1500,  1500}  // Tier  4
  };
}
