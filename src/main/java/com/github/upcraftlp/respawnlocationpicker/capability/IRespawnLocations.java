package com.github.upcraftlp.respawnlocationpicker.capability;

import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * @author UpcraftLP
 */
public interface IRespawnLocations {

    List<TargetPoint4d> getRespawnLocations(int listLength);

    default void addRespawnLocation(BlockPos pos, int dimension, String name, String biome) {
        this.addRespawnLocation(new TargetPoint4d(pos, dimension, name, biome));
    }

    boolean addRespawnLocation(TargetPoint4d target);

    TargetPoint4d deleteRespawnLocation(int index);

    boolean deleteRespawnLocation(TargetPoint4d target);

    int getLocationCount();

    int clearRespawnLocations();

}
