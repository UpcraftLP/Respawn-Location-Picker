package com.github.upcraftlp.respawnlocationpicker.client.gui;

import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketSetRespawnLocation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

/**
 * @author UpcraftLP
 */
public class GuiRespawnLocations extends GuiScreen {

    private final NonNullList<TargetPoint4d> targets;
    private final boolean hasHoverText;
    private NonNullList<String> labels = null;
    private static final int
            BUTTON_HEIGHT = 20,
            MARGIN = 8;
    private int BUTTON_WIDTH = 100;


    public GuiRespawnLocations(NonNullList<TargetPoint4d> targets, boolean showHoverText) {
        this.targets = targets;
        this.hasHoverText = showHoverText;
        if(showHoverText) labels = NonNullList.create();
    }

    @Override
    public void initGui() {
        int x = width / 2;
        int y = width / 10 + 10;

        //make sure to adapt the size of the buttons to the label widths
        for(TargetPoint4d target : this.targets) {
            int width = mc.fontRenderer.getStringWidth(target.getName()) + (MARGIN * 2);
            if(width > BUTTON_WIDTH) BUTTON_WIDTH = width;
        }
        for(int i = 0; i < this.targets.size(); i++) {
            if(hasHoverText) {
                labels.add(getStringForTarget(targets.get(i), true));
            }
            int xPos = x - (BUTTON_WIDTH + 5) + ((i % 2) * (BUTTON_WIDTH + 10));
            this.buttonList.add(new GuiButtonExt(i, xPos, y + (i / 2) * (BUTTON_HEIGHT + 5), BUTTON_WIDTH, BUTTON_HEIGHT, targets.get(i).getName()));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.respawnLocation.title"), this.width / 2 / 2, 15, 16777215);
        GlStateManager.popMatrix();
        if(hasHoverText) {
            for (GuiButton button : this.buttonList) {
                if(button.isMouseOver()) {
                    drawHoveringText(labels.get(button.id), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        NetworkHandler.INSTANCE.sendToServer(new PacketSetRespawnLocation(button.id));
    }

    private static String getStringForTarget(TargetPoint4d target, boolean includeFormatting) {
        StringBuilder builder = new StringBuilder();
        if(target.hasPosition()) {
            builder
                    //DIMENSION
                    .append(includeFormatting ? TextFormatting.DARK_AQUA : "")
                    .append("DIM ").append(target.getDimension()).append(":")
                    .append(includeFormatting ? TextFormatting.RESET : "")
                    .append(" ")

                    //POSITION
                    .append(includeFormatting ? TextFormatting.DARK_PURPLE : "")
                    .append("[")
                    .append(target.getX())
                    .append(", ")
                    .append(target.getY())
                    .append(", ")
                    .append(target.getZ()).append("]");
        }
        if(target.hasBiome()) {
            builder
                    .append("(")
                    .append(target.getBiome())
                    .append(")");
        }
        return builder.toString();
    }

}
