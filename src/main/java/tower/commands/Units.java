package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;
import tower.Players;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Units {
    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static final Map<UnitType, String> unitNames = new HashMap<>();
    public static void initUnitsTable() {
        // Initialize the unitPrices and unitNames maps based on the size of UnitsTable.units
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

            // Spawn the unit for the player
            Unit oldUnit = player.unit();
            Unit spawned = unitType.spawn(player.x, player.y);

            // Check if the spawned unit is alive
            if (spawned != null && !spawned.dead()) {
                Call.unitControl(player, spawned);
                oldUnit.kill();

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.schedule(() -> {
                    if (spawned.dead()) {
                        // Return the item to the player
                        playerData.addPoints((float) price, player);
                        player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
                        player.sendMessage(Bundle.get("unit.died", player.locale));
                    }
                },   3, TimeUnit.SECONDS);

                player.sendMessage(Bundle.get("unit.brought", player.locale));
            } else {
                // Handle the case where the unit could not be spawned
                playerData.addPoints((float) price, player); // Return the points to the player
                player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
            }
        } else {
            player.sendMessage(Bundle.get("menu.units.not-enough", player.locale()));
        }
    }

    private static void openUnitMenuGui(UnitType unitType, Player player) {
        int price = unitPrices.get(unitType);
        String message = unitType.emoji() + "\n" + "\n" +
                Bundle.get("menu.units.info.health", player.locale) + " " + (int) unitType.health + "\n" +
                Bundle.get("menu.units.info.armor", player.locale) + " " + (int) unitType.armor + "\n" +
                Bundle.get("menu.units.info.price", player.locale) + " " + price;

        int menu = Menus.registerMenu(((player1, option) -> {
            switch (option) {
                case   0 -> buyUnit(unitType, player);
                case   1 -> openGui(player);
            }
        }));

        Call.menu(player.con, menu, Bundle.get("menu.units.title"), message, new String[][] {
            {"[lime]Buy"},
            {"[lightgray]Back", "[gray]Close"}
        });
    }

    public static void execute(Player player) {
        openGui(player);
    }

    private static void openGui(Player player) {
        String[][] buttons = new String[UnitsTable.units.size()][1];
        for (int i =   0; i < UnitsTable.units.size(); i++) {
            Map<String, Object> unitMap = UnitsTable.units.get(i);
            UnitType unitType = (UnitType) unitMap.get("unit");
            buttons[i][0] = unitType.emoji();
        }
        Call.menu(player.con, Menus.registerMenu((player1, option) -> {
            if (option >=   0 && option < UnitsTable.units.size()) {
                Map<String, Object> unitMap = UnitsTable.units.get(option);
                UnitType unitType = (UnitType) unitMap.get("unit");
                openUnitMenuGui(unitType, player);
            } else {
                player.sendMessage("Invalid selection. Please try again.");
            }
        }), Bundle.get("menu.units.title", player.locale()), "", buttons);
    }
}