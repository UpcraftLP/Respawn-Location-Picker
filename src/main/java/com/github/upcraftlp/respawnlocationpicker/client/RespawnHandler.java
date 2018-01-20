package com.github.upcraftlp.respawnlocationpicker.client;

import com.github.upcraftlp.respawnlocationpicker.Main;
import com.github.upcraftlp.respawnlocationpicker.Reference;
import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketRespawnPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author UpcraftLP
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.MODID, value = {Side.CLIENT})
public class RespawnHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public static void onPlayerClickRespawn(GuiScreenEvent.ActionPerformedEvent event) {
        final GuiScreen guiScreen = event.getGui();
        if(guiScreen instanceof GuiGameOver) {
            event.setCanceled(true);
            switch (event.getButton().id) {
                case 0:
                    NetworkHandler.INSTANCE.sendToServer(new PacketRespawnPlayer());
                    break;
                case 1:
                   mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiYesNo((result, id) -> {
                       if(result) guiScreen.confirmClicked(result, id);
                       else NetworkHandler.INSTANCE.sendToServer(new PacketRespawnPlayer());
                   }, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0)));
            }
        }
    }
}
