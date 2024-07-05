package tower.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Players;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;

public class Units {
    private static final Logger logger = Logger.getLogger(Units.class.getName());
    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static final Map<Integer, BiConsumer<Player, Integer>> dynamicListeners = new HashMap<>();
    private static final Map<String, Integer> menuIds = new HashMap<>();
    private static final String[][] tierButtons = new String[6][1];
    private static int tierMenuId;

    static {
        registerTierMenu();
        for (int i = 0; i < 6; i++) {
            tierButtons[i][0] = "[cyan]Tier " + i;
        }
        initUnitsTable();
    }

    public static void initUnitsTable() {
        for (Map<String, Object> unitMap : UnitsTable.units) {
            UnitType unitType = (UnitType) unitMap.get("unit");
            int price = (int) unitMap.get("price");
            price = (int) (price * 1.4);
            unitPrices.put(unitType, price);
        }
        logger.log(Level.INFO, "Initialized units table with prices: {0}", unitPrices);
    }

    public static void execute(Player player) {
        logger.log(Level.INFO, "Executing Units command for player: {0}", player.name);
        openTierMenuGui(player);
    }

    public static void registerTierMenu() {
        tierMenuId = Menus.registerMenu((player, option) -> dynamicListeners.getOrDefault(tierMenuId, (p, o) -> p.sendMessage("Error: Invalid menu handler")).accept(player, option));
        dynamicListeners.put(tierMenuId, Units::handleTierMenuOption);
        logger.log(Level.INFO, "Tier menu registered with ID: {0}", tierMenuId);
    }

    private static void openTierMenuGui(Player player) {
        logger.log(Level.INFO, "Opening Tier menu GUI for player: {0}", player.name);
        Call.menu(player.con, tierMenuId, "Select Tier", "", tierButtons);
    }

    private static void handleTierMenuOption(Player player, int option) {
        if (option >= 0 && option < 6) {
            logger.log(Level.INFO, "Player {0} selected Tier {1}", new Object[]{player.name, option});
            openTierUnitsMenuGui(option, player);
        } else {
            player.sendMessage("Invalid selection. Please try again.");
            logger.log(Level.WARNING, "Invalid tier selection by player: {0}", player.name);
        }
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

        int menuId = menuIds.computeIfAbsent("tierUnitsMenu_" + tier, k -> 
            Menus.registerMenu((player1, option) -> {
                if (option >= 0 && option < tierUnits.size()) {
                    Map<String, Object> unitMap = tierUnits.get(option);
                    UnitType unitType = (UnitType) unitMap.get("unit");
                    openUnitMenuGui(unitType, player);
                } else {
                    player.sendMessage("Invalid selection. Please try again.");
                    logger.log(Level.WARNING, "Invalid unit selection by player: {0}", player.name);
                }
            })
        );

        logger.log(Level.INFO, "Opening Tier Units menu GUI for player: {0}, tier: {1}, menuId: {2}", new Object[]{player.name, tier, menuId});
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
                    default -> player.sendMessage("Invalid option. Please try again.");
                }
            })
        );

        logger.log(Level.INFO, "Opening Unit menu GUI for player: {0}, unitType: {1}, menuId: {2}", new Object[]{player.name, unitType, menuId});
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
                        logger.log(Level.WARNING, "Player {0} attempted to set quantity less than 1 for unit: {1}", new Object[]{player.name, unitType});
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
                    logger.log(Level.INFO, "Purchase cancelled by player: {0}", player.name);
                }
            })
        );

        logger.log(Level.INFO, "Opening Quantity Adjustment menu GUI for player: {0}, unitType: {1}, menuId: {2}", new Object[]{player.name, unitType, menuId});
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
                logger.log(Level.INFO, "Player {0} bought unit: {1}", new Object[]{player.name, unitType});
            }
        } else {
            player.sendMessage("Not enough funds.");
            logger.log(Level.WARNING, "Player {0} attempted to buy unit with insufficient funds: {1}", new Object[]{player.name, unitType});
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
            logger.log(Level.INFO, "Player {0} bought multiple units: {1} (quantity: {2})", new Object[]{player.name, unitType, quantity});
        } else {
            player.sendMessage("[red]You don't have enough funds to buy " + quantity + " units.");
            logger.log(Level.WARNING, "Player {0} attempted to buy multiple units with insufficient funds: {1} (quantity: {2})", new Object[]{player.name, unitType, quantity});
        }
    }
}
