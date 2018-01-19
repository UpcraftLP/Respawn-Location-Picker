package com.github.upcraftlp.respawnlocationpicker.capability;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author UpcraftLP
 */
public class DefaultRespawnLocations implements IRespawnLocations {

    private final List<TargetPoint4d> RESPAWN_LOCATIONS = Lists.newArrayList();

    @Override
    public List<TargetPoint4d> getRespawnLocations(World world, int length) {
        if(world != null) this.RESPAWN_LOCATIONS.add(new TargetPoint4d(world.getSpawnPoint(), world.provider.getDimension(), "World Spawn"));
        return this.RESPAWN_LOCATIONS.subList(0, this.RESPAWN_LOCATIONS.size() < length ? this.RESPAWN_LOCATIONS.size() : length);
    }

    @Override
    public void addRespawnLocation(TargetPoint4d target) {
        if(!this.RESPAWN_LOCATIONS.contains(target)) this.RESPAWN_LOCATIONS.add(target);
    }

    public static class Storage implements Capability.IStorage<IRespawnLocations> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IRespawnLocations> capability, IRespawnLocations instance, EnumFacing side) {
            NBTTagList list = new NBTTagList();
            instance.getRespawnLocations(null, ModConfig.respawnLocations).forEach(targetPoint4d -> list.appendTag(targetPoint4d.serializeNBT()));
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
