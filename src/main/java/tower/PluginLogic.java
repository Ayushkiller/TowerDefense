package tower;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.*;
import arc.util.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.net.Administration;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockFlag;

import tower.Domain.Unitsdrops;
import useful.Bundle;

import static mindustry.Vars.*;

import java.util.Map;

public class PluginLogic {

    public static float multiplier = 1f;
    public static ObjectMap<UnitType, Seq<ItemStack>> drops;
    
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
            return true; 
        });

        Timer.schedule(()->state.rules.waveTeam.data().units.each(unit->{
            var core = unit.closestEnemyCore();
            if(core == null || unit.dst(core) > 60f) return;
            float damage = unit.health + unit.shield / Mathf.sqrt(multiplier);
            core.damage(damage, true);
            unit.kill();
            
            // Check if the core's health is 0 or less and set to 1
        
            if(core.health <= 0) {
                core.health = 1;
            }
        }), 0f, 1f);
        Timer.schedule(()->Bundle.popup(1f, 20, 50, 20, 450, 0, "ui.multiplier", Color.HSVtoRGB(multiplier * 120f, 100f, 100f), Strings.autoFixed(multiplier, 2)), 0f, 1f);

        Events.on(EventType.WorldLoadEvent.class, event->multiplier = 0.5f);
        Events.on(EventType.WaveEvent.class, event -> {
            // Calculate the multiplier based on the wave number
            // For waves 1 to 8, increase the multiplier slowly
            // After wave 8, increase the multiplier rapidly
            if (state.wave <= 10) {
                multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.2f), multiplier, 1.5f);
            } else if (state.wave > 10 && state.wave <= 30) {
                // After wave 8 and before wave 30, increase the multiplier rapidly
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
        Events.on(EventType.UnitDestroyEvent.class, event -> {
        if (event.unit.team != state.rules.waveTeam) return;

        var core = event.unit.closestEnemyCore();
        var drop = drops.get(event.unit.type);

        if (core == null || drop == null) return;

        var builder = new StringBuilder();

        drop.each(stack -> {
        int amount = Mathf.random(stack.amount - stack.amount /   2, stack.amount + stack.amount /   2);

        builder.append("[accent]+").append(amount).append(stack.item.emoji()).append("  ");
        Call.transferItemTo(event.unit, stack.item, core.acceptStack(stack.item, amount, core), event.unit.x, event.unit.y, core);
        });

        Call.label(builder.toString(),   1f, event.unit.x + Mathf.range(4f), event.unit.y + Mathf.range(4f));

        // Distribute points to all players
        Players.forEach(playerData -> {
         if (playerData != null) {
            float reductionPercentage = playerData.calculateReductionPercentage(playerData.getPoints());
            playerData.addPointsWithReduction(reductionPercentage);
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
            event.unit.type.abilities.clear();
            event.unit.type.crashDamageMultiplier = 0f;
            event.unit.type.crushDamage = 0f;
            event.unit.type.deathExplosionEffect= Fx.healWave;
            event.unit.shield(event.unit.shield * multiplier);
            event.unit.speedMultiplier(event.unit.speedMultiplier * multiplier);

             // Apply AI settings after the unit has spawned
             event.unit.type.mineWalls = event.unit.type.mineFloor = event.unit.type.targetAir = event.unit.type.targetGround = false;
             event.unit.type.payloadCapacity = event.unit.type.legSplashDamage = event.unit.type.range = event.unit.type.maxRange = event.unit.type.mineRange =  0f;
             event.unit.type.aiController = event.unit.type.flying ? FlyingAI::new : GroundAI::new;
             event.unit.type.targetFlags = new BlockFlag[]{BlockFlag.core};
            return;
        });
    }
    public static boolean isPath(Tile tile) {
        return tile.floor() == Blocks.darkPanel5 || tile.floor() == Blocks.sandWater;
    }

    public static boolean canBePlaced(Tile tile, Block block) {
        return !tile.getLinkedTilesAs(block, new Seq<>()).contains(PluginLogic::isPath);
    }
}
