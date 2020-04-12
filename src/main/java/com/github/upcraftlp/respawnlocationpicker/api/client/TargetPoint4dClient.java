package com.github.upcraftlp.respawnlocationpicker.api.client;

import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author UpcraftLP
 */
@SideOnly(Side.CLIENT)
public class TargetPoint4dClient extends TargetPoint4d {

    private boolean hasPosition = false;

    public TargetPoint4dClient() {
        super(BlockPos.ORIGIN, 0, new TextComponentString(""), new TextComponentString(""));
    }

    public void setPosition(BlockPos pos, int dimension) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.dimension = dimension;
        this.hasPosition = true;
    }

    @Override
    public boolean hasPosition() {
        return this.hasPosition;
    }

    @Override
    public boolean hasBiome() {
        return !this.biome.getFormattedText().isEmpty();
    }

}
