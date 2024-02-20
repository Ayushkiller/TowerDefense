package tower.Domain;

import mindustry.content.Items;
import mindustry.type.Item;

public class Currency {
    
        public static Item [][] itemsforcore = {
            // Tier 1
            { Items.copper, Items.lead, Items.beryllium}
            , // Tier 2
            {Items.metaglass, Items.graphite, Items.silicon}
            , // Tier 3
            {Items.titanium, Items.thorium, Items.tungsten}
            , // Tier 4
            {Items.oxide, Items.phaseFabric, Items.surgeAlloy}

    };
    // this integer gain method is used to denoate how much points you will gain buy bying the items
    public static Integer[][] Gain = {

        {3, 1, 10}
        ,
        {25, 10, 30}
        ,
        {20, 30, 25}
        ,
        {35, 40, 325}

    };
    // this is sht of how much items will be removed from core
    public static Integer[][] Priceforitems = {
        
        {300, 100, 1000}
        ,
        {250, 100, 300}
        ,
        {200, 3000, 250}
        ,
        {350, 400, 3250}

    };
 
    
}
