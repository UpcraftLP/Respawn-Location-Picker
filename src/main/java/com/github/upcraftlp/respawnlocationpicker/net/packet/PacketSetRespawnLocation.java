package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.capability.CapabilityProviderRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        NetHandlerPlayServer netHandler = ctx.getServerHandler();
        EntityPlayerMP playerMP = netHandler.playerEntity;
        IRespawnLocations locations = playerMP.getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);

        TargetPoint4d target = null;
        TargetPoint4d destination; //needed for lambda expression


        if(ModConfig.showWorldSpawn || locations.getLocationCount() == 0) {
            if(message.targetIndex == 0) {
                World world = playerMP.world; //getspawndimension
                if(!world.provider.canRespawnHere()) {
                    world = server.worldServerForDimension(world.provider.getRespawnDimension(playerMP));
                }
                BlockPos spawn = world.getSpawnPoint();
                target = new TargetPoint4d(spawn, world.provider.getDimension(), "World Spawn", TargetHelper.getBiome(world, spawn));
            }
            message.targetIndex -= 1;
        }
        destination = target != null ? target : locations.getRespawnLocations(ModConfig.respawnLocations).get(message.targetIndex);
        playerMP.setSpawnChunk(destination.getPosition(), true, destination.getDimension());


        server.addScheduledTask(() -> {
            netHandler.processClientStatus(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
            netHandler.player.connection.setPlayerLocation(destination.getX() + 0.5D, destination.getY(), destination.getZ() + 0.5D, 0.0F, 0.0F);
            netHandler.playerEntity.closeScreen();
        });
        return null;
    }
}
