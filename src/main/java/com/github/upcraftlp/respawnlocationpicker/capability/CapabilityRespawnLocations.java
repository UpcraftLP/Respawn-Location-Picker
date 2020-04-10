package com.github.upcraftlp.respawnlocationpicker.capability;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author UpcraftLP
 */
public class CapabilityRespawnLocations {

    @CapabilityInject(IRespawnLocations.class)
    public static final Capability<IRespawnLocations> CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IRespawnLocations.class, new Capability.IStorage<IRespawnLocations>() {
            @Override
            public NBTBase writeNBT(Capability<IRespawnLocations> capability, IRespawnLocations instance, EnumFacing side) {
                final NBTTagList list = new NBTTagList();
                instance.getRespawnLocations(ModConfig.respawnLocations).forEach(targetPoint4d -> list.appendTag(targetPoint4d.serializeNBT()));
                return list;
            }

            @Override
            public void readNBT(Capability<IRespawnLocations> capability, IRespawnLocations instance, EnumFacing side, NBTBase nbt) {
                NBTTagList list = (NBTTagList) nbt;
                for(int i = 0; i < list.tagCount(); i++) {
                    instance.addRespawnLocation(new TargetPoint4d(list.getCompoundTagAt(i)));
                }
            }
        }, DefaultRespawnLocations::new);
    }
}
