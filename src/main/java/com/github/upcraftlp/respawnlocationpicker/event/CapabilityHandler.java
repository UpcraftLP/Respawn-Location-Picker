package com.github.upcraftlp.respawnlocationpicker.event;

import com.github.upcraftlp.respawnlocationpicker.Reference;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.capability.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.compat.WaystonesCompat;
import com.github.upcraftlp.respawnlocationpicker.util.CapabilityProviderSerializable;
import com.github.upcraftlp.respawnlocationpicker.util.CompatHelper;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * @author UpcraftLP
 */
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class CapabilityHandler {

    public static final ResourceLocation CAPABILITY = new ResourceLocation(Reference.MODID, "respawns");

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityPlayerMP) event.addCapability(CAPABILITY, new CapabilityProviderSerializable<>(CapabilityRespawnLocations.CAPABILITY, null));
    }

    @SubscribeEvent
    public static void onPlayerDied(PlayerEvent.Clone event) {
        IRespawnLocations old = event.getOriginal().getCapability(CapabilityRespawnLocations.CAPABILITY, null);
        IRespawnLocations locations = event.getEntityPlayer().getCapability(CapabilityRespawnLocations.CAPABILITY, null);

        List<TargetPoint4d> targets = Lists.newArrayList();
        targets.addAll(old.getRespawnLocations(old.getLocationCount()));
        locations.clearRespawnLocations();
        for(TargetPoint4d target : targets) {
            locations.addRespawnLocation(target);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSetSpawn(PlayerSetSpawnEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        if(!world.isRemote) {
            BlockPos pos = event.getNewSpawn();
            IRespawnLocations locations = player.getCapability(CapabilityRespawnLocations.CAPABILITY, null);
            for (TargetPoint4d targets : locations.getRespawnLocations(locations.getLocationCount())) {
                if(targets.getDimension() == player.dimension && targets.getPosition().equals(pos)) return;
            }
            ITextComponent name = new TextComponentTranslation("gui.respawnLocation.point");
            if(CompatHelper.isModLoaded(CompatHelper.WAYSTONES)) {
                String waystone = WaystonesCompat.getWaystoneName(world, pos);
                if(waystone != null) {
                    name = new TextComponentString(waystone);
                }
            }

            locations.addRespawnLocation(new TargetPoint4d(pos, player.dimension, name, new TextComponentString(TargetHelper.getBiome(player.world, pos))));
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
            if(state.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD) {
                pos = pos.offset(state.getValue(BlockBed.FACING));
            }
            TileEntityBed bed = (TileEntityBed)world.getTileEntity(pos);
            IRespawnLocations locations = player.getCapability(CapabilityRespawnLocations.CAPABILITY, null);
            boolean result = locations.addRespawnLocation(new TargetPoint4d(pos.up(), world.provider.getDimension(), new TextComponentTranslation(bed.getItemStack().getTranslationKey() + ".name"), new TextComponentString(TargetHelper.getBiome(world, pos))));
            if(result && world.isDaytime()) {
                event.setCanceled(true);
                player.sendStatusMessage(new TextComponentTranslation("respawns.setBedSpawn.success").setStyle(new Style().setColor(TextFormatting.WHITE)), true);
            }
        }
    }
}
