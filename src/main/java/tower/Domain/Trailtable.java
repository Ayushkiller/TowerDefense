package tower.Domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trailtable {
    public static List<Map<String, Object>> trails = new ArrayList<>();

    static {
        
        Map<String, Object> trail1 = new HashMap<>();
        trail1.put("type", "Basic");
        trail1.put("color", Color.RED);
        trails.add(trail1);

        Map<String, Object> trail2 = new HashMap<>();
        trail2.put("type", "Advanced");
        trail2.put("color", Color.BLUE);
        trails.add(trail2);

        
    }
}

