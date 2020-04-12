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
@SuppressWarnings("CanBeFinal")
@Config(modid = MODID, name = "craftdevmods/" + MODID) //--> /config/craftdevmods/respawnlocationpicker.cfg
public class ModConfig {

    @Config.RangeInt(min = 0, max = 100)
    @Config.Comment("maximum number of respawn locations a player can have")
    public static int respawnLocations = 10;

    @Config.Comment("whether or not to include the global world spawn in the list of available respawn points")
    public static boolean showWorldSpawn = true;

    @Config.Comment("Wether or not players have the option to respawn around their grave.")
    public static boolean gravesEnabled = false;

    @Config.Comment("The radius in which the players respawn when picking the \"Last Death\" option.")
    @Config.RangeInt(min = 100, max = 500)
    public static int graveRange = 100;

    @Config.Comment("Wether or not players have the option to respawn next to other players.")
    public static boolean allowPlayerRespawn = false;

    @Config.Comment("should players be allowed to add spawnpoints using /addspawnpoint")
    public static boolean allowCustomSpawnpoints = true;

    @Config.Comment("will players get the coordinates, biome, both or none if hovering over a waypoint?")
    public static SpawnPointDisplayMode displayMode = SpawnPointDisplayMode.BOTH;

    @Mod.EventBusSubscriber(modid = MODID)
    public static class Handler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(Reference.MODID)) {
                ConfigManager.load(Reference.MODID, Config.Type.INSTANCE);
            }
        }
    }

    public enum SpawnPointDisplayMode {
        NONE(false, false),
        BIOME(false,  true),
        COORDINATES(true, false),
        BOTH(true, true);

        private boolean posIncluded, biomeIncluded;

        SpawnPointDisplayMode(boolean posIncluded, boolean biomeIncluded) {
            this.posIncluded = posIncluded;
            this.biomeIncluded = biomeIncluded;
        }

        public boolean biomeIncluded() {
            return this.biomeIncluded;
        }

        public boolean posIncluded() {
            return this.posIncluded;
        }
    }
    
}
