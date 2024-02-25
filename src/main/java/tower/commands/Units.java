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
    private static final int menu = Menus.registerMenu((player, option) -> {
        int rows = UnitsTable.units.length;
        int columns = UnitsTable.units[0].length;
        int row = option / columns;
        int column = option % columns;

        // Ensure the row and column indices are within bounds
        if (row >=   0 && row < rows && column >=   0 && column < columns) {
            UnitType unitType = UnitsTable.units[row][column];
            openUnitMenuGui(unitType, player);
        } else {
            // Handle the case where the indices are out of bounds
            // For example, you might want to show an error message to the player or log the error
            player.sendMessage("Invalid selection. Please try again.");
        }
    });

    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static String[][] buttons; // Declare the buttons variable here

    public static void initUnitsTable() {
        // Initialize the buttons array based on the size of UnitsTable.units
        int rows = UnitsTable.units.length;
        int columns = UnitsTable.units[0].length;
        buttons = new String[rows][columns];

        for (int i =   0; i < UnitsTable.units.length; i++) {
            for (int j =   0; j < UnitsTable.units[i].length; j++) {
                UnitType unit = UnitsTable.units[i][j];
                buttons[i][j] = unit.emoji(); // Only include the unit's emoji
                unitPrices.put(unit, UnitsTable.prices[i][j]); // Keep the price mapping for other uses
            }
        }
    }
    private static void buyUnit(UnitType unitType, Player player) {
        logUnitType(unitType);
        PlayerData playerData = Players.getPlayer(player);
        int price = unitPrices.get(unitType);
        if (playerData.getPoints() >= price) {
    
            playerData.subtractPoints((float) price, player);
    
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
                        playerData.addPoints((float) price, player);
                        player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
                        player.sendMessage(Bundle.get("unit.died", player.locale));
                    }
                });
    
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

    @SuppressWarnings("unused")
    private static void openUnitMenuGui(UnitType unitType, Player player) {
        // Initialize the buttons array based on the size of UnitsTable.units
        int rows = UnitsTable.units.length;
        int columns = UnitsTable.units[0].length;
        buttons = new String[rows][columns];

        // Iterate through rows
        for (int row =  0; row < UnitsTable.units.length; row++) {
            // Iterate through columns
            for (int col =  0; col < UnitsTable.units[row].length; col++) {
                // Calculate the index based on the current row and column
                int index = row * UnitsTable.units[row].length + col;

                // Check if the calculated index matches the unitType
                if (UnitsTable.units[row][col] == unitType) {
                    // Use row and col to access elements in UnitsTable.units and UnitsTable.prices
                    UnitType selectedUnitType = UnitsTable.units[row][col];
                    int price = UnitsTable.prices[row][col];

                    // Set up the menu for the selected unit
                    String[][] buttons = {
                            {"[lime]Buy"},
                            {"[lightgray]Back", "[gray]Close"}
                    };

                    String message = selectedUnitType.emoji() + "\n" + "\n" +
                            Bundle.get("menu.units.info.health", player.locale) + " " + (int) selectedUnitType.health + "\n" +
                            Bundle.get("menu.units.info.armor", player.locale) + " " + (int) selectedUnitType.armor + "\n" +
                            Bundle.get("menu.units.info.price", player.locale) + " " + price;

                    int menu = Menus.registerMenu(((player1, option) -> {
                        switch (option) {
                            case  0 -> buyUnit(selectedUnitType, player);
                            case  1 -> openGui(player);
                        }
                    }));

                    Call.menu(player.con, menu, Bundle.get("menu.units.title"), message, buttons);
                    return; // Exit the method once the matching unit is found
                }
            }
        }

        // If no matching unit is found, send an error message
        player.sendMessage("Invalid selection. Please try again.");
    }
 }

