package com.github.upcraftlp.respawnlocationpicker.command;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.api.capability.CapabilityProviderRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * @author UpcraftLP
 */
public class CommandAddRespawnPoint extends CommandBase {


    @Override
    public String getName() {
        return "addSpawnPoint";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.addSpawnPoint.usage"; //addSpawnPoint <name> [player]
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = getCommandSenderAsPlayer(sender);
        if(args.length < 1 || args.length > 2) throw new WrongUsageException(getUsage(sender));
        if(args.length == 2 && !sender.canUseCommand(server.getOpPermissionLevel(), this.getName())) {
            sender.sendMessage(new TextComponentTranslation("commands.addRespawnPoint.permissionOtherPlayers").setStyle(new Style().setColor(TextFormatting.RED)));
        }
        TargetPoint4d target = new TargetPoint4d(player.getPosition(), player.dimension, args[0], TargetHelper.getBiome(player.world, player.getPosition()));
        EntityPlayer targetPlayer = args.length == 2 ? getPlayer(server, sender, args[1]) : player;
        IRespawnLocations respawnLocations = targetPlayer.getCapability(CapabilityProviderRespawnLocations.CAPABILITY, null);
        if(respawnLocations.addRespawnLocation(target)) sender.sendMessage(new TextComponentTranslation("commands.addRespawnPoint.success", args[0]));
        else throw new CommandException("commands.addRespawnPoint.fail", args[0]);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return ModConfig.allowCustomSpawnpoints ? 0 : 2;
    }
}
