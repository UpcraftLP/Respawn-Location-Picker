package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.client.gui.GuiRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

/**
 * @author UpcraftLP
 */
public class PacketRespawnLocations implements IMessage, IMessageHandler<PacketRespawnLocations, IMessage> {

    public TargetPoint4d[] targets;

    public PacketRespawnLocations() {
        //NO-OP
    }

    public PacketRespawnLocations(List<TargetPoint4d> targets) {
        this.targets = targets.toArray(new TargetPoint4d[targets.size()]);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        this.targets = new TargetPoint4d[length];
        for(int i = 0; i < length; i++) {
            targets[i] = new TargetPoint4d(BlockPos.fromLong(buf.readLong()), buf.readInt(), ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.targets.length);
        for(TargetPoint4d target : this.targets) {
            buf.writeLong(target.getPosition().toLong());
            buf.writeInt(target.getDimension());
            ByteBufUtils.writeUTF8String(buf, target.getName());
        }
    }

    @Override
    public IMessage onMessage(PacketRespawnLocations message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiRespawnLocations(message.targets)));
        return null;
    }
}
