package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityHandler;
import com.github.upcraftlp.respawnlocationpicker.capability.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
        //NO-OP
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
        playerMP.setSpawnDimension(target.getDimension());
        playerMP.setSpawnPoint(target.getPosition(), true);
        ctx.getServerHandler().player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().recreatePlayerEntity(playerMP, target.getDimension(), false);
        playerMP = ctx.getServerHandler().player;
        playerMP.setLocationAndAngles(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, 0.0F, 0.0F);
        playerMP.setSpawnDimension(target.getDimension());
        playerMP.setSpawnPoint(target.getPosition(), true);
        playerMP.dimension = target.getDimension();
        playerMP.closeScreen();
        return null;
    }
}
