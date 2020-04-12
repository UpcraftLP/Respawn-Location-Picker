package com.github.upcraftlp.respawnlocationpicker.util;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RadiusHelper {

    public static BlockPos getRandomRadiusPoint(BlockPos target, int radius, int yDiff, World w, EntityPlayerMP p) {
        double ran = w.rand.nextDouble() * 2 * Math.PI;
        double modifier = radius * Math.sqrt(w.rand.nextDouble());
        double x = modifier * Math.cos(ran);
        double z = modifier * Math.sin(ran);

        BlockPos modPos = target.add(x, 0, z);
        Vec3d posVec = new Vec3d(modPos).add(0.5, 0, 0.5);

        AxisAlignedBB destBox = p.getEntityBoundingBox().offset(x, yDiff - posVec.y, z);
        while(!w.getCollisionBoxes(p, destBox).isEmpty() && posVec.y < w.provider.getActualHeight())
        {
            posVec = posVec.add(0, 1, 0);
            destBox = destBox.offset(0, 1, 0);
        }
        return new BlockPos(posVec);
    }
}
