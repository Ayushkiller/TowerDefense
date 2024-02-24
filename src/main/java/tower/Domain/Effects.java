package tower.Domain;


import mindustry.content.StatusEffects;
import mindustry.type.StatusEffect;


/**
 * Represents the currency system for the tower defense game.
 * This class contains static arrays that define the items used as currency,
 * the points gained for each item, and the prices for purchasing items.
 */
public class Effects {

public static StatusEffect[][] Effects = {
    // Tier   1
    { StatusEffects.overdrive, StatusEffects.fast, StatusEffects.overclock},
    // Tier   2
    {StatusEffects.shielded, StatusEffects.slow, StatusEffects.boss}
};

    /**
     * A  2D array representing the prices for purchasing items in the currency system.
     * Each row corresponds to a tier of items, and each column within a row represents the price for an item within that tier.
     */
    public static Integer[][] Priceforeffects = {
        {15,  15,  15},
        {25,  25,  25}
    };
    public static Integer[][] Timetoapplyfor = {
        {1500,  1500,  1500},
        {2500,  2500,  2500}
    };
}

