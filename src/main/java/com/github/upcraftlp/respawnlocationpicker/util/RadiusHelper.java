package com.github.upcraftlp.respawnlocationpicker.util;

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
        Vec3d posVec = new Vec3d(modPos).add(0.5, -yDiff, 0.5);

        AxisAlignedBB destBox = p.getEntityBoundingBox().offset(posVec.x - p.posX, -yDiff, posVec.z - p.posZ);
        while(!w.getCollisionBoxes(p, destBox).isEmpty() && posVec.y < w.provider.getActualHeight())
        {
            System.out.println("New Y: " + posVec.y);
            w.getCollisionBoxes(p, destBox).forEach(e -> System.out.println(e.toString()));
            posVec = posVec.add(0, 1, 0);
            destBox = destBox.offset(0, 1, 0);
        }
        return new BlockPos(posVec);
    }
}
