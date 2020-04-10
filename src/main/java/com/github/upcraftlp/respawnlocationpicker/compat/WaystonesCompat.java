package com.github.upcraftlp.respawnlocationpicker.compat;

import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaystonesCompat {

    public static String getWaystoneName(World world, BlockPos pos) {
        for(EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings()) {
            BlockPos toTest = pos.offset(facing);
            IBlockState blockState = world.getBlockState(toTest);
            if(blockState.getBlock() instanceof BlockWaystone) {
                EnumFacing direction = blockState.getValue(BlockWaystone.FACING);
                if(direction.getOpposite() == facing) {
                    TileEntity tileEntity = world.getTileEntity(toTest);
                    if(tileEntity instanceof TileWaystone) {
                        return ((TileWaystone) tileEntity).getWaystoneName();
                    }
                }
            }
        }
        return null;
    }
}
