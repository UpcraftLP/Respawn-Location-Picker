package com.github.upcraftlp.respawnlocationpicker.client.gui;

import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketSetRespawnLocation;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * @author UpcraftLP
 */
public class GuiRespawnLocations extends GuiScreen {

    private TargetPoint4d[] targets;

    public GuiRespawnLocations(TargetPoint4d[] targets) {
        this.targets = targets;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, 100, 100, "test"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        NetworkHandler.INSTANCE.sendToServer(new PacketSetRespawnLocation(0));
    }
}
