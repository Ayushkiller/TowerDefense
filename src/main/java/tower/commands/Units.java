package tower.commands;

import arc.Core;
import arc.util.Log;

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

@SuppressWarnings("unused")
public class Units {
//    static final String[] prefixes = {"[lime]", "[scarlet]", "[lightgray]"};

    private static final int menu = Menus.registerMenu((player, option) -> {
        UnitType unitType = UnitsTable.units[option / UnitsTable.units[0].length][option % UnitsTable.units[0].length];
        openUnitMenuGui(unitType, player);
    });

    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static String[][] buttons; // Declare the buttons variable here

    public static void initUnitsTable() {
        int row =   0;
        int column =   0;
    
        // Determine the number of rows and columns based on the size of the units array
        int numberOfRows = UnitsTable.units.length;
        int numberOfColumns = UnitsTable.units[0].length;
    
        // Initialize the buttons array with the correct size
        buttons = new String[numberOfRows][numberOfColumns];
    
        for (int i =  0; i < UnitsTable.units.length; i++) {
            for (int j =  0; j < UnitsTable.units[i].length; j++) {
                UnitType unit = UnitsTable.units[i][j];
                buttons[i][j] = unit.name.substring(0,  1).toUpperCase().concat(unit.name.substring(1));
                Log.info("i: " + i + ", j: " + j + ", unit: " + unit + ", price: " + UnitsTable.prices[i][j]);
                unitPrices.put(unit, UnitsTable.prices[i][j]);
            }
        }
        

    }
    private static void buyUnit(UnitType unitType, Player player) {
        PlayerData playerData = Players.getPlayer(player);
        int price = unitPrices.get(unitType);
        if (playerData.getPoints() >= price) {

            playerData.subtractPoints(unitPrices.get(unitType));

            Unit oldUnit = player.unit();
            Unit spawned = unitType.spawn(player.x, player.y);

            // Check if the spawned unit is alive
            if (spawned != null && !spawned.dead()) {
                playerData.setUnit(spawned);
                Call.unitControl(player, spawned);
                oldUnit.kill();

                player.sendMessage(Bundle.get("unit.brought", player.locale));
            } else {
                // Handle the case where the unit could not be spawned
                player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
            }
        } else {
            player.sendMessage(Bundle.get("menu.units.not-enough", player.locale()));
        }
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
