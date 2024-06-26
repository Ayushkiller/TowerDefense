package tower.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Players;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;

public class Units {
  private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static final Map<Integer, BiConsumer<Player, Integer>> dynamicListeners =  new HashMap<>();
    private static final Map<String, Integer> menuIds =  new HashMap<>();

    private static final String[][] tierButtons = new String[6][1];
    private static int tierMenuId;
    static {
        registerTierMenu();
        for (int i = 0; i < 6; i++) {
            tierButtons[i][0] = "[cyan]Tier " + i;
        }
    }

    public static void initUnitsTable() {
        for (Map<String, Object> unitMap : UnitsTable.units) {
            UnitType unitType = (UnitType) unitMap.get("unit");
            int price = (int) unitMap.get("price");
            price = (int) (price * 1.4);
            unitPrices.put(unitType, price);
        }
    }

    public static void execute(Player player) {
        openTierMenuGui(player);
    }

    public static void registerTierMenu() {
        tierMenuId = Menus.registerMenu((player, option) -> dynamicListeners.getOrDefault(tierMenuId, (p, o) -> p.sendMessage("Error: Invalid menu handler")).accept(player, option));
        dynamicListeners.put(tierMenuId, Units::handleTierMenuOption);
    }

    private static void openTierMenuGui(Player player) {
        Call.menu(player.con, tierMenuId, "Select Tier", "", tierButtons);
    }

    private static void handleTierMenuOption(Player player, int option) {
        if (option >= 0 && option < 6) {
            openTierUnitsMenuGui(option, player);
        } else {
            player.sendMessage("Invalid selection. Please try again.");
        }
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

        int menuId = menuIds.computeIfAbsent("tierUnitsMenu_" + tier, k -> 
            Menus.registerMenu((player1, option) -> {
                if (option >= 0 && option < tierUnits.size()) {
                    Map<String, Object> unitMap = tierUnits.get(option);
                    UnitType unitType = (UnitType) unitMap.get("unit");
                    openUnitMenuGui(unitType, player);
                } else {
                    player.sendMessage("Invalid selection. Please try again.");
                }
            })
        );

        Call.menu(player.con, menuId, "Select Unit", "", buttons);
    }

    private static void openUnitMenuGui(UnitType unitType, Player player) {
        int price = unitPrices.get(unitType);
        String message = unitType.emoji() + "\n\n" +
                "Health: " + (int) unitType.health + "\n" +
                "Armor: " + (int) unitType.armor + "\n" +
                "Price: " + price;

        int menuId = menuIds.computeIfAbsent("unitMenu_" + unitType, k -> 
            Menus.registerMenu((player1, option) -> {
                switch (option) {
                    case 0 -> buyUnit(unitType, player, true);
                    case 1 -> openTierMenuGui(player);
                    case 2 -> openQuantityAdjustmentMenu(unitType, player, 1);
                }
            })
        );

        Call.menu(player.con, menuId, "Unit Info", message, new String[][] {
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

        int menuId = menuIds.computeIfAbsent("quantityMenu_" + unitType, k -> 
            Menus.registerMenu((p, opt) -> {
                if (opt < 3) { // Adjustment buttons
                    int adjustment = opt == 0 ? -1 : (opt == 1 ? 1 : 10);
                    int currentQuantity = defaultQuantity + adjustment;
                    if (currentQuantity < 1) {
                        player.sendMessage("[red]Quantity cannot be less than 1.");
                        openQuantityAdjustmentMenu(unitType, player, 1); // Reset to minimum 1
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
            })
        );

        Call.menu(player.con, menuId, title, message, buttons);
    }

    private static void buyUnit(UnitType unitType, Player player, boolean shouldControlUnit) {
        PlayerData playerData = Players.getPlayer(player);
        int price = unitPrices.get(unitType);
        if (playerData.getCash() >= price) {
            playerData.subtractCash((float) price, player);
            Unit spawned = unitType.spawn(player);
            if (shouldControlUnit) {
                Call.unitControl(player, spawned);
                player.sendMessage("Unit bought successfully.");
            }
        } else {
            player.sendMessage("Not enough funds.");
        }
    }

    private static void buyMultipleUnits(UnitType unitType, Player player, int quantity) {
        int totalCost = unitPrices.get(unitType) * quantity;
        PlayerData playerData = Players.getPlayer(player);

        if (playerData.getCash() >= totalCost) {
            for (int i = 0; i < quantity; i++) {
                buyUnit(unitType, player, false);
            }
            playerData.subtractCash(totalCost, player);
            player.sendMessage("[green]Units purchased successfully.");
        } else {
            player.sendMessage("[red]You don't have enough funds to buy " + quantity + " units.");
        }
    }

    public static void clearMenuIds() {
        menuIds.clear();
        dynamicListeners.clear();
    }
}
