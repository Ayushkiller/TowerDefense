package tower.commands;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Players;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;
import tower.Bundle;

import java.util.HashMap;
import java.util.Map;

import arc.util.Time;


public class Units {
//    static final String[] prefixes = {"[lime]", "[scarlet]", "[lightgray]"};

    private static final int menu = Menus.registerMenu((player, option) -> {
        UnitType unitType = UnitsTable.units[option / UnitsTable.units[0].length][option % UnitsTable.units[0].length];
        openUnitMenuGui(unitType, player);
    });

    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static String[][] buttons; // Declare the buttons variable here

    public static void initUnitsTable() {
        // Determine the number of rows and columns based on the size of the units array
        int numberOfRows = UnitsTable.units.length;
        int numberOfColumns = UnitsTable.units[0].length;

        // Check if the dimensions of UnitsTable.units and UnitsTable.prices match
        if (numberOfRows != UnitsTable.prices.length || numberOfColumns != UnitsTable.prices[0].length) {
            throw new IllegalStateException("The dimensions of UnitsTable.units and UnitsTable.prices do not match.");
        }

        // Initialize the buttons array with the correct size
        buttons = new String[numberOfRows][numberOfColumns];

        for (int i =  0; i < UnitsTable.units.length; i++) {
            for (int j =  0; j < UnitsTable.units[i].length; j++) {
                UnitType unit = UnitsTable.units[i][j];
                buttons[i][j] = unit.emoji();
                unitPrices.put(unit, UnitsTable.prices[i][j]);
            }
        }
    }
    private static void buyUnit(UnitType unitType, Player player) {
        logUnitType(unitType);
        PlayerData playerData = Players.getPlayer(player);
        int price = unitPrices.get(unitType);
        if (playerData.getPoints() >= price) {
    
            playerData.subtractPoints(price);
    
            Unit oldUnit = player.unit();
            Unit spawned = unitType.spawn(player.x, player.y);
    
            // Check if the spawned unit is alive
            if (spawned != null && !spawned.dead()) {
                Call.unitControl(player, spawned);
                oldUnit.kill();
    
                // Check if the unit dies within  3 seconds
                Time.run(3f, () -> {
                    if (spawned.dead()) {
                        // Return the item to the player
                        playerData.addPoints(price);
                        player.sendMessage(Bundle.get("unit.died", player.locale));
                    }
                });
    
                player.sendMessage(Bundle.get("unit.brought", player.locale));
            } else {
                // Handle the case where the unit could not be spawned
                playerData.addPoints(price); // Return the points to the player
                player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
            }
        } else {
            player.sendMessage(Bundle.get("menu.units.not-enough", player.locale()));
        }
    }
    // Add a method to log the unit type ID and the length of the prices array
   private static void logUnitType(UnitType unitType) {
    System.out.println("Unit type ID: " + unitType.id);
    System.out.println("Length of prices array: " + UnitsTable.prices.length);
     }
    

    public static void execute(Player player) {
        openGui(player);
    }

    private static void openGui(Player player) {
        Call.menu(player.con, menu, Bundle.get("menu.units.title", player.locale), "", buttons);
    }

    private static void openUnitMenuGui(UnitType unitType, Player player) {
        String[][] buttons = {
                {"[lime]Buy"},
                {"[lightgray]Back", "[gray]Close"}
        };

        String message = unitType.emoji() + "\n" + "\n" +
                Bundle.get("menu.units.info.health", player.locale) + " " + (int) unitType.health + "\n" +
                Bundle.get("menu.units.info.armor", player.locale) + " " + (int) unitType.armor + "\n" +
                Bundle.get("menu.units.info.price", player.locale) + " " + unitPrices.get(unitType);

        int menu = Menus.registerMenu(((player1, option) -> {
            switch (option) {
                case 0 -> buyUnit(unitType, player);
                case 1 -> openGui(player);
            }
        }));

        Call.menu(player.con, menu, Bundle.get("menu.units.title"), message, buttons);
    }
}
