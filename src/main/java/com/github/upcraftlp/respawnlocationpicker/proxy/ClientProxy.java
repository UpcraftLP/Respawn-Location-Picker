package com.github.upcraftlp.respawnlocationpicker.proxy;

import com.github.upcraftlp.respawnlocationpicker.client.net.PacketHandlerRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketRespawnLocations;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        NetworkHandler.INSTANCE.registerMessage(new PacketHandlerRespawnLocations(), PacketRespawnLocations.class, packetID++, Side.CLIENT);
    }
}
