package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.client.TargetPoint4dClient;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.List;

/**
 * @author UpcraftLP
 */
public class PacketRespawnLocations implements IMessage {

    public NonNullList<TargetPoint4d> targets = NonNullList.create();

    public boolean hasPos = false;
    public boolean hasBiome = false;

    @SuppressWarnings("unused")
    public PacketRespawnLocations() {
        //NO-OP
    }

    public PacketRespawnLocations(List<TargetPoint4d> targets) {
        this.targets.addAll(targets);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.hasPos = buf.readBoolean();
        this.hasBiome = buf.readBoolean();
        int length = buffer.readVarInt();
        for(int i = 0; i < length; i++) {
            TargetPoint4dClient targetPoint = new TargetPoint4dClient();
            if(this.hasPos) {
                BlockPos pos = buffer.readBlockPos();
                int dim = buffer.readInt();
                targetPoint.setPosition(pos, dim);
            }
            targetPoint.setName(ITextComponent.Serializer.fromJsonLenient(buffer.readString(65535)));
            if(this.hasBiome) {
                targetPoint.setBiome(ITextComponent.Serializer.fromJsonLenient(buffer.readString(65535)));
            }
            this.targets.add(targetPoint);
        }
        buffer.clear();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.hasPos = ModConfig.displayMode.posIncluded();
        this.hasBiome = ModConfig.displayMode.biomeIncluded();
        buffer.writeBoolean(this.hasPos);
        buffer.writeBoolean(this.hasBiome);
        buffer.writeVarInt(this.targets.size());
        for(TargetPoint4d target : this.targets) {
            if(this.hasPos) {
                buffer.writeBlockPos(target.getPosition());
                buffer.writeInt(target.getDimension());
            }
            buffer.writeString(ITextComponent.Serializer.componentToJson(target.getName()));
            if(this.hasBiome) buffer.writeString(ITextComponent.Serializer.componentToJson(target.getBiome()));
        }
    }

}
