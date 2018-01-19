package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityHandler;
import com.github.upcraftlp.respawnlocationpicker.capability.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
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
        IRespawnLocations respawnLocations = player.getCapability(CapabilityHandler.CAPABILITY, null);
        List<TargetPoint4d> targets = respawnLocations.getRespawnLocations(player.world, ModConfig.respawnLocations);
        return new PacketRespawnLocations(targets);
    }
}
