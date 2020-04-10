package com.github.upcraftlp.respawnlocationpicker.command;

import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.capability.IRespawnLocations;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

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
        return "commands.clearSpawnPoints.usage"; //clearSpawnPoints [player]
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 1) throw new WrongUsageException(this.getUsage(sender));
        EntityPlayer target = args.length == 0 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
        if(target.hasCapability(CapabilityRespawnLocations.CAPABILITY, null)) {
            IRespawnLocations respawnLocations = target.getCapability(CapabilityRespawnLocations.CAPABILITY, null);
            int count = respawnLocations.clearRespawnLocations();
            sender.sendMessage(new TextComponentTranslation("commands.clearSpawnPoints.success", count));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
