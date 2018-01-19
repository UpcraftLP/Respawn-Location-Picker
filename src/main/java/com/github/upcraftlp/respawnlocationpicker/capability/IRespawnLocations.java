package com.github.upcraftlp.respawnlocationpicker.capability;

import com.github.upcraftlp.respawnlocationpicker.util.TargetPoint4d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author UpcraftLP
 */
public interface IRespawnLocations {

    List<TargetPoint4d> getRespawnLocations(@Nullable World world, int listLength);

    default void addRespawnLocation(BlockPos pos, int dimension, String name) {
        this.addRespawnLocation(new TargetPoint4d(pos, dimension, name));
    }

    void addRespawnLocation(TargetPoint4d target);

}
