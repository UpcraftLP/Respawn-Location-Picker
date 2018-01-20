package com.github.upcraftlp.respawnlocationpicker;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.github.upcraftlp.respawnlocationpicker.Reference.MODID;

/**
 * @author UpcraftLP
 */
@Config(modid = MODID, name = "craftdevmods/" + MODID) //--> /config/craftdevmods/respawnlocationpicker.cfg
public class ModConfig {

    @Config.RangeInt(min = 0, max = 100)
    @Config.Comment("maximum number of respawn locations a player can have")
    public static int respawnLocations = 10;

    @Config.Comment("whether or not to include the global world spawn in the list of available respawn points")
    public static boolean showWorldSpawn = true;

    @Config.Comment("should players be allowed to add spawnpoints using /addspawnpoint")
    public static boolean allowCustomSpawnpoints = true;

    @Mod.EventBusSubscriber(modid = MODID)
    public static class Handler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(Reference.MODID)) {
                ConfigManager.load(Reference.MODID, Config.Type.INSTANCE);
            }
        }
    }
    
}
