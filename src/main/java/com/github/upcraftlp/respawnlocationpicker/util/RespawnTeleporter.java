package com.github.upcraftlp.respawnlocationpicker.util;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class RespawnTeleporter extends Teleporter {

    private final BlockPos targetPos;

    public RespawnTeleporter(WorldServer server, BlockPos targetPos) {
        super(server);
        this.targetPos = targetPos;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        entityIn.posX = this.targetPos.getX() + 0.5D;
        entityIn.posY = this.targetPos.getY();
        entityIn.posZ = this.targetPos.getZ() + 0.5D;
        while (!isClear(entityIn)) {
            entityIn.posY += 1.0D; //TODO better algorithm for finding a suitable spawn position
        }
        entityIn.setPositionAndUpdate(entityIn.posX, entityIn.posY, entityIn.posZ);
    }

    private static boolean isClear(Entity entity) {
        AxisAlignedBB box = entity.getEntityBoundingBox();
        int minX = MathHelper.floor(box.minX);
        int maxX = MathHelper.floor(box.maxX + 1.0D);
        int minY = MathHelper.floor(box.minY);
        int maxY = MathHelper.floor(box.maxY + 1.0D);
        int minZ = MathHelper.floor(box.minZ);
        int maxZ = MathHelper.floor(box.maxZ + 1.0D);

        if (minX < 0.0D)
            --minX;
        if (minY < 0.0D)
            --minY;
        if (minZ < 0.0D)
            --minZ;

        for (int x = minX; x < maxX; ++x)
            for (int z = minZ; z < maxZ; ++z) {
                for (int y = minY; y < maxY; ++y) {
                    IBlockState block = entity.world.getBlockState(new BlockPos(x, y, z));
                    final Material mat = block.getMaterial();
                    if (mat.isSolid() || mat.getPushReaction() == EnumPushReaction.BLOCK)
                        return false;
                }
            }
        return true;
    }
}
