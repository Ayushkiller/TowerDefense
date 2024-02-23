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
import tower.Domain.PlayerData;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockFlag;

import useful.Bundle;

import static mindustry.Vars.*;
import static mindustry.content.Items.*;
import static mindustry.content.UnitTypes.*;

public class PluginLogic {
    public static float multiplier = 1f;
    public static ObjectMap<UnitType, Seq<ItemStack>> drops;

    public static void init() {

        drops = ObjectMap.of(
            crawler, ItemStack.list(copper, 20, lead, 10, silicon, 3),
            atrax, ItemStack.list(copper, 30, lead, 40, graphite, 10, titanium, 5),
            spiroct, ItemStack.list(lead, 100, graphite, 40, silicon, 40, thorium, 10),
            arkyid, ItemStack.list(copper, 300, graphite, 80, metaglass, 80, titanium, 80, thorium, 20, phaseFabric, 10),
            toxopid, ItemStack.list(copper, 400, lead, 400, graphite, 120, silicon, 120, thorium, 40, plastanium, 40, surgeAlloy, 15, phaseFabric, 5),

            dagger, ItemStack.list(copper, 20, lead, 10, silicon, 3),
            mace, ItemStack.list(copper, 30, lead, 40, graphite, 10, titanium, 5),
            fortress, ItemStack.list(lead, 100, graphite, 40, silicon, 40, thorium, 10),
            scepter, ItemStack.list(copper, 300, silicon, 80, metaglass, 80, titanium, 80, thorium, 20, phaseFabric, 10),
            reign, ItemStack.list(copper, 400, lead, 400, graphite, 120, silicon, 120, thorium, 40, plastanium, 40, surgeAlloy, 15, phaseFabric, 5),

            nova, ItemStack.list(copper, 20, lead, 10, metaglass, 3),
            pulsar, ItemStack.list(copper, 30, lead, 40, metaglass, 10),
            quasar, ItemStack.list(lead, 100, metaglass, 40, silicon, 40, titanium, 80, thorium, 10),
            vela, ItemStack.list(copper, 300, metaglass, 80, graphite, 80, titanium, 60, plastanium, 20, surgeAlloy, 5),
            corvus, ItemStack.list(copper, 400, lead, 400, graphite, 100, silicon, 100, metaglass, 120, titanium, 120, thorium, 60, surgeAlloy, 10, phaseFabric, 10),

            flare, ItemStack.list(copper, 20, lead, 10, graphite, 3),
            horizon, ItemStack.list(copper, 30, lead, 40, graphite, 10),
            zenith, ItemStack.list(lead, 100, graphite, 40, silicon, 40, titanium, 30, plastanium, 10),
            antumbra, ItemStack.list(copper, 300, graphite, 80, metaglass, 80, titanium, 60, surgeAlloy, 15),
            eclipse, ItemStack.list(copper, 400, lead, 400, graphite, 120, silicon, 120, titanium, 120, thorium, 40, plastanium, 40, surgeAlloy, 5, phaseFabric, 10),

            mono, ItemStack.list(copper, 20, lead, 10, silicon, 3),
            poly, ItemStack.list(copper, 30, lead, 40, silicon, 10, titanium, 5),
            mega, ItemStack.list(lead, 100, silicon, 40, graphite, 40, thorium, 10),
            quad, ItemStack.list(copper, 300, silicon, 80, metaglass, 80, titanium, 80, thorium, 20, phaseFabric, 10),
            oct, ItemStack.list(copper, 400, lead, 400, graphite, 120, silicon, 120, thorium, 40, plastanium, 40, surgeAlloy, 15, phaseFabric, 5),

            // These units are ships, and need water to be placed and work
            risso, ItemStack.list(copper, 20, lead, 10, metaglass, 3),
            minke, ItemStack.list(copper, 30, lead, 40, metaglass, 10),
            bryde, ItemStack.list(lead, 100, metaglass, 40, silicon, 40, titanium, 80, thorium, 10),
            sei, ItemStack.list(copper, 300, metaglass, 80, graphite, 80, titanium, 60, plastanium, 20, surgeAlloy, 5),
            omura, ItemStack.list(copper, 400, lead, 400, graphite, 100, silicon, 100, metaglass, 120, titanium, 120, thorium, 60, surgeAlloy, 10, phaseFabric, 10),

            retusa, ItemStack.list(copper, 20, lead, 10, metaglass, 3),
            oxynoe, ItemStack.list(copper, 30, lead, 40, metaglass, 10),
            cyerce, ItemStack.list(lead, 100, metaglass, 40, silicon, 40, titanium, 80, thorium, 10),
            aegires, ItemStack.list(copper, 300, metaglass, 80, graphite, 80, titanium, 60, plastanium, 20, surgeAlloy, 5),
            navanax, ItemStack.list(copper, 400, lead, 400, graphite, 100, silicon, 100, metaglass, 120, titanium, 120, thorium, 60, surgeAlloy, 10, phaseFabric, 10),

            // Player Units
            alpha, ItemStack.list(copper, 30, lead, 30, graphite, 20, silicon, 20, metaglass, 20),
            beta, ItemStack.list(titanium, 40, thorium, 20),
            gamma, ItemStack.list(plastanium, 20, surgeAlloy, 10, phaseFabric, 10),

            stell, ItemStack.list(beryllium, 20, silicon, 25),
            locus, ItemStack.list(beryllium, 20, graphite, 20, silicon, 20, tungsten, 15),
            precept, ItemStack.list(beryllium, 45, graphite, 25, silicon, 50, tungsten, 50, surgeAlloy, 75, thorium, 40),
            vanquish, ItemStack.list(beryllium, 80, graphite, 50, silicon, 100, tungsten, 120, oxide, 60, surgeAlloy, 125, thorium, 100, phaseFabric, 60),
            conquer, ItemStack.list(beryllium, 250, graphite, 225, silicon, 125, tungsten, 140, oxide, 120, carbide, 240, surgeAlloy, 250, thorium, 240, phaseFabric, 120),

            elude, ItemStack.list(beryllium, 6, graphite, 25, silicon, 35),
            avert, ItemStack.list(beryllium, 24, graphite, 50, silicon, 30, tungsten, 20, oxide, 20),
            obviate, ItemStack.list(beryllium, 48, graphite, 75, silicon, 50, tungsten, 45, carbide, 50, thorium, 40, phaseFabric, 75),
            quell, ItemStack.list(beryllium, 96, graphite, 100, silicon, 140, tungsten, 70, oxide, 60, carbide, 75, surgeAlloy, 60, thorium, 100, phaseFabric, 125),
            disrupt, ItemStack.list(beryllium, 122, graphite, 125, silicon, 155, tungsten, 100, oxide, 120, carbide, 240, surgeAlloy, 120, thorium, 240, phaseFabric, 250),

            merui, ItemStack.list(beryllium, 25, silicon, 35, tungsten, 10),
            cleroi, ItemStack.list(beryllium, 35, graphite, 20, silicon, 25, tungsten, 20, oxide, 20),
            anthicus, ItemStack.list(beryllium, 50, graphite, 25, silicon, 50, tungsten, 65, oxide, 75, thorium, 40),
            tecta, ItemStack.list(beryllium, 100, graphite, 50, silicon, 140, tungsten, 120, oxide, 125, surgeAlloy, 60, thorium, 100, phaseFabric, 125),
            collaris, ItemStack.list(beryllium, 135, graphite, 90, silicon, 175, tungsten, 155, oxide, 250, carbide, 240, surgeAlloy, 120, thorium, 240, phaseFabric, 120),
            evoke, ItemStack.list(beryllium, 50, graphite, 50, silicon, 50),
            incite, ItemStack.list(tungsten, 25, oxide, 25, carbide, 50),
            emanate, ItemStack.list(surgeAlloy, 25, thorium, 25, phaseFabric, 50)
        );


        netServer.admins.addActionFilter(action->{
            if(action.tile == null) return true;

            if(action.type == Administration.ActionType.placeBlock || action.type == Administration.ActionType.breakBlock){
                if(!(canBePlaced(action.tile, action.block)|| action.block instanceof ShockMine || action.block instanceof CoreBlock)){
                    Bundle.label(action.player, 4f, action.tile.drawx(), action.tile.drawy(), "ui.forbidden");
                    return false; // Explicitly return false here
                }
            }

            return true; // Return true if no conditions are met that would return false
        });

        Timer.schedule(()->state.rules.waveTeam.data().units.each(unit->{
            var core = unit.closestEnemyCore();
            if(core == null || unit.dst(core) > 60f) return;

            core.damage(unit.health / Mathf.sqrt(multiplier), true);
            

            unit.kill();

        }), 0f, 1f);

        Timer.schedule(()->Bundle.popup(1f, 20, 50, 20, 450, 0, "ui.multiplier", Color.HSVtoRGB(multiplier * 120f, 100f, 100f), Strings.autoFixed(multiplier, 2)), 0f, 1f);

        Events.on(EventType.WorldLoadEvent.class, event->multiplier = 0.5f);
        Events.on(EventType.WaveEvent.class, event->multiplier = Mathf.clamp(((state.wave * state.wave / 3200f) + 0.5f), multiplier, 100f));
        Events.on(EventType.GameOverEvent.class, event -> Players.clearMap());
          Events.on(EventType.UnitDestroyEvent.class, event->{
       if(event.unit.team != state.rules.waveTeam) return;

        var core = event.unit.closestEnemyCore();
        var drop = drops.get(event.unit.type);

        if(core == null || drop == null) return;

        var builder = new StringBuilder();

        drop.each(stack-> {
        int amount = Mathf.random(stack.amount - stack.amount /   2, stack.amount + stack.amount /   2);

        builder.append("[accent]+").append(amount).append(" [green]").append(stack.item.emoji()).append("  ");
        Call.transferItemTo(event.unit, stack.item, core.acceptStack(stack.item, amount, core), event.unit.x, event.unit.y, core);
         });

        Call.label(builder.toString(),   1f, event.unit.x + Mathf.range(4f), event.unit.y + Mathf.range(4f));

       // Add points with a    chance to all players
       if(Mathf.random() <   0.1f) {
        float pointsToAdd = Mathf.random(0f,   4f);
        for(ObjectMap.Entry<String, PlayerData> entry : PlayerData.allPlayerDataInstances) {
            PlayerData playerData = entry.value;
            playerData.addPoints(pointsToAdd);
        }
    }
Events.on(EventType.PlayerLeave.class, leaveEvent -> {
    // Get the points of the leaving player
    PlayerData leavingPlayerData = Players.getPlayer(leaveEvent.player);
    float leavingPlayerPoints = leavingPlayerData.getPoints();

    // Calculate the points per player
    int remainingPlayers = PlayerData.allPlayerDataInstances.size -  1; // Subtract  1 because the leaving player is not included in the count
    float pointsPerPlayer = leavingPlayerPoints / remainingPlayers;

    // Calculate points to add to each remaining player
    float pointsToAdd = pointsPerPlayer; // Assuming you want to add the average points per player to each remaining player

    // Distribute the points to the remaining players
    for (PlayerData playerData : Players.players.values()) {
        playerData.addPoints(pointsToAdd);
    }
});
            if(event.unit.team != state.rules.waveTeam) 

            event.unit.health(event.unit.health * multiplier);
            event.unit.maxHealth(event.unit.maxHealth * multiplier);
            event.unit.damageMultiplier(0f);
            event.unit.apply(StatusEffects.overdrive, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.overclock, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.shielded, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.boss, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.slow, Float.POSITIVE_INFINITY);
            event.unit.apply(StatusEffects.disarmed, Float.POSITIVE_INFINITY);
            event.unit.type.speed = 1.5f;
            event.unit.type.range = -1f;
            event.unit.type.hovering = true;
            event.unit.type.abilities.clear();
            event.unit.type.physics = false;

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
