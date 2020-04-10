package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.capability.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

/**
 * @author UpcraftLP
 */
public class PacketRespawnPlayer implements IMessage, IMessageHandler<PacketRespawnPlayer, PacketRespawnLocations> {

    @Override
    public void fromBytes(ByteBuf buf) {
        //NO-OP
    }

    @Override
    public void toBytes(ByteBuf buf) {
        //NO-OP
    }

    @Override
    public PacketRespawnLocations onMessage(PacketRespawnPlayer message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        IRespawnLocations respawnLocations = player.getCapability(CapabilityRespawnLocations.CAPABILITY, null);
        int listLength = ModConfig.respawnLocations;
        if(ModConfig.showWorldSpawn) listLength -= 1;
        List<TargetPoint4d> targets = Lists.newArrayList();
        if(ModConfig.showWorldSpawn || respawnLocations.getLocationCount() == 0) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            int dimension = player.world.provider.getRespawnDimension(player);
            World respawnWorld = server.getWorld(dimension);
            BlockPos spawn = respawnWorld.getSpawnPoint();
            targets.add(new TargetPoint4d(spawn, dimension, "World Spawn", TargetHelper.getBiome(respawnWorld, spawn)));
        }
        targets.addAll(respawnLocations.getRespawnLocations(listLength));
        return new PacketRespawnLocations(targets);
    }
}
