package com.github.upcraftlp.respawnlocationpicker.command;

import com.github.upcraftlp.respawnlocationpicker.ModConfig;
import com.github.upcraftlp.respawnlocationpicker.capability.CapabilityRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.capability.IRespawnLocations;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetHelper;
import com.github.upcraftlp.respawnlocationpicker.api.util.TargetPoint4d;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

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
        return "commands.addSpawnPoint.usage"; //addSpawnPoint <name> [player] [x y z] [dimension]
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length >= 1 && (args.length <= 2 || args.length >= 5) && args.length <= 6) {
            if(args.length == 2 && !sender.canUseCommand(server.getOpPermissionLevel(), this.getName())) {
                sender.sendMessage(new TextComponentTranslation("commands.addSpawnPoint.permissionOtherPlayers").setStyle(new Style().setColor(TextFormatting.RED)));
            }
            BlockPos pos = sender.getPosition();
            EntityPlayer targetPlayer;
            int dim = sender.getEntityWorld().provider.getDimension();
            if(args.length > 1) {
                targetPlayer = getPlayer(server, sender, args[1]);
                if(args.length > 2) {
                    pos = parseBlockPos(sender, args, 2, false);
                }
                if(args.length == 6) dim = parseInt(args[5]);
            }
            else {
                targetPlayer = getCommandSenderAsPlayer(sender);
            }
            TargetPoint4d target = new TargetPoint4d(pos, dim, new TextComponentString(args[0]), new TextComponentString(TargetHelper.getBiome(dim, pos)));

            IRespawnLocations respawnLocations = targetPlayer.getCapability(CapabilityRespawnLocations.CAPABILITY, null);
            if(respawnLocations.addRespawnLocation(target)) sender.sendMessage(new TextComponentTranslation("commands.addSpawnPoint.success", args[0]));
            else throw new CommandException("commands.addSpawnPoint.fail", args[0]);
        }
        else throw new WrongUsageException(getUsage(sender));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return ModConfig.allowCustomSpawnpoints ? 0 : 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 2) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        if(args.length >= 3 && args.length <= 5) return getTabCompletionCoordinate(args, 2, null);
        if(args.length == 6) return Lists.newArrayList(Integer.toString(sender.getEntityWorld().provider.getDimension()));
        return Collections.emptyList();
    }
}
