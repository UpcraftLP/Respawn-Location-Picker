package com.github.upcraftlp.respawnlocationpicker.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.concurrent.Immutable;

/**
 * @author UpcraftLP
 */
@Immutable
public class TargetPoint4d implements INBTSerializable<NBTTagCompound> {

    private int x, y, z, dimension;
    private String name;

    public TargetPoint4d(int x, int y, int z, int dimension, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.name = name;
    }

    public TargetPoint4d(NBTTagCompound nbt) {
        this(0, 0, 0, 0, "");
        this.deserializeNBT(nbt);
    }

    public TargetPoint4d(BlockPos pos, int dimension, String name) {
        this(pos.getX(), pos.getY(), pos.getZ(), dimension, name);
    }

    public BlockPos getPosition() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getDimension() {
        return dimension;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) return true;
        else if(obj instanceof TargetPoint4d) {
            TargetPoint4d object = (TargetPoint4d) obj;
            return object.dimension == this.dimension && object.getPosition().equals(this.getPosition()) && object.name.equals(this.name);
        }
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("X", this.x);
        nbt.setInteger("Y", this.y);
        nbt.setInteger("Z", this.z);
        nbt.setInteger("dimension", this.dimension);
        nbt.setString("name", this.name);
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.name = nbt.getString("name");
        this.dimension = nbt.getInteger("dimension");
        this.x = nbt.getInteger("X");
        this.y = nbt.getInteger("Y");
        this.z = nbt.getInteger("Z");
    }
}
