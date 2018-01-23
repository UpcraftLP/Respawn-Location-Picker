package com.github.upcraftlp.respawnlocationpicker.client.net;

import com.github.upcraftlp.respawnlocationpicker.client.gui.GuiRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketRespawnLocations;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author UpcraftLP
 */
@SideOnly(Side.CLIENT)
public class PacketHandlerRespawnLocations implements IMessageHandler<PacketRespawnLocations, IMessage> {

    @Override
    public IMessage onMessage(PacketRespawnLocations message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean showHoverText = message.hasBiome ||message.hasPos;
        mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiRespawnLocations(message.targets, showHoverText)));
        return null;
    }
}
