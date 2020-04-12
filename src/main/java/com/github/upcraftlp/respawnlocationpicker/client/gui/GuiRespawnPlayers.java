package com.github.upcraftlp.respawnlocationpicker.client.gui;

import com.github.upcraftlp.respawnlocationpicker.net.NetworkHandler;
import com.github.upcraftlp.respawnlocationpicker.net.packet.PacketRespawnNextTo;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuiRespawnPlayers extends GuiScreen {

    private final GuiScreen previous;

    private static final Ordering<NetworkPlayerInfo> ENTRY_ORDERING = Ordering.from(new PlayerComparator());
    private static final int BUTTON_HEIGHT = 20, MARGIN = 8;
    private int BUTTON_WIDTH = 100;

    private List<NetworkPlayerInfo> players;

    public GuiRespawnPlayers(GuiScreen previous) {
        this.previous = previous;
        this.players = new ArrayList<>();
    }

    @Override
    public void initGui() {
        NetHandlerPlayClient handler = this.mc.player.connection;
        this.players = ENTRY_ORDERING.sortedCopy(handler.getPlayerInfoMap());
        populateButtons();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        checkChange();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.respawnLocation.players"), this.width / 2 / 2, 15, 16777215);
        GlStateManager.popMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        NetworkHandler.INSTANCE.sendToServer(new PacketRespawnNextTo(this.players.get(button.id).getGameProfile().getId()));
    }

    @Override
    public void onGuiClosed() {

    }

    private void checkChange() {
        NetHandlerPlayClient handler = this.mc.player.connection;
        List<NetworkPlayerInfo> copy = ENTRY_ORDERING.sortedCopy(handler.getPlayerInfoMap());
        if(copy.size() != players.size()) {
            this.players = copy;
            populateButtons();
        }
    }

    private void populateButtons() {
        this.buttonList.clear();

        int x = width / 2;
        int y = width / 10 + 10;

        for(NetworkPlayerInfo p : this.players) {
            int width = mc.fontRenderer.getStringWidth(p.getGameProfile().getName()) + (MARGIN * 2);
            if(width > BUTTON_WIDTH) BUTTON_WIDTH = width;
        }
        for(int i = 0; i < this.players.size(); i++) {
            int xPos = x - (BUTTON_WIDTH + 5) + ((i % 2) * (BUTTON_WIDTH + 10));
            this.buttonList.add(new GuiButtonExt(i, xPos, y + (i / 2) * (BUTTON_HEIGHT + 5), BUTTON_WIDTH, BUTTON_HEIGHT, this.players.get(i).getGameProfile().getName()));
        }
    }

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        public int compare(NetworkPlayerInfo info1, NetworkPlayerInfo info2) {
            ScorePlayerTeam scoreplayerteam = info1.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = info2.getPlayerTeam();
            return ComparisonChain.start()
                    .compareTrueFirst(info1.getGameType() != GameType.SPECTATOR, info2.getGameType() != GameType.SPECTATOR)
                    .compare(scoreplayerteam != null ? scoreplayerteam.getName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getName() : "")
                    .compare(info1.getGameProfile().getName(), info2.getGameProfile().getName())
                    .result();
        }
    }
}
