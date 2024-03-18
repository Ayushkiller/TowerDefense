package tower;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.ai.types.GroundAI;
import mindustry.content.*;
import mindustry.game.EventType;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.net.Administration;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockFlag;
import tower.Domain.PlayerData;
import tower.Domain.Unitsdrops;
import tower.game.Scenarios;
import tower.pathing.FlyingAIForAss;
import useful.Bundle;

import static mindustry.Vars.*;

import java.util.Map;

public class PluginLogic {
    public static float multiplier = 1f;
    public static boolean multiplierAdjusted = false;
    public static ObjectMap<UnitType, Seq<ItemStack>> drops;
    public static ObjectMap<Tile, ForceProjector> forceProjectorTiles = new ObjectMap<>();
    public static void init() {
        drops = new ObjectMap<>();
        for (Map<String, Object> dropEntry : Unitsdrops.drops) {
            UnitType unit = (UnitType) dropEntry.get("unit");
            Object dropsObject = dropEntry.get("drops");
            if (dropsObject instanceof Seq<?>) {
                @SuppressWarnings("unchecked")
                Seq<ItemStack> itemStacks = (Seq<ItemStack>) dropsObject;
                drops.put(unit, itemStacks);
            } else {
                System.err.println("Unexpected type for drops: " + dropsObject.getClass().getName());
            }
        }
        netServer.admins.addActionFilter(action->{
            if(action.tile == null) return true;
            if(action.type == Administration.ActionType.placeBlock || action.type == Administration.ActionType.breakBlock){
                if(!(canBePlaced(action.tile, action.block)|| action.block instanceof ShockMine|| action.block instanceof Conduit || action.block instanceof CoreBlock)){
                    Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                    return false; // Explicitly return false here
                }
            }
            if((action.type == Administration.ActionType.dropPayload || action.type == Administration.ActionType.pickupBlock)){
                Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                return false;
            }
            return true; 

        });
        

        Timer.schedule(()->state.rules.waveTeam.data().units.each(unit->{
            var core = unit.closestEnemyCore();
            if(core == null || unit.dst(core) > 80f || core.health <= 0) return; // Check if core is null, out of range, or already dead
            float damage = (unit.health + unit.shield)/ Mathf.sqrt(multiplier);
            // Ensure damage does not exceed the core's health
            damage = Math.min(damage, core.health);
            core.damage(Team.crux,damage);
            core.damage(1,true);
            unit.kill();
            
            // Check if the core's health is 0 or less and set to 1
            if(core.block.health <= 0) {
                core.block.health = 1;
            }
        }), 0f, 1f);
        Timer.schedule(()->Bundle.popup(1f, 20, 50, 20, 450, 0, "ui.multiplier", Color.HSVtoRGB(multiplier * 120f, 100f, 100f), Strings.autoFixed(multiplier, 2)), 0f, 1f);

        Events.on(EventType.WorldLoadEvent.class, event->multiplier = 0.5f);
        Events.on(EventType.WaveEvent.class, event -> {
            if (state.wave == 50) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 100) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 150) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 200) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 250) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 300) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 350) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 400) {
                Scenarios.requestDeploymentForAllPlayers(); 
            }
            if (state.wave == 450) {
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
            } else if (state.wave > 150 && state.wave <= 250) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 12f);
            } else if (state.wave > 250 && state.wave <= 350) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f) * 2, multiplier, 18f);
            } else {
                // After wave 350, increase the multiplier rapidly
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 2f) * 2, multiplier, 100f);
            }
        });
        
        Events.on(EventType.GameOverEvent.class, event -> Players.clearMap());
        Events.on(EventType.TileChangeEvent.class, event -> {
            Tile changedTile = event.tile;
            Block block = changedTile.block();

            if (block instanceof ForceProjector) {
                // If the tile is not already in the map, add it
                if (!forceProjectorTiles.containsKey(changedTile)) {
                    forceProjectorTiles.put(changedTile, (ForceProjector) block);
                   
                }
            } else {
                // If the tile is in the map but no longer contains a ForceProjector, remove it
                if (forceProjectorTiles.containsKey(changedTile)) {
                    forceProjectorTiles.remove(changedTile);

                }
            }
        });
        Events.on(EventType.UnitDestroyEvent.class, event -> {
            if (event.unit.team != state.rules.waveTeam) return;
        
            var core = event.unit.closestEnemyCore();
            var drop = drops.get(event.unit.type);
        
            if (core == null || drop == null) return;
        
            var builder = new StringBuilder();
        
            drop.each(stack -> {
                // Adjust the amount based on the multiplierAdjusted flag
                int amount = multiplierAdjusted ? (int) (stack.amount * 0.75f) : Mathf.random(stack.amount - stack.amount / 2, stack.amount + stack.amount / 2);
        
                builder.append("[accent]+").append(amount).append(stack.item.emoji()).append(" ");
                Call.transferItemTo(event.unit, stack.item, core.acceptStack(stack.item, amount, core), event.unit.x, event.unit.y, core);
            });
        
            Call.label(builder.toString(), 1f, event.unit.x + Mathf.range(4f), event.unit.y + Mathf.range(4f));
        
            Timer.schedule(() -> multiplierAdjusted = false, 180f);
        
            // Distribute Cash to all players
            Players.forEach(playerData -> {
            if (playerData != null) {
            float reductionPercentage = playerData.calculateReductionPercentage(playerData.getCash());
              playerData.addCashWithReduction(reductionPercentage);
             }
             });
           });
        Events.on(EventType.UnitSpawnEvent.class, event->{

            if(event.unit.team != state.rules.waveTeam) 

            event.unit.health(event.unit.health * multiplier);
            event.unit.maxHealth(event.unit.maxHealth * multiplier);
            event.unit.damageMultiplier(0f);
            event.unit.apply(StatusEffects.overdrive, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.overclock, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.shielded, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.boss, Float.POSITIVE_INFINITY);
            if (state.wave <= 250) {
                event.unit.apply(StatusEffects.slow, Float.POSITIVE_INFINITY);
            }
            if (state.wave >= 350) {
                event.unit.apply(StatusEffects.fast, Float.POSITIVE_INFINITY);
            }
            event.unit.apply(StatusEffects.disarmed, Float.POSITIVE_INFINITY);
            event.unit.type.speed = 1.5f;
            event.unit.type.range = -1f;
            event.unit.type.hovering = true;
            event.unit.disarmed = true;
            event.unit.type.abilities.clear();
            event.unit.type.abilities.clear();
            event.unit.type.abilities.clear();
            event.unit.type.abilities.clear();
            event.unit.type.abilities.clear();
            event.unit.type.crashDamageMultiplier = 0f;
            event.unit.type.crushDamage = 0f;
            event.unit.type.deathExplosionEffect= Fx.shockwave;
            event.unit.shield(event.unit.shield * multiplier);
            event.unit.speedMultiplier(event.unit.speedMultiplier * multiplier);

             // Apply AI settings after the unit has spawned
             event.unit.type.mineWalls = event.unit.type.mineFloor = event.unit.type.targetAir = event.unit.type.targetGround = false;
             event.unit.type.payloadCapacity = event.unit.type.legSplashDamage = event.unit.type.range = event.unit.type.maxRange = event.unit.type.mineRange =  0f;
            event.unit.type.aiController = event.unit.type.flying ? FlyingAIForAss::new : GroundAI::new;
             event.unit.type.targetFlags = new BlockFlag[]{BlockFlag.core};
            return;
        });
        Events.run(EventType.Trigger.update, () -> {
            checkUnitsWithinRadius();
        });
    }
    public static boolean isPath(Tile tile) {
        return 
           tile.floor() == Vars.world.tile(0, 0).floor() 
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
            Call.sendMessage("[red]Enemy got a buff!After capturing The sector. They are now stronger.");
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
            Call.sendMessage("[red]Due to deployment of team of specialised engineers.Our current Turrets damage is reduced to 50%");
            Call.sendMessage("[lime]New Team of engineers will arrive soon.Due to more enginners after their arrival Turrets damage will be esclated to 120%");
        }
    }
    public static void checkUnitsWithinRadius() {
        forceProjectorTiles.each((tile, forceProjector) -> {
            Groups.unit.each(unit -> {
                float distance = unit.dst(tile.worldx(), tile.worldy());
                if (distance <= 100f && unit.team == state.rules.waveTeam ) {
                    unit.type.speed = 0.9f;
                    unit.healthMultiplier(0.75f);
                    Bundle.label(player,100f, tile.drawx(), tile.drawy(), "shieldProjector.label");
                }
            });
        });
    }
}
