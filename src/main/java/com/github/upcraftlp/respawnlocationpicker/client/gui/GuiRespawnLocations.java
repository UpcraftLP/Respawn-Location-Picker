package com.github.upcraftlp.respawnlocationpicker.client.gui;

import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketSetRespawnLocation;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author UpcraftLP
 */
public class GuiRespawnLocations extends GuiScreen {

    private TargetPoint4d[] targets;
    private String[] labels;
    private static final int
            BUTTON_HEIGHT = 20,
            MARGIN = 8;
    private int BUTTON_WIDTH = 100;


    public GuiRespawnLocations(TargetPoint4d[] targets) {
        this.targets = targets;
    }

    @Override
    public void initGui() {
        int x = width / 2;
        int y = width / 10 + 10;
        labels = new String[this.targets.length];

        //make sure to adapt the size of the buttons to the label widths
        for(TargetPoint4d target : this.targets) {
            int width = mc.fontRenderer.getStringWidth(target.getName()) + (MARGIN * 2);
            if(width > BUTTON_WIDTH) BUTTON_WIDTH = width;

        }
        for(int i = 0; i < this.targets.length; i++) {
            labels[i] = getStringForTarget(targets[i], true);
            int xPos = x - (BUTTON_WIDTH + 5) + ((i % 2) * (BUTTON_WIDTH + 10));
            this.buttonList.add(new GuiButtonExt(i, xPos, y + (i / 2) * (BUTTON_HEIGHT + 5), BUTTON_WIDTH, BUTTON_HEIGHT, targets[i].getName()));
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.buttonList.clear();
        this.targets = new TargetPoint4d[0];
        this.labels = new String[0];
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.respawnLocation.title"), this.width / 2 / 2, 15, 16777215);
        GlStateManager.popMatrix();
        for (GuiButton button : this.buttonList) {
            if(button.isMouseOver()) {
                drawHoveringText(labels[button.id], mouseX, mouseY);
            }
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        NetworkHandler.INSTANCE.sendToServer(new PacketSetRespawnLocation(button.id));
    }

    public static String getStringForTarget(TargetPoint4d target, boolean includeFormatting) {
        return
                //DIMENSION
                (includeFormatting ? TextFormatting.DARK_AQUA : "") +
                "DIM " +
                target.getDimension() +
                ":" +
                (includeFormatting ? TextFormatting.RESET : "") +

                //SPAWNPOINT LOCATION
                " " +
                (includeFormatting ? TextFormatting.DARK_PURPLE : "") +
                "[" +
                target.getX() +
                ", " +
                target.getY() +
                ", " +
                target.getZ() +
                "]";
    }

}
