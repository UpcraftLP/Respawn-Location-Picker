package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.util.RadiusHelper;
import com.github.upcraftlp.respawnlocationpicker.util.RespawnTeleporter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketRespawnNextTo implements IMessage, IMessageHandler<PacketRespawnNextTo, IMessage> {

    private UUID target;

    public PacketRespawnNextTo() { }

    public PacketRespawnNextTo(UUID target) {
        this.target = target;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        PacketBuffer buf = new PacketBuffer(byteBuf);
        buf.writeUniqueId(target);
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        PacketBuffer buf = new PacketBuffer(byteBuf);
        this.target = buf.readUniqueId();
        buf.clear();
    }

    @Override
    public IMessage onMessage(PacketRespawnNextTo packet, MessageContext ctx) {

        NetHandlerPlayServer handler = ctx.getServerHandler();
        MinecraftServer server = handler.player.server;
        EntityPlayerMP target = server.getPlayerList().getPlayerByUUID(packet.getTarget());

        if(handler.player.getUniqueID().equals(target.getUniqueID())) {
            //Player UUID and TargetUUID matches. Can literally not happen unless hacking is involved.
            handler.disconnect(new TextComponentString("You done fucked up, boiiiiiiiii!"));
            return null;
        }

        int dim = target.dimension;
        BlockPos pos = RadiusHelper.getRandomRadiusPoint(target.getPosition(), 3, 5, target.world, handler.player);
        handler.player.setSpawnChunk(pos, true, dim);
        server.addScheduledTask(() -> {
            handler.player.closeScreen();
            handler.processClientStatus(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
            if(dim != handler.player.dimension)
                handler.player.changeDimension(dim, new RespawnTeleporter(pos));
        });
        return null;
    }

    public UUID getTarget() {
        return target;
    }
}
