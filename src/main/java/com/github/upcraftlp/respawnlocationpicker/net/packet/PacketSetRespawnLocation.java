package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.capability.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.RadiusHelper;
import com.github.upcraftlp.respawnlocationpicker.util.RespawnTeleporter;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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
        EntityPlayerMP playerMP = netHandler.player;
        IRespawnLocations locations = playerMP.getCapability(CapabilityRespawnLocations.CAPABILITY, null);

        TargetPoint4d target = null;
        TargetPoint4d destination; //needed for lambda expression


        if(ModConfig.showWorldSpawn || locations.getLocationCount() == 0) {
            if(message.targetIndex == 0) {
                World world = server.getWorld(playerMP.getSpawnDimension());
                if(!world.provider.canRespawnHere()) {
                    world = server.getWorld(world.provider.getRespawnDimension(playerMP));
                }
                BlockPos spawn = world.getSpawnPoint();
                target = new TargetPoint4d(spawn, world.provider.getDimension(), new TextComponentTranslation("gui.respawnLocation.world"), new TextComponentString(TargetHelper.getBiome(world, spawn)));
            }
            message.targetIndex -= 1;
        }

        if(ModConfig.gravesEnabled) {
            int compareIndex = ModConfig.showWorldSpawn || locations.getLocationCount() == 0 ? 1 : 0;
            if(message.targetIndex == compareIndex) {
                int dim = playerMP.dimension;
                World w = server.getWorld(dim);
                BlockPos pos = RadiusHelper.getRandomRadiusPoint(playerMP.getPosition(), ModConfig.graveRange, 20, w, playerMP);
                target = new TargetPoint4d(pos, dim, new TextComponentTranslation("gui.respawnLocation.grave"), new TextComponentString(TargetHelper.getBiome(w, pos)));
            }
            message.targetIndex -= 1;
        }

        destination = target != null ? target : locations.getRespawnLocations(ModConfig.respawnLocations).get(message.targetIndex);
        int targetDim = destination.getDimension();
        playerMP.setSpawnChunk(destination.getPosition(), true, targetDim);

        server.addScheduledTask(() -> {
            netHandler.player.closeScreen();
            netHandler.processClientStatus(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
            if(targetDim != netHandler.player.dimension)
                netHandler.player.changeDimension(targetDim, new RespawnTeleporter(destination.getPosition()));
        });
        return null;
    }
}
