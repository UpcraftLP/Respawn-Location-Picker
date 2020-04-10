package com.github.upcraftlp.respawnlocationpicker.capability;

import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.google.common.collect.Lists;

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

}
