package com.github.upcraftlp.respawnlocationpicker.util;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.Loader;

import java.util.Map;

/**
 * @author UpcraftLP
 */
public class CompatHelper {

    private static final Map<String, Boolean> MODS = Maps.newHashMap();

    public static final String
    WAYSTONES = "waystones";

    public static boolean isModLoaded(String modid) {
        if(!MODS.containsKey(modid)) {
            MODS.put(modid, Loader.isModLoaded(modid));
        }
        return MODS.get(modid);
    }
}
