package tower;

import static mindustry.Vars.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Strings;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.ai.types.GroundAI;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.net.Administration;
import mindustry.type.ItemStack;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.defense.RegenProjector;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.defense.ShockwaveTower;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.RepairTurret;
import mindustry.world.meta.BlockFlag;
import tower.Domain.PlayerData;
import tower.Domain.Unitsdrops;
import tower.game.Scenarios;
import tower.pathing.FlyingAIForAss;
import useful.Bundle;

public class PluginLogic {
    public static float multiplier = 1f;
    public static boolean multiplierAdjusted = false;
    public static ObjectMap<UnitType, Seq<ItemStack>> drops;
    public static ObjectMap<Tile, ForceProjector> forceProjectorTiles = new ObjectMap<>();
    public static ObjectMap<Tile, RepairTurret> repairPointTiles = new ObjectMap<>();
    public static ObjectMap<Tile, Float> repairPointCash = new ObjectMap<>();
    public static ObjectMap<Tile, RegenProjector> regenProjectorTiles = new ObjectMap<>();
    public static ObjectMap<Tile, ShockwaveTower> ShockerTiles = new ObjectMap<>();
    public static void init() {
        initializeDrops();
        setupAdminActionFilters();
        scheduleTimers();
        setupEventHandlers();
    }

    private static void initializeDrops() {
        drops = new ObjectMap<>();
        for (Map<String, Object> dropEntry : Unitsdrops.drops) {
            UnitType unit = (UnitType) dropEntry.get("unit");
            Object dropsObject = dropEntry.get("drops");
            if (dropsObject instanceof Seq<?>) {
                @SuppressWarnings("unchecked")
                Seq<ItemStack> itemStacks = (Seq<ItemStack>) dropsObject;
                drops.put(unit, itemStacks);
            }
        }
    }

    private static void setupAdminActionFilters() {
        netServer.admins.addActionFilter(action -> {
            if (action.tile == null)
                return true;
            if (action.type == Administration.ActionType.placeBlock) {
                if (!canBePlaced(action.tile, action.block) && !(action.block instanceof ShockMine
                        || action.block instanceof Conduit || action.block instanceof CoreBlock)) {
                    Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                    return false;
                }
            }
            if ((action.type == Administration.ActionType.dropPayload
                    || action.type == Administration.ActionType.pickupBlock)) {
                Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                return false;
            }
            return true;
        });
    }

    private static void scheduleTimers() {
        Timer.schedule(() -> {
            forceProjectorTiles.each((tile, forceProjectorTiles) -> {
                Groups.player.each(player -> {
                    if (player.dst(tile.worldx(), tile.worldy()) <= 100f) {
                        Call.effect(Fx.greenCloud, tile.x, tile.y, 100f, Color.royal);
                    }
                });
            });
            repairPointTiles.each((tile, repairPointTiles) -> {
                Groups.player.each(player -> {
                    if (player.dst(tile.worldx(), tile.worldy()) <= 30f) {
                        Call.effect(Fx.reactorExplosion, tile.x, tile.y, 30f, Color.royal);
                    }
                });
            });
        }, 0f, 2f);

        Timer.schedule(() -> {
            repairPointTiles.each((tile, repairPointTiles) -> {
                AtomicInteger totalCashGenerated = new AtomicInteger(0);
                Groups.player.each(player -> {
                    if (player.dst(tile.worldx(), tile.worldy()) <= 30f) {
                        totalCashGenerated.addAndGet(100); // Accumulate cash for all players within the distance,
                                                           // multiplied by 100 to keep two decimal places
                    }
                });
                float cashToStore = totalCashGenerated.get() / 100f; // Convert back to float
                repairPointCash.put(tile, cashToStore); // Store the total cash generated for this tile
            });
        }, 0f, 20f);

        Timer.schedule(() -> {
            Groups.player.each(player -> {
                repairPointTiles.each((tile, repairPointTiles) -> {
                    if (player.dst(tile.worldx(), tile.worldy()) <= 30f) {
                        float cashToAdd = repairPointCash.get(tile, 0f);
                        if (cashToAdd > 0) {
                            PlayerData playerData = Players.getPlayer(player);
                            playerData.addCash(cashToAdd, player);
                            // Reset the cash for this tile after distribution
                            repairPointCash.put(tile, 0f);
                        }
                    }
                });
            });
        }, 0f, 1f);
        Timer.schedule(() -> {
            for (Player player : Groups.player) {
                PlayerData playerData = Players.getPlayer(player);
                if (playerData != null && playerData.getCash() < 0) {
                    playerData.setCash(0, player);
                }
            }
        }, 0f, 2f); // Check every second
        Timer.schedule(() -> state.rules.waveTeam.data().units.each(unit -> {
            var core = unit.closestEnemyCore();
            if (core == null || unit.dst(core) > 80f || core.health <= 0)
                return; // Check if core is null, out of range, or already dead
            float damage = (unit.health + unit.shield) / Mathf.sqrt(multiplier);
            // Ensure damage does not exceed the core's health
            damage = Math.min(damage, core.health);
            core.damage(Team.crux, damage);
            Call.effect(Fx.healWaveMend, unit.x, unit.y, 40f, Color.crimson);
            core.damage(1, true);
            unit.kill();
            if (core.block.health <= 0) {
                core.block.health = 1;
            }
        }), 0f, 1f);
        Timer.schedule(() -> Bundle.popup(1f, 20, 50, 20, 450, 0, "ui.multiplier",
                Color.HSVtoRGB(multiplier * 120f, 100f, 100f), Strings.autoFixed(multiplier, 2)), 0f, 1f);
    }

    private static void setupEventHandlers() {
        Events.on(EventType.WorldLoadEvent.class, event -> {
            // Set the multiplier to 0.5f
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
        Events.on(EventType.WaveEvent.class, event -> {
            if (state.wave % 50 == 0) {
                Scenarios.requestDeploymentForAllPlayers();
            }
            if (state.wave <= 10) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f), multiplier, 1.5f);
            } else if (state.wave > 10 && state.wave <= 30) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 3f);
            } else if (state.wave > 30 && state.wave <= 60) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 3.5f);
            } else if (state.wave > 60 && state.wave <= 120) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 4f);
            } else if (state.wave > 120 && state.wave <= 150) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 6f);
            } else if (state.wave > 150 && state.wave <= 200) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 12f);
            } else if (state.wave > 200 && state.wave <= 250) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 18f);
            } else if (state.wave > 250 && state.wave <= 350) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 48f);
            } else {
                // After wave 350, increase the multiplier rapidly
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 2f) * 2, multiplier, 100f);
            }
        });
        Events.on(EventType.GameOverEvent.class, event -> {
            Players.clearMap();
            forceProjectorTiles.clear();
            repairPointTiles.clear();
            repairPointCash.clear();
            ShockerTiles.clear();
            repairPointTiles.clear();
        });
        Events.on(EventType.TileChangeEvent.class, event -> {
            Tile changedTile = event.tile;
            Block block = changedTile.block();
        
            if (block instanceof ForceProjector) {
                // Handle ForceProjector placement
                if (!forceProjectorTiles.containsKey(changedTile)) {
                    forceProjectorTiles.put(changedTile, (ForceProjector) block);
                }
            } else if (block instanceof RegenProjector) {
                // Handle RegenProjector placement
                if (!regenProjectorTiles.containsKey(changedTile)) {
                    regenProjectorTiles.put(changedTile, (RegenProjector) block);
                }
            } else if (block instanceof RepairTurret) {
                // Handle RepairTurret placement
                if (!repairPointTiles.containsKey(changedTile)) {
                    repairPointTiles.put(changedTile, (RepairTurret) block);
                }
            } else if (block instanceof ShockwaveTower) {
                // Handle ShockwaveTower placement
                if (!ShockerTiles.containsKey(changedTile)) {
                    ShockerTiles.put(changedTile, (ShockwaveTower) block);
                }
            } else {
                if (forceProjectorTiles.containsKey(changedTile)) {
                    forceProjectorTiles.remove(changedTile);
                }
                if (repairPointTiles.containsKey(changedTile)) {
                    repairPointTiles.remove(changedTile);
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
                // Adjust the amount based on the multiplierAdjusted flag
                int amount = multiplierAdjusted ? (int) (stack.amount * 0.75f)
                        : Mathf.random(stack.amount - stack.amount / 2, stack.amount + stack.amount / 2);

                builder.append("[accent]+").append(amount).append(stack.item.emoji()).append(" ");
                Call.transferItemTo(event.unit, stack.item, core.acceptStack(stack.item, amount, core), event.unit.x,
                        event.unit.y, core);
            });

            Call.labelReliable(builder.toString(), 1f, event.unit.x + Mathf.range(4f), event.unit.y + Mathf.range(4f));

            Timer.schedule(() -> multiplierAdjusted = false, 180f);
        });
        Events.on(EventType.UnitSpawnEvent.class, event -> {
            if (event.unit.team == state.rules.waveTeam) {
                if (event.unit != null) {
                    event.unit.health(event.unit.health * multiplier);
                    event.unit.maxHealth(event.unit.maxHealth * multiplier);
                    event.unit.damageMultiplier(0f);
                    if (state.wave <= 200f) {
                        applyStatusEffect(event.unit, StatusEffects.overdrive, Float.POSITIVE_INFINITY);
                        applyStatusEffect(event.unit, StatusEffects.overclock, Float.POSITIVE_INFINITY);
                        applyStatusEffect(event.unit, StatusEffects.shielded, Float.POSITIVE_INFINITY);
                        applyStatusEffect(event.unit, StatusEffects.boss, Float.POSITIVE_INFINITY);
                        applyStatusEffect(event.unit, StatusEffects.sporeSlowed, Float.POSITIVE_INFINITY);
                        applyStatusEffect(event.unit, StatusEffects.muddy, Float.POSITIVE_INFINITY);
                    }
                    if (state.wave >= 250) {
                        applyStatusEffect(event.unit, StatusEffects.fast, Float.POSITIVE_INFINITY);
                    }
                    applyStatusEffect(event.unit, StatusEffects.disarmed, Float.POSITIVE_INFINITY);
                    if (event.unit.type != null) {
                        event.unit.type.speed = 0.8f;
                        event.unit.type.range = -1f;
                        event.unit.type.hovering = true;
                        event.unit.disarmed = true;
                        if (event.unit.type == UnitTypes.omura || event.unit.type == UnitTypes.aegires||event.unit.type == UnitTypes.oct) {
                            event.unit.kill();
                        }
                        event.unit.type.physics = false;
                        event.unit.type.crashDamageMultiplier = 0f;
                        event.unit.type.crushDamage = 0f;
                        event.unit.type.deathExplosionEffect = Fx.shockwave;
                        event.unit.shield(event.unit.shield * multiplier);
                        event.unit.speedMultiplier(event.unit.speedMultiplier * multiplier);
                        event.unit.type.mineWalls = event.unit.type.mineFloor = event.unit.type.targetAir = event.unit.type.targetGround = false;
                        event.unit.type.payloadCapacity = event.unit.type.legSplashDamage = event.unit.type.range = event.unit.type.maxRange = event.unit.type.mineRange = 0f;
                        event.unit.type.aiController = (event.unit.type.flying)? FlyingAIForAss::new
        : GroundAI::new;
                        event.unit.type.targetFlags = new BlockFlag[] { BlockFlag.core };
                    }
                }
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            checkUnitsWithinRadius();
        });
    }
        
        // Helper method to apply status effects with null checks
        private static void applyStatusEffect(Unit unit, StatusEffect effect, float duration) {
            if (unit != null && effect != null) {
                unit.apply(effect, duration);
            }
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

    public static boolean canBePlaced(Tile tile, Block block) {
        return !tile.getLinkedTilesAs(block, new Seq<>()).contains(PluginLogic::isPath);
    }

    public static void adjustMultiplierBasedOnNoVotes(int globalNoVotes) {
        if (globalNoVotes > 0) {
            float increaseAmount = Mathf.random(0.0f, state.wave * 0.02f);
            multiplier += increaseAmount;
            Call.sendMessage(tower.Bundle.get("enemyBuffMessage"));
            multiplierAdjusted = true;
        }
    }

    public static void Help(int globalYesVotes) {
        if (globalYesVotes > 0) {
            Timer.schedule(() -> {
                Groups.player.each(p -> {
                    PlayerData playerData = Players.getPlayer(p);
                    if (playerData != null) {
                        playerData.addCash(300, p);
                        Team team = Team.sharded;
                        Rules.TeamRule teamRule = Vars.state.rules.teams.get(team);
                        teamRule.blockDamageMultiplier = 1.2f;
                    }
                });
            }, 120f);
            Team team = Team.sharded;
            Rules.TeamRule teamRule = Vars.state.rules.teams.get(team);
            teamRule.blockDamageMultiplier = 0.5f;
            Call.sendMessage(tower.Bundle.get("turretsDamageReducedMessage"));
            Call.sendMessage(tower.Bundle.get("newEngineersArrivalMessage"));
        }
    }

    public static void checkUnitsWithinRadius() {
        forceProjectorTiles.each((tile, forceProjector) -> {
            Groups.unit.each(unit -> {
                float distance = unit.dst(tile.worldx(), tile.worldy());
                if (distance <= 105f && unit.team == state.rules.waveTeam) {
                    unit.type.speed = 1.2f;
                    unit.healthMultiplier(0.75f);
                    unit.apply(StatusEffects.electrified, 200f);
                    unit.apply(StatusEffects.slow, 200f);
                }
            });
        });
    }
}
