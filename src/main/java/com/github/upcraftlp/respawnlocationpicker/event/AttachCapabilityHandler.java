package com.github.upcraftlp.respawnlocationpicker.event;

import com.github.upcraftlp.respawnlocationpicker.Reference;
import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author UpcraftLP
 */
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class AttachCapabilityHandler {

    public static final ResourceLocation CAPABILITY = new ResourceLocation(Reference.MODID, "respawns");

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<EntityPlayerMP> event) {
        event.addCapability(CAPABILITY, new CapabilityHandler());
    }
}
