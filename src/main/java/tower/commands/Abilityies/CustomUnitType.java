package tower.commands.Abilityies;

import mindustry.type.UnitType;
import mindustry.type.Weapon;
import java.util.ArrayList;
import java.util.List;

public class CustomUnitType {
    public UnitType originalUnitType;
    public float speed;
    public boolean alwaysShootWhenMoving;
    public boolean physics;
    public boolean autoFindTarget;
    public boolean alwaysUnlocked;
    public List<Weapon> weapons = new ArrayList<>();

    public CustomUnitType(UnitType originalUnitType) {
        this.originalUnitType = originalUnitType;
       
        this.speed = originalUnitType.speed; 
        this.alwaysShootWhenMoving = false; 
        this.physics = true; 
        this.autoFindTarget = false; 
        this.alwaysUnlocked = false; 
      
    }
}