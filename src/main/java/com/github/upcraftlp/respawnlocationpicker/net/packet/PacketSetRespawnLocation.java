package com.github.upcraftlp.respawnlocationpicker.net.packet;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.CapabilityProviderRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;
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
        IRespawnLocations locations = playerMP.getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);

        TargetPoint4d target;
        if(message.targetIndex == 0 && ModConfig.showWorldSpawn) {
            int dimension = playerMP.getSpawnDimension();
            World world = server.getWorld(dimension);
            if(!world.provider.canRespawnHere()) {
                dimension = world.provider.getRespawnDimension(playerMP);
            }
            target = new TargetPoint4d(server.getWorld(dimension).getSpawnPoint(), dimension, "World Spawn", true);
        }
        else {
            target = locations.getRespawnLocations(ModConfig.respawnLocations).get(message.targetIndex);
        }
        playerMP.setSpawnDimension(target.getDimension());
        playerMP.setSpawnChunk(target.getPosition(), target.isBed, target.getDimension());

        //netHandler.processClientStatus(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
        server.addScheduledTask(() -> {
            playerMP.markPlayerActive();
            netHandler.player = server.getPlayerList().recreatePlayerEntity(playerMP, target.getDimension(), false);
            if (server.isHardcore()) {
                netHandler.player.setGameType(GameType.SPECTATOR);
                netHandler.player.getServerWorld().getGameRules().setOrCreateGameRule("spectatorsGenerateChunks", "false");
            }
            netHandler.player.closeScreen();
            netHandler.player.setLocationAndAngles(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, 0.0F, 0.0F);
        });
        return null;
    }
}
