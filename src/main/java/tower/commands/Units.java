package tower.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;

public class Units {
    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static int tierMenuId;
    private static int[] tierUnitMenuIds = new int[6];
    private static int unitMenuId;
    private static int quantityAdjustmentMenuId;
    private static final Map<Integer, BiConsumer<Player, Integer>> dynamicListeners = new HashMap<>();

    public static void initUnitsTable() {
        for (Map<String, Object> unitMap : UnitsTable.units) {
            UnitType unitType = (UnitType) unitMap.get("unit");
            int price = (int) unitMap.get("price");
            price = (int) (price * 1.4);
            unitPrices.put(unitType, price);
        }
    }

    public static void registerMenus() {
        // Register the tier menu
        String[][] tierButtons = new String[6][1];
        for (int i = 0; i < 6; i++) {
            tierButtons[i][0] = "[cyan]Tier " + i;
        }
        tierMenuId = Menus.registerMenu((player, option) -> {
            if (option >= 0 && option < 6) {
                openTierUnitsMenuGui(option, player);
            } else {
                player.sendMessage("Invalid selection. Please try again.");
            }
        });

        // Register the tier unit menus
        for (int i = 0; i < 6; i++) {
            int tier = i;
            tierUnitMenuIds[i] = Menus.registerMenu((player, option) -> {
                dynamicListeners.get(tierUnitMenuIds[tier]).accept(player, option);
            });
        }

        // Register the unit menu
        unitMenuId = Menus.registerMenu((player, option) -> {
            dynamicListeners.get(unitMenuId).accept(player, option);
        });

        // Register the quantity adjustment menu
        quantityAdjustmentMenuId = Menus.registerMenu((player, option) -> {
            dynamicListeners.get(quantityAdjustmentMenuId).accept(player, option);
        });
    }

    public static void execute(Player player) {
        openTierMenuGui(player);
    }

    private static void openTierMenuGui(Player player) {
        String[][] buttons = new String[6][1];
        for (int i = 0; i < 6; i++) {
            buttons[i][0] = "[cyan]Tier " + i;
        }
        Call.menu(player.con, tierMenuId, "Select Tier", "", buttons);
    }

    private static void openTierUnitsMenuGui(int tier, Player player) {
        List<Map<String, Object>> tierUnits = UnitsTable.units.stream()
                .filter(unit -> (int) unit.get("tier") == tier)
                .toList();

        String[][] buttons = new String[tierUnits.size()][1];
        for (int i = 0; i < tierUnits.size(); i++) {
            Map<String, Object> unitMap = tierUnits.get(i);
            UnitType unitType = (UnitType) unitMap.get("unit");
            String name = (String) unitMap.get("name");
            buttons[i][0] = unitType.emoji() + " " + name;
        }

        dynamicListeners.put(tierUnitMenuIds[tier], (player1, option) -> {
            if (option >= 0 && option < tierUnits.size()) {
                Map<String, Object> unitMap = tierUnits.get(option);
                UnitType unitType = (UnitType) unitMap.get("unit");
                openUnitMenuGui(unitType, player);
            } else {
                player.sendMessage("Invalid selection. Please try again.");
            }
        });

        Call.menu(player.con, tierUnitMenuIds[tier], "Select Unit", "", buttons);
    }

    private static void openUnitMenuGui(UnitType unitType, Player player) {
        int price = unitPrices.get(unitType);
        String message = unitType.emoji() + "\n\n" +
                Bundle.get("menu.units.info.health", player.locale) + " " + (int) unitType.health + "\n" +
                Bundle.get("menu.units.info.armor", player.locale) + " " + (int) unitType.armor + "\n" +
                Bundle.get("menu.units.info.price", player.locale) + " " + price;

        dynamicListeners.put(unitMenuId, (player1, option) -> {
            switch (option) {
                case 0 -> buyUnit(unitType, player, true);
                case 1 -> openTierMenuGui(player);
                case 2 -> openQuantityAdjustmentMenu(unitType, player, 1);
            }
        });

        Call.menu(player.con, unitMenuId, Bundle.get("menu.units.title"), message, new String[][] {
                { "[lime]Buy" },
                { "[gray]Back" },
                { "[blue]Buy Multiple" }
        });
    }

    private static void openQuantityAdjustmentMenu(UnitType unitType, Player player, int defaultQuantity) {
        String title = "[red]Adjust Quantity";
        String[][] buttons = new String[][] { { "-1", "+1", "+10" }, { "[green]Buy", "[grey]Back", "[red]Close" } };
        int price = unitPrices.get(unitType);
        String message = "[green]Current Quantity: " + defaultQuantity + "\n[red]Total Price: " + (defaultQuantity * price);

        dynamicListeners.put(quantityAdjustmentMenuId, (p, opt) -> {
            if (opt < 3) { // Adjustment buttons
                int adjustment = Integer.parseInt(buttons[0][opt]);
                int currentQuantity = defaultQuantity + adjustment;
                if (currentQuantity < 1) {
                    player.sendMessage("[red]Quantity cannot be less than 1.");
                    return;
                }
                openQuantityAdjustmentMenu(unitType, player, currentQuantity);
            } else if (opt == 3) { // Buy button
                buyMultipleUnits(unitType, player, defaultQuantity);
            } else if (opt == 4) { // Back button
                openUnitMenuGui(unitType, player);
            } else if (opt == 5) { // Close button
                player.sendMessage("Purchase cancelled.");
            }
        });

        Call.menu(player.con, quantityAdjustmentMenuId, title, message, buttons);
    }

    private static void buyUnit(UnitType unitType, Player player, boolean shouldControlUnit) {
        PlayerData playerData = Players.getPlayer(player);
        int price = unitPrices.get(unitType);
        if (playerData.getCash() >= price) {
            playerData.subtractCash((float) price);
            Unit spawned = unitType.spawn(player);
            if (shouldControlUnit) {
                Call.unitControl(player, spawned);
                player.sendMessage(Bundle.get("unit.bought", player.locale()));
            }
        } else {
            player.sendMessage(Bundle.get("menu.units.not-enough", player.locale()));
        }
    }

    private static void buyMultipleUnits(UnitType unitType, Player player, int quantity) {
        int totalCost = unitPrices.get(unitType) * quantity;
        PlayerData playerData = Players.getPlayer(player);

        if (playerData.getCash() >= totalCost) {
            for (int i = 0; i < quantity; i++) {
                buyUnit(unitType, player, false);
            }
            playerData.subtractCash(totalCost);
            player.sendMessage("[green]Units purchased successfully.");
        } else {
            player.sendMessage("[red]You don't have enough funds to buy " + quantity + " units.");
        }
    }
}
