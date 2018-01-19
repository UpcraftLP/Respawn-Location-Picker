package com.github.upcraftlp.respawnlocationpicker.client;

import com.github.upcraftlp.respawnlocationpicker.Reference;
import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketRespawnPlayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author UpcraftLP
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.MODID, value = {Side.CLIENT})
public class RespawnHandler {

    public static void onPlayerClickRespawn(GuiScreenEvent.ActionPerformedEvent event) {
        if(event.getGui() instanceof GuiGameOver && event.getButton().id == 0) {
            event.setCanceled(true);
            NetworkHandler.INSTANCE.sendToServer(new PacketRespawnPlayer());
        }
    }
}
