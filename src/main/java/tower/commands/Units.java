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
        Call.menu(player.con,openTierMenuGui, "Select Tier", "", buttons);
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
    private static final int openUnitMenuGui =  Menus.registerMenu(((player1, option) -> {
        switch (option) {
            case 0 -> buyUnit((UnitType) UnitsTable.units.get(option), player1, true); // Pass true to allow unit control
            case 1 -> openTierMenuGui(player1); // Go back to the tier selection menu
            case 2 -> openQuantityAdjustmentMenu((UnitType) UnitsTable.units.get(option), player1, 1); // New case for buying multiple units
        }
    }));
    private static void openUnitMenuGui(UnitType unitType, Player player) {
        int price = unitPrices.get(unitType);
        String message = unitType.emoji() + "\n\n" +
                Bundle.get("menu.units.info.health", player.locale) + " " + (int) unitType.health + "\n" +
                Bundle.get("menu.units.info.armor", player.locale) + " " + (int) unitType.armor + "\n" +
                Bundle.get("menu.units.info.price", player.locale) + " " + price;
        Call.menu(player.con,openUnitMenuGui, Bundle.get("menu.units.title"), message, new String[][] {
                { "[lime]Buy" },
                { "[gray]Back" },
                { "[blue]Buy Multiple" } // New option for buying multiple units
        });
    }
//TODO: Make Menus.register into seprate like other examples above and call from there

    private static void openQuantityAdjustmentMenu(UnitType unitType, Player player, int defaultQuantity) {
        String title = "[red]Adjust Quantity";
        String[][] buttons = { { "-1", "0", "+1" }, { "[green]Buy", "[grey]Back", "[red]Close" } };
        int price = unitPrices.get(unitType);
        StringBuilder message = new StringBuilder("[green]Current Quantity: ").append(defaultQuantity)
                .append("\n[red]Total Price: ").append(defaultQuantity * price);

        Call.menu(player.con, Menus.registerMenu((p, opt) -> {
            if (opt < 3) { // Adjustment buttons
                int adjustment = Integer.parseInt(buttons[0][opt]);
                int currentQuantity = Math.max(1, defaultQuantity + adjustment);
                openQuantityAdjustmentMenu(unitType, player, currentQuantity);
            } else if (opt == 3) { // Buy button
                buyMultipleUnits(unitType, player, defaultQuantity);
            } else if (opt == 4) { // Back button
                openUnitMenuGui(unitType, player);
            } else if (opt == 5) { // Close button
                player.sendMessage("Purchase cancelled.");
            }
        }), title, message.toString(), buttons);
    }

    private static void buyMultipleUnits(UnitType unitType, Player player, int quantity) {
        int totalCost = unitPrices.get(unitType) * quantity;
        PlayerData playerData = Players.getPlayer(player);

        if (playerData.getCash() >= totalCost) {
            for (int i = 0; i < quantity; i++) {
                buyUnit(unitType, player, false); // Pass false to prevent unit control
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