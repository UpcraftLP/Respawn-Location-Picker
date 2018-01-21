package com.github.upcraftlp.respawnlocationpicker.api;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author UpcraftLP
 */
public class CapabilityProviderRespawnLocations implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IRespawnLocations.class)
    public static final Capability<IRespawnLocations> CAPABILITY = null;

    private final IRespawnLocations INSTANCE = CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY ? CAPABILITY.cast(this.INSTANCE) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, INSTANCE, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, INSTANCE, null, nbt);
    }
}
