package com.github.upcraftlp.respawnlocationpicker.event;

import com.github.upcraftlp.respawnlocationpicker.Reference;
import com.github.upcraftlp.respawnlocationpicker.api.capability.CapabilityProviderRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.CompatHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.google.common.collect.Lists;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.Mod;
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
    public static void onSetSpawn(PlayerSetSpawnEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        if(!world.isRemote) {
            BlockPos pos = event.getNewSpawn();
            IRespawnLocations locations = player.getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);
            for (TargetPoint4d targets : locations.getRespawnLocations(locations.getLocationCount())) {
                if(targets.getDimension() == player.dimension && targets.getPosition().equals(pos)) return;
            }
            String name = "Spawnpoint";

            if(CompatHelper.isModLoaded(CompatHelper.WAYSTONES)) {
                for(EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings()) {
                    IBlockState blockState = world.getBlockState(pos.offset(facing));
                    if(blockState.getBlock() instanceof BlockWaystone) {
                        EnumFacing direction = blockState.getValue(BlockWaystone.FACING);
                        if(direction.getOpposite() == facing) {
                            TileEntity tileEntity = world.getTileEntity(pos);
                            if(tileEntity instanceof TileWaystone) {
                                name = ((TileWaystone) tileEntity).getWaystoneName();
                            }
                        }
                    }
                }
            }

            locations.addRespawnLocation(new TargetPoint4d(pos, player.dimension, name, TargetHelper.getBiome(player.world, pos)));
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
            boolean result = locations.addRespawnLocation(new TargetPoint4d(pos, world.provider.getDimension(), "Bed", TargetHelper.getBiome(world, pos)));
            if(result && world.isDaytime()) {
                event.setCanceled(true);
                player.sendStatusMessage(new TextComponentTranslation("respawns.setBedSpawn.success").setStyle(new Style().setColor(TextFormatting.WHITE)), true);
            }
        }
    }
}
