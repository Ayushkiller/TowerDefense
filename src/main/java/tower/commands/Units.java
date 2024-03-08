package tower.commands;

import mindustry.entities.abilities.Ability;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Domain.Abilities;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;
import tower.Players;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Units {
    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static final Map<UnitType, String> unitNames = new HashMap<>();

    public static void initUnitsTable() {
        for (Map<String, Object> unitMap : UnitsTable.units) {
            UnitType unitType = (UnitType) unitMap.get("unit");
            String name = (String) unitMap.get("name");
            int price = (int) unitMap.get("price");
            unitNames.put(unitType, name);
            unitPrices.put(unitType, price);
        }
    }

private static void buyUnit(UnitType unitType, Player player) {
    PlayerData playerData = Players.getPlayer(player);
    int price = unitPrices.get(unitType);
    if (playerData.getPoints() >= price) {
        playerData.subtractPoints((float) price, player);

        Unit oldUnit = player.unit();
        Unit spawned = unitType.spawn(player.x, player.y);

        if (spawned != null && !spawned.dead()) {
            Call.unitControl(player, spawned);
            oldUnit.kill();

            // Check if the unit has an ability defined in UnitsTable.java
            Map<String, Object> unitMap = UnitsTable.units.stream()
                    .filter(u -> u.get("unit").equals(unitType))
                    .findFirst()
                    .orElse(null);


        if (unitMap != null && unitMap.containsKey("Ability")) {
            int abilityIndex = (int) unitMap.get("Ability");
            switch (abilityIndex) {
                case 1:
                    spawned.abilities = Abilities.getAbility1().toArray(new Ability[0]);
                    break;
                case 2:
                    spawned.abilities = Abilities.getAbility2().toArray(new Ability[0]);
                    break;
                case 3:
                    spawned.abilities = Abilities.getAbility3().toArray(new Ability[0]);
                    break;
                case 4:
                    spawned.abilities = Abilities.getAbility4().toArray(new Ability[0]);
                    break;
                case 6:
                    spawned.abilities = Abilities.getAbility6().toArray(new Ability[0]);
                    break;
                case 7:
                    spawned.abilities = Abilities.getAbility7().toArray(new Ability[0]);
                    break;
                case 8:
                    spawned.abilities = Abilities.getAbility8().toArray(new Ability[0]);
                    break;
                case 9:
                    spawned.abilities = Abilities.getAbility9().toArray(new Ability[0]);
                    break;
                case 10:
                    spawned.abilities = Abilities.getAbility10().toArray(new Ability[0]);
                    break;
            }
        }
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                if (spawned.dead()) {
                    playerData.addPoints((float) price, player);
                    player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
                    player.sendMessage(Bundle.get("unit.died", player.locale));
                }
            }, 3, TimeUnit.SECONDS);

            player.sendMessage(Bundle.get("unit.brought", player.locale));
        } else {
            playerData.addPoints((float) price, player);
            player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
        }
    } else {
        player.sendMessage(Bundle.get("menu.units.not-enough", player.locale()));
    }
}
    private static void openTierMenuGui(Player player) {
        String[][] buttons = new String[7][1]; 
        for (int i = 0; i < 7; i++) {
            buttons[i][0] = "Tier " + i;
        }
        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            if (option >= 0 && option < 7) {
                openTierUnitsMenuGui(option, player); 
            } else {
                player.sendMessage("Invalid selection. Please try again.");
            }
        }), "Select Tier", "", buttons);
    }

    private static void openTierUnitsMenuGui(int tier, Player player) {
        List<Map<String, Object>> tierUnits = UnitsTable.units.stream()
                .filter(unit -> (int) unit.get("tier") == tier)
                .collect(Collectors.toList());

        String[][] buttons = new String[tierUnits.size()][1];
        for (int i = 0; i < tierUnits.size(); i++) {
            Map<String, Object> unitMap = tierUnits.get(i);
            UnitType unitType = (UnitType) unitMap.get("unit");
            String name = (String) unitMap.get("name");
            buttons[i][0] = unitType.emoji() + " " + name;
        }
        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            if (option >= 0 && option < tierUnits.size()) {
                Map<String, Object> unitMap = tierUnits.get(option);
                UnitType unitType = (UnitType) unitMap.get("unit");
                openUnitMenuGui(unitType, player);
            } else {
                player.sendMessage("Invalid selection. Please try again.");
            }
        }), "Select Unit", "", buttons);
    }

    private static void openUnitMenuGui(UnitType unitType, Player player) {
        int price = unitPrices.get(unitType);
        String message = unitType.emoji() + "\n" + "\n" +
                Bundle.get("menu.units.info.health", player.locale) + " " + (int) unitType.health + "\n" +
                Bundle.get("menu.units.info.armor", player.locale) + " " + (int) unitType.armor + "\n" +
                Bundle.get("menu.units.info.price", player.locale) + " " + price;

        int menu = Menus.registerMenu(((player1, option) -> {
            switch (option) {
                case 0 -> buyUnit(unitType, player);
                case 1 -> openGui(player);
            }
        }));

        Call.menu(player.con, menu, Bundle.get("menu.units.title"), message, new String[][] {
            {"[lime]Buy"},
            {"[lightgray]Back", "[gray]Close"}
        });
    }

    public static void execute(Player player) {
        openTierMenuGui(player);
    }

    private static void openGui(Player player) {
        openTierMenuGui(player);
    }
}