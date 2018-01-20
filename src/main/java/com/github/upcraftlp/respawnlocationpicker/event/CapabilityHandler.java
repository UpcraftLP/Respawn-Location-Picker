package com.github.upcraftlp.respawnlocationpicker.event;

import com.github.upcraftlp.respawnlocationpicker.Reference;
import com.github.upcraftlp.respawnlocationpicker.api.CapabilityProviderRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author UpcraftLP
 */
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class CapabilityHandler {

    public static final ResourceLocation CAPABILITY = new ResourceLocation(Reference.MODID, "respawns");
    private static final Field SPAWN_CHUNK_MAP;

    static {
        Field spawnChunkMap;
        try {
            spawnChunkMap = EntityPlayer.class.getDeclaredField("spawnChunkMap");
        } catch (NoSuchFieldException e) { //should never happen
            e.printStackTrace();
            spawnChunkMap = null;
        }
        SPAWN_CHUNK_MAP = spawnChunkMap;
    }

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityPlayerMP) event.addCapability(CAPABILITY, new CapabilityProviderRespawnLocations());
    }

    @SubscribeEvent
    public static void onPlayerDied(PlayerEvent.Clone event) {
        IRespawnLocations old = event.getOriginal().getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);
        IRespawnLocations locations = event.getEntityPlayer().getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);

        List<TargetPoint4d> targets = Lists.newArrayList();
        targets.addAll(old.getRespawnLocations(old.getLocationCount()));
        locations.clearRespawnLocations();
        for(TargetPoint4d target : targets) {
            locations.addRespawnLocation(target);
        }
    }

    @SubscribeEvent
    public static void onRightClickBed(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if(world.isRemote) return;
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        EntityPlayer player = event.getEntityPlayer();
        if(state.getBlock().isBed(state, world, pos, player)) {
            IRespawnLocations locations = player.getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);
            boolean result = locations.addRespawnLocation(new TargetPoint4d(player.getBedLocation(), player.dimension, "Bed", false));
            if(result && world.isDaytime()) {
                event.setCanceled(true);
                player.sendStatusMessage(new TextComponentTranslation(Reference.MODID + ".setBedSpawn.success").setStyle(new Style().setColor(TextFormatting.WHITE)), true);
            }
        }
    }
}
