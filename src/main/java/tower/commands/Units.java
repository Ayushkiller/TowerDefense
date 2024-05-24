package tower.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import mindustry.entities.abilities.Ability;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Menus;
import tower.Bundle;
import tower.Players;
import tower.Domain.Abilities;
import tower.Domain.PlayerData;
import tower.Domain.UnitsTable;

public class Units {
    private static final Map<UnitType, Integer> unitPrices = new HashMap<>();
    private static int currentQuantity = 0;
    private static int currentTotalPrice = 0;
    private static UnitType currentUnitType = null;
    private static Player currentPlayer = null;
    private static boolean isMenuActive = false;

    public static void initUnitsTable() {
        for (Map<String, Object> unitMap : UnitsTable.units) {
            UnitType unitType = (UnitType) unitMap.get("unit");
            int price = (int) unitMap.get("price");
            price = (int) (price * 1.4);
            unitPrices.put(unitType, price);
        }
    }

    private static void buyUnit(UnitType unitType, Player player, boolean shouldControlUnit) {
        PlayerData playerData = Players.getPlayer(player);
        int price = unitPrices.get(unitType);
        if (playerData.getCash() >= price) {
            playerData.subtractCash((float) price);

            Unit spawned = unitType.spawn(player.x, player.y);

            if (spawned != null && !spawned.dead()) {
                spawned.type.autoFindTarget = true;
                spawned.type.alwaysUnlocked = true;
                if (shouldControlUnit) {
                    Call.unitControl(player, spawned);
                }

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
                        case 5:
                            spawned.abilities = Abilities.getAbility5().toArray(new Ability[0]);
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
                        playerData.addCash((float) price);
                        player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
                        player.sendMessage(Bundle.get("unit.died", player.locale));
                    }
                }, 3, TimeUnit.SECONDS);

                player.sendMessage(Bundle.get("unit.brought", player.locale));
            } else {
                playerData.addCash((float) price);
                player.sendMessage(Bundle.get("unit.spawn.failed", player.locale));
            }
        } else {
            player.sendMessage(Bundle.get("menu.units.not-enough", player.locale()));
        }
    }

    private static final int openTierMenuGui = Menus.registerMenu((player, option) -> {
        if (option >= 0 && option < 6) {
            openTierUnitsMenuGui(option, player);
        } else {
            player.sendMessage("Invalid selection. Please try again.");
        }
    });

    private static void openTierMenuGui(Player player) {
        String[][] buttons = new String[6][1];
        for (int i = 0; i < 6; i++) {
            buttons[i][0] = "[cyan]Tier " + i;
        }
        Call.menu(player.con, openTierMenuGui, "Select Tier", "", buttons);
    }

    private static final int openTierMenuUnitsGui = Menus.registerMenu((player1, option) -> {
        List<Map<String, Object>> tierUnits = UnitsTable.units.stream()
                .filter(unit -> (int) unit.get("tier") == option)
                .toList();
        if (option >= 0 && option < tierUnits.size()) {
            Map<String, Object> unitMap = tierUnits.get(option);
            UnitType unitType = (UnitType) unitMap.get("unit");
            openUnitMenuGui(unitType, player1);
        } else {
            player1.sendMessage("Invalid selection. Please try again.");
        }
    });

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
        Call.menu(player.con, openTierMenuUnitsGui, "Select Unit", "", buttons);
    }


    private static void openUnitMenuGui(UnitType unitType, Player player) {
        int price = unitPrices.get(unitType);
        String message = unitType.emoji() + "\n\n" +
                Bundle.get("menu.units.info.health", player.locale) + " " + (int) unitType.health + "\n" +
                Bundle.get("menu.units.info.armor", player.locale) + " " + (int) unitType.armor + "\n" +
                Bundle.get("menu.units.info.price", player.locale) + " " + price;
        Call.menu(player.con, openUnitMenuGui, Bundle.get("menu.units.title"), message, new String[][] {
                { "[lime]Buy" },
                { "[gray]Back" },
                { "[blue]Buy Multiple" } // New option for buying multiple units
        });
    }
    private static final int openUnitMenuGui = Menus.registerMenu(((player1, option) -> {
        switch (option) {
            case 0 -> buyUnit((UnitType) UnitsTable.units.get(option), player1, true); // Pass true to allow unit
                                                                                       // control
            case 1 -> openTierMenuGui(player1); // Go back to the tier selection menu
            case 2 -> openQuantityAdjustmentMenu((UnitType) UnitsTable.units.get(option), player1, 1); // New case for
                                                                                                       // buying
                                                                                                       // multiple units
        }
    }));

    private static final int quantityAdjustmentMenuId = Menus.registerMenu((player, option) -> {
        if (!isMenuActive)
            return; // Ignore inputs if the menu is not active

        switch (option) {
            case 0: // Increase quantity
                currentQuantity++;
                currentTotalPrice += unitPrices.get(currentUnitType);
                break;
            case 1: // Decrease quantity
                if (currentQuantity > 0) {
                    currentQuantity--;
                    currentTotalPrice -= unitPrices.get(currentUnitType);
                }
                break;
            case 2: // Buy
                buyMultipleUnits(currentUnitType, currentPlayer, currentQuantity);
                isMenuActive = false; // Deactivate the menu after purchase
                break;
            case 3: // Close
                isMenuActive = false; // Deactivate the menu without purchasing
                break;
            case 4: // Back
            openUnitMenuGui(currentUnitType, player);
                isMenuActive = false;
                break;
        }

        // Update the menu display
        updateQuantityAdjustmentMenuDisplay(player);
    });

    private static void openQuantityAdjustmentMenu(UnitType unitType, Player player, int initialQuantity) {
        if (isMenuActive)
            return; // Prevent re-opening the menu if it's already active

        currentUnitType = unitType;
        currentPlayer = player;
        currentQuantity = initialQuantity;
        currentTotalPrice = unitPrices.get(unitType) * initialQuantity;
        isMenuActive = true;
        PlayerData playerData = PlayerData.get(player);
        String[][] options = {
                { "+1" },
                { "-1" },
                { "Buy" },
                { "Close" },
                { "Back" }
        };
        String message = "Current Quantity: " + currentQuantity + "\nTotal Price: " + currentTotalPrice
                + "\nYour Cash: " + playerData.getCash();

        Call.menu(quantityAdjustmentMenuId, "Quantity Adjustment Menu", message, options);
    }

    private static void updateQuantityAdjustmentMenuDisplay(Player player) {
        if (!isMenuActive)
            return;

        String[][] options = {
                { "+1" },
                { "-1" },
                { "Buy" },
                { "Close" },
                { "Back" }
        };
        PlayerData playerData = PlayerData.get(player);
        String message = "Current Quantity: " + currentQuantity + "\nTotal Price: " + currentTotalPrice
                + "\nYour Cash: " + playerData.getCash();

        Call.menu(quantityAdjustmentMenuId, "Quantity Adjustment Menu", message, options);
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

    public static void execute(Player player) {
        openTierMenuGui(player);
    }
}