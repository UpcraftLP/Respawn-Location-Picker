package com.github.upcraftlp.respawnlocationpicker.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author UpcraftLP
 */
public class TargetHelper {

    public static String getBiome(int dimension, BlockPos pos) {
        return getBiome(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension), pos);
    }

    public static String getBiome(World world, BlockPos pos) {
        return world.getBiome(pos).getBiomeName();
    }
}
