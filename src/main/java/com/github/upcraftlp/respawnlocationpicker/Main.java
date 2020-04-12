package com.github.upcraftlp.respawnlocationpicker;

import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.command.CommandAddRespawnPoint;
import com.github.upcraftlp.respawnlocationpicker.command.CommandClearRespawns;
import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketRespawnNextTo;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketRespawnPlayer;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketSetRespawnLocation;
import com.github.upcraftlp.respawnlocationpicker.proxy.Proxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.upcraftlp.respawnlocationpicker.Reference.CLIENT_PROXY;
import static com.github.upcraftlp.respawnlocationpicker.Reference.DEPENDENCIES;
import static com.github.upcraftlp.respawnlocationpicker.Reference.MCVERSIONS;
import static com.github.upcraftlp.respawnlocationpicker.Reference.MODID;
import static com.github.upcraftlp.respawnlocationpicker.Reference.MODNAME;
import static com.github.upcraftlp.respawnlocationpicker.Reference.SERVER_PROXY;
import static com.github.upcraftlp.respawnlocationpicker.Reference.VERSION;

@Mod(
        name = MODNAME,
        version = VERSION,
        acceptedMinecraftVersions = MCVERSIONS,
        modid = MODID,
        dependencies = DEPENDENCIES
)
public class Main {

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static Proxy proxy;

    private static final Logger log = LogManager.getLogger(MODID);

    public static Logger getLogger() {
        return log;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityRespawnLocations.register();
        NetworkHandler.INSTANCE.registerMessage(PacketRespawnPlayer.class, PacketRespawnPlayer.class, 0, Side.SERVER);
        NetworkHandler.INSTANCE.registerMessage(PacketSetRespawnLocation.class, PacketSetRespawnLocation.class, 1, Side.SERVER);
        NetworkHandler.INSTANCE.registerMessage(PacketRespawnNextTo.class, PacketRespawnNextTo.class, 3, Side.SERVER);
        proxy.preInit(event);
        log.info("Pre-Initialization finished.");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandClearRespawns());
        event.registerServerCommand(new CommandAddRespawnPoint());
        log.info("World initialization complete.");
    }

}
