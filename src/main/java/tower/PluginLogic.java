package tower;

import static mindustry.Vars.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Strings;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.net.Administration;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.RegenProjector;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.defense.ShockwaveTower;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.RepairTurret;
import tower.Domain.PlayerData;
import tower.Domain.Unitsdrops;
import tower.commands.Units;
import useful.Bundle;

public class PluginLogic {
    public static float multiplier = 1f;
    public static ObjectMap<UnitType, Seq<ItemStack>> drops = new ObjectMap<>();
    public static ObjectMap<Tile, Block> forceProjectorTiles = new ObjectMap<>();
    public static ObjectMap<Tile, Block> repairPointTiles = new ObjectMap<>();
    public static ObjectMap<Tile, Float> repairPointCash = new ObjectMap<>();
    private static Seq<Timer.Task> scheduledTasks = new Seq<>();
    private static final ConcurrentHashMap<Tile, Boolean> pathCache = new ConcurrentHashMap<>();

    public static void init() {
        initializeDrops();
        setupAdminActionFilters();
        scheduleTimers();
        setupEventHandlers();
    }

    private static void initializeDrops() {
        drops.putAll(Unitsdrops.drops);
    }

    private static void setupAdminActionFilters() {
        netServer.admins.addActionFilter(action -> {
            if (action.tile == null)
                return true;
            if (action.type == Administration.ActionType.placeBlock && !canBePlaced(action.tile, action.block) &&
                    !(action.block instanceof ShockMine || action.block instanceof Conduit
                            || action.block instanceof CoreBlock)) {
                Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                return false;
            }
            if (action.type == Administration.ActionType.dropPayload
                    || action.type == Administration.ActionType.pickupBlock) {
                Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                return false;
            }
            return true;
        });
    }

    private static void scheduleTimers() {
        addScheduledTask(PluginLogic::applyForceProjectorEffects, 0f, 2f);
        addScheduledTask(PluginLogic::accumulateRepairPointCash, 0f, 20f);
        addScheduledTask(PluginLogic::distributeRepairPointCash, 0f, 1f);
        addScheduledTask(PluginLogic::resetNegativePlayerCash, 0f, 2f);
        addScheduledTask(PluginLogic::damageNearbyEnemyCores, 0f, 1f);
        addScheduledTask(PluginLogic::showMultiplierPopup, 0f, 1f);
        addScheduledTask(PluginLogic::RemindPeople, 0f, 180f);
    }

    private static void applyForceProjectorEffects() {
        forceProjectorTiles.forEach(entry -> {
            Tile tile = entry.key;
            Groups.player.each(player -> {
                if (player.dst(tile.worldx(), tile.worldy()) <= 100f) {
                    Call.effect(Fx.greenCloud, tile.x, tile.y, 100f, Color.royal);
                    Bundle.label(player, 4f, tile.drawx(), tile.drawy(), "ui.force");
                }
            });
        });
        repairPointTiles.forEach(entry -> {
            Tile tile = entry.key;
            Groups.player.each(player -> {
                if (player.dst(tile.worldx(), tile.worldy()) <= 30f) {
                    Call.effect(Fx.reactorExplosion, tile.x, tile.y, 30f, Color.royal);
                    Bundle.label(player, 4f, tile.drawx(), tile.drawy(), "ui.repair");
                }
            });
        });
    }

    private static void RemindPeople() {
        Groups.player.each(player -> {
            player.sendMessage("[green]Please [yellow]Type [white]/menu to open ingame [cyan] Shop");
        });
    }

    private static void accumulateRepairPointCash() {
        repairPointTiles.forEach(entry -> {
            Tile tile = entry.key;
            AtomicInteger totalCashGenerated = new AtomicInteger();
            Groups.player.each(player -> {
                if (player.dst(tile.worldx(), tile.worldy()) <= 30f) {
                    totalCashGenerated.addAndGet(100);
                }
            });
            repairPointCash.put(tile, totalCashGenerated.get() / 100f);
        });
    }

    private static void distributeRepairPointCash() {
        Groups.player.each(player -> {
            repairPointTiles.forEach(entry -> {
                Tile tile = entry.key;
                if (player.dst(tile.worldx(), tile.worldy()) <= 30f) {
                    float cashToAdd = repairPointCash.get(tile, 0f);
                    if (cashToAdd > 0) {
                        PlayerData playerData = Players.getPlayer(player);
                        playerData.addCash(cashToAdd, player);
                        repairPointCash.put(tile, 0f);
                    }
                }
            });
        });
    }

    private static void resetNegativePlayerCash() {
        Groups.player.each(player -> {
            PlayerData playerData = Players.getPlayer(player);
            if (playerData != null && playerData.getCash() < 0) {
                playerData.setCash(0, player);
            }
        });
    }

    private static void damageNearbyEnemyCores() {
        state.rules.waveTeam.data().units.each(unit -> {
            var core = unit.closestEnemyCore();
            if (core == null || unit.dst(core) > 80f || core.health <= 0)
                return;
            float damage = (unit.health + unit.shield) / Mathf.sqrt(multiplier);
            damage = Math.min(damage, core.health);
            core.damage(Team.crux, damage);
            Call.effect(Fx.healWaveMend, unit.x, unit.y, 40f, Color.crimson);
            core.damage(1, true);
            unit.kill();
            if (core.block.health <= 0) {
                core.block.health = 1;
            }
        });
    }

    private static void showMultiplierPopup() {
        Bundle.popup(1f, 20, 50, 20, 450, 0, "ui.multiplier",
                Color.HSVtoRGB(multiplier * 120f, 100f, 100f),
                Strings.autoFixed(multiplier, 2));
    }

    private static void addScheduledTask(Runnable task, float delay, float interval) {
        Timer.Task timerTask = Timer.schedule(task, delay, interval);
        scheduledTasks.add(timerTask);
    }

    private static void cancelAllTasks() {
        scheduledTasks.each(Timer.Task::cancel);
        scheduledTasks.clear();
    }

    private static void reloadAllTasks() {
        cancelAllTasks();
        scheduleTimers();
    }

    private static void setupEventHandlers() {
        Events.on(EventType.WorldLoadEvent.class, event -> {
            multiplier = 0.5f;
            for (int x = 0; x < Vars.world.width(); x++) {
                for (int y = 0; y < Vars.world.height(); y++) {
                    Tile tile = Vars.world.tile(x, y);
                    if (isPath(tile)) {
                        Block block = tile.block();
                        if (block != null && !(block instanceof ShockMine || block instanceof Conduit
                                || block instanceof CoreBlock)) {
                            tile.setBlock(Blocks.air);
                        }
                    }
                }
            }
        });

        Events.on(EventType.UnitDestroyEvent.class, event -> {
            if (event.unit.team != state.rules.waveTeam)
                return;
            var core = event.unit.closestEnemyCore();
            var drop = drops.get(event.unit.type);
            if (core == null || drop == null)
                return;
            var builder = new StringBuilder();
            drop.each(stack -> {
                int amount = (int) ((stack.amount - stack.amount / 2)
                        + (Math.random() * (stack.amount * 1.4f + stack.amount / 2)));
                builder.append("[accent]+").append(amount).append("[white]").append(stack.item.emoji()).append(" ");
                Call.transferItemTo(event.unit, stack.item, core.acceptStack(stack.item, amount, core), event.unit.x,
                        event.unit.y, core);
            });
            Call.labelReliable(builder.toString(), 1f, event.unit.x + Mathf.range(4f), event.unit.y + Mathf.range(4f));
        });

        Events.on(EventType.WorldLoadEvent.class, event -> {
            for (int x = 0; x < Vars.world.width(); x++) {
                for (int y = 0; y < Vars.world.height(); y++) {
                    Tile tile = Vars.world.tile(x, y);
                    isPath(tile);
                }
            }
        });

        Events.on(EventType.WaveEvent.class, event -> adjustMultiplierByWave());
        Events.on(EventType.GameOverEvent.class, event -> {
            Units.clearMenuIds();
            resetGame();
        });
        Events.on(EventType.TileChangeEvent.class, event -> updateTiles(event.tile));
        Events.on(EventType.WorldLoadEndEvent.class, event -> reloadAllTasks());
    }

    private static void adjustMultiplierByWave() {
        float baseValue = state.wave * state.wave / 3200f + 0.2f;
        if (state.wave <= 10) {
            multiplier = Mathf.clamp(baseValue, multiplier, 1.5f);
        } else if (state.wave <= 30) {
            multiplier = Mathf.clamp(baseValue * 2, multiplier, 3f);
        } else if (state.wave <= 60) {
            multiplier = Mathf.clamp(baseValue * 2, multiplier, 3.5f);
        } else if (state.wave <= 120) {
            multiplier = Mathf.clamp(baseValue * 2, multiplier, 4f);
        } else if (state.wave <= 150) {
            multiplier = Mathf.clamp(baseValue * 2, multiplier, 6f);
        } else if (state.wave <= 200) {
            multiplier = Mathf.clamp(baseValue * 2, multiplier, 12f);
        } else {
            multiplier = Mathf.clamp(baseValue * 2, multiplier, 100f);
        }
    }

    private static void resetGame() {
        multiplier = 1f;
        repairPointCash.clear();
        forceProjectorTiles.clear();
        repairPointTiles.clear();
    }

    private static void updateTiles(Tile tile) {
        Block block = tile.block();
        if (block instanceof RegenProjector || block instanceof ShockwaveTower) {
            forceProjectorTiles.put(tile, block);
        } else if (block instanceof RepairTurret || block instanceof RegenProjector) {
            repairPointTiles.put(tile, block);
        } else {
            forceProjectorTiles.remove(tile);
            repairPointTiles.remove(tile);
            repairPointCash.remove(tile);
        }

        // Check if the tile is in the path cache and remove it if found
        if (pathCache.containsKey(tile)) {
            tile.setAir();
        }
    }
    public static boolean isInPathCache(Tile tile) {
        return pathCache.containsKey(tile);
    }
   

    public static void checkUnitsWithinRadius() {
        forceProjectorTiles.forEach(entry -> {
            Tile tile = entry.key;
            Groups.unit.each(unit -> {
                if (unit.team == state.rules.waveTeam && unit.dst(tile.worldx(), tile.worldy()) <= 105f) {
                    unit.type.speed = 1.2f;
                    unit.healthMultiplier(0.75f);
                    unit.apply(StatusEffects.electrified, 200f);
                    unit.apply(StatusEffects.slow, 200f);
                    unit.apply(StatusEffects.freezing, 200f);
                    unit.apply(StatusEffects.sporeSlowed, 200f);
                }
            });
        });
    }

    public static boolean canBePlaced(Tile tile, Block block) {
        return !pathCache.computeIfAbsent(tile, PluginLogic::isPath);
    }

    public static boolean isPath(Tile tile) {
        return tile.floor() == Vars.world.tile(0, 0).floor()
                || tile.floor() == Vars.world.tile(0, 1).floor()
                || tile.floor() == Vars.world.tile(1, 0).floor()
                || tile.floor() == Vars.world.tile(1, 1).floor()
                || tile.floor() == Vars.world.tile(2, 0).floor()
                || tile.floor() == Vars.world.tile(0, 2).floor()
                || tile.floor() == Vars.world.tile(2, 1).floor()
                || tile.floor() == Vars.world.tile(1, 2).floor()
                || tile.floor() == Vars.world.tile(2, 2).floor();
    }
}
