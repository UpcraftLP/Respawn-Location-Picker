package com.github.upcraftlp.respawnlocationpicker.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * @author UpcraftLP
 */
@Immutable
public class TargetPoint4d implements INBTSerializable<NBTTagCompound> {

    private int x, y, z, dimension;
    private String name;
    public boolean isBed;

    public TargetPoint4d(int x, int y, int z, int dimension, String name, boolean forced) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.name = name;
        this.isBed = !forced;
    }

    public TargetPoint4d(NBTTagCompound nbt) {
        this(0, 0, 0, 0, "", false);
        this.deserializeNBT(nbt);
    }

    public TargetPoint4d(BlockPos pos, int dimension, String name, boolean forced) {
        this(pos.getX(), pos.getY(), pos.getZ(), dimension, name, forced);
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
        if(this == obj) return true;
        else if(obj instanceof TargetPoint4d) {
            TargetPoint4d object = (TargetPoint4d) obj;
            if(object.dimension != this.dimension) return false;
            if(object.getX() != this.x) return false;
            if(object.getY() != this.y) return false;
            if(object.getZ() != this.z) return false;
            if(!object.name.equals(this.name)) return false;
            return object.isBed == this.isBed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, dimension, name, isBed);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("X", this.x);
        nbt.setInteger("Y", this.y);
        nbt.setInteger("Z", this.z);
        nbt.setInteger("dimension", this.dimension);
        nbt.setString("name", this.name);
        nbt.setBoolean("bed", this.isBed);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.isBed = nbt.getBoolean("bed");
        this.name = nbt.getString("name");
        this.dimension = nbt.getInteger("dimension");
        this.x = nbt.getInteger("X");
        this.y = nbt.getInteger("Y");
        this.z = nbt.getInteger("Z");
    }
}
