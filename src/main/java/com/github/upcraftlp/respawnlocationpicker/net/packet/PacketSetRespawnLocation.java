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
public class PacketSetRespawnLocation implements IMessage, IMessageHandler<PacketSetRespawnLocation, IMessage> {

    private int targetIndex;

    public PacketSetRespawnLocation() {

    }

    public PacketSetRespawnLocation(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.targetIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.targetIndex);
    }

    @Override
    public IMessage onMessage(PacketSetRespawnLocation message, MessageContext ctx) {
        EntityPlayerMP playerMP = ctx.getServerHandler().player;
        IRespawnLocations locations = playerMP.getCapability(CapabilityHandler.CAPABILITY, null);
        List<TargetPoint4d> targetList = locations.getRespawnLocations(playerMP.world, ModConfig.respawnLocations);
        TargetPoint4d target = targetList.get(message.targetIndex);
        ctx.getServerHandler().player.setPositionAndUpdate(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D); //TODO dimension
        return null;
    }
}
