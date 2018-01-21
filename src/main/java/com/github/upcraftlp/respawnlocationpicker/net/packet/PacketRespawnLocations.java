package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.client.TargetPoint4dClient;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.github.upcraftlp.respawnlocationpicker.client.gui.GuiRespawnLocations;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.NonNullList;
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

    private NonNullList<TargetPoint4d> targets = NonNullList.create();
    private boolean
            hasPos = false,
            hasBiome = false;

    public PacketRespawnLocations() {
        //NO-OP
    }

    public PacketRespawnLocations(List<TargetPoint4d> targets) {
        this.targets.addAll(targets);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        this.hasPos = buf.readBoolean();
        this.hasBiome = buf.readBoolean();
        for(int i = 0; i < length; i++) {
            TargetPoint4dClient targetPoint = new TargetPoint4dClient();
            if(this.hasPos) targetPoint.setPosition(BlockPos.fromLong(buf.readLong()), buf.readInt());
            targetPoint.setName(ByteBufUtils.readUTF8String(buf));
            if(this.hasBiome) targetPoint.setBiome(ByteBufUtils.readUTF8String(buf));
            this.targets.add(targetPoint);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.hasPos = ModConfig.displayMode.posIncluded();
        this.hasBiome = ModConfig.displayMode.biomeIncluded();
        buf.writeInt(this.targets.size());
        buf.writeBoolean(this.hasPos);
        buf.writeBoolean(this.hasBiome);
        for(TargetPoint4d target : this.targets) {
            if(this.hasPos) {
                buf.writeLong(target.getPosition().toLong());
                buf.writeInt(target.getDimension());
            }
            ByteBufUtils.writeUTF8String(buf, target.getName());
            if(this.hasBiome) ByteBufUtils.writeUTF8String(buf, target.getBiome());
        }
    }

    @Override
    public IMessage onMessage(PacketRespawnLocations message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean showHoverText = message.hasBiome ||message.hasPos;
        mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiRespawnLocations(message.targets, showHoverText)));
        return null;
    }
}
