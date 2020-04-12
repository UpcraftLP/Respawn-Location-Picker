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
@Config.LangKey("config.respawnLocations.configtitle")
public class ModConfig {

    @Config.RangeInt(min = 0, max = 100)
    @Config.Comment("Maximum number of respawn locations a player can have.")
    @Config.LangKey("config.respawnLocations.locations")
    public static int respawnLocations = 10;

    @Config.Comment("Whether or not to include the global world spawn in the list of available respawn points.")
    @Config.LangKey("config.respawnLocations.worldspawn")
    public static boolean showWorldSpawn = true;

    @Config.Comment("Wether or not players have the option to respawn around their grave.")
    @Config.LangKey("config.respawnLocations.graves")
    public static boolean gravesEnabled = false;

    @Config.Comment("The radius in which the players respawn when picking the \"Last Death\" option.")
    @Config.LangKey("config.respawnLocations.range")
    @Config.RangeInt(min = 0, max = 500)
    public static int graveRange = 100;

    @Config.Comment("Wether or not players have the option to respawn next to other players.")
    @Config.LangKey("config.respawnLocations.players")
    public static boolean allowPlayerRespawn = false;

    @Config.Comment("Should players be allowed to add spawnpoints using /addspawnpoint.")
    @Config.LangKey("config.respawnLocations.custom")
    public static boolean allowCustomSpawnpoints = true;

    @Config.Comment("Will players get the coordinates, biome, both or none if hovering over a waypoint?")
    @Config.LangKey("config.respawnLocations.display")
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
