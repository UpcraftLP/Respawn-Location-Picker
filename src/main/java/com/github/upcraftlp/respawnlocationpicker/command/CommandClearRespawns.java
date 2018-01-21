package com.github.upcraftlp.respawnlocationpicker.command;

import com.github.upcraftlp.respawnlocationpicker.api.CapabilityProviderRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.IRespawnLocations;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author UpcraftLP
 */
public class CommandClearRespawns extends CommandBase {
    @Override
    public String getName() {
        return "clearSpawnPoints";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.clearSpawnPoints.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 1) throw new WrongUsageException(this.getUsage(sender));
        EntityPlayer player = getCommandSenderAsPlayer(sender);
        EntityPlayer target = args.length == 0 ? player : getPlayer(server, player, args[0]);
        if(target.hasCapability(CapabilityProviderRespawnLocations.CAPABILITY, null)) {
            IRespawnLocations respawnLocations = target.getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);
            int count = respawnLocations.clearRespawnLocations();
            sender.sendMessage(new TextComponentTranslation("commands.clearSpawnPoints.success", count));
        }
    }

}
