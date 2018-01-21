package com.github.upcraftlp.respawnlocationpicker.capability;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.util.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author UpcraftLP
 */
public class DefaultRespawnLocations implements IRespawnLocations {

    private final List<TargetPoint4d> RESPAWN_LOCATIONS = Lists.newArrayList();

    @Override
    public List<TargetPoint4d> getRespawnLocations(int maxLength) {
        if(this.RESPAWN_LOCATIONS.isEmpty()) return Lists.newArrayList();
        if(this.RESPAWN_LOCATIONS.size() < maxLength) maxLength = this.RESPAWN_LOCATIONS.size();
        return this.RESPAWN_LOCATIONS.subList(0, maxLength);
    }

    @Override
    public boolean addRespawnLocation(TargetPoint4d target) {
        if(this.RESPAWN_LOCATIONS.contains(target)) return false;
        this.RESPAWN_LOCATIONS.add(0, target);
        return true;
    }

    @Override
    public TargetPoint4d deleteRespawnLocation(int index) {
        return this.RESPAWN_LOCATIONS.remove(index);
    }

    @Override
    public boolean deleteRespawnLocation(TargetPoint4d target) {
        return this.RESPAWN_LOCATIONS.remove(target);
    }

    @Override
    public int getLocationCount() {
        return this.RESPAWN_LOCATIONS.size();
    }

    @Override
    public int clearRespawnLocations() {
        int count = RESPAWN_LOCATIONS.size();
        RESPAWN_LOCATIONS.clear();
        return count;
    }

    public static class Storage implements Capability.IStorage<IRespawnLocations> {

        @Nullable
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
    }


}
