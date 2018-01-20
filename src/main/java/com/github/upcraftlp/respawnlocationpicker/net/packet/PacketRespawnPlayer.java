package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.CapabilityProviderRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
        IRespawnLocations respawnLocations = player.getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);
        int listLength = ModConfig.respawnLocations;
        if(ModConfig.showWorldSpawn) listLength -= 1;
        List<TargetPoint4d> targets = respawnLocations.getRespawnLocations(listLength);
        if(ModConfig.showWorldSpawn) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            int dimension = player.world.provider.getRespawnDimension(player);
            World respawnWorld = server.getWorld(dimension);
            targets.add(0, new TargetPoint4d(respawnWorld.getSpawnPoint(), dimension, "World Spawn", true));
        }
        return new PacketRespawnLocations(targets);
    }
}