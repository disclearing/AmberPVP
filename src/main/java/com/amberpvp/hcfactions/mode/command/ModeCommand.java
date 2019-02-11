package com.amberpvp.hcfactions.mode.command;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.mode.command.subcommand.ModeCreateCommand;
import com.amberpvp.hcfactions.mode.command.subcommand.ModeDeleteCommand;
import com.amberpvp.hcfactions.mode.command.subcommand.ModeStartCommand;
import com.amberpvp.hcfactions.mode.command.subcommand.ModeStopCommand;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class ModeCommand extends PluginCommand {

    public ModeCommand() {
        new ModeCreateCommand();
        new ModeDeleteCommand();
        new ModeStartCommand();
        new ModeStopCommand();
    }

    @Command(name = "mode", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender player = command.getSender();

        if (!player.hasPermission("staff.sradmin")) {
            player.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        player.sendMessage(ChatColor.RED + "/mode create <name>");
        player.sendMessage(ChatColor.RED + "/mode delete <name>");
        player.sendMessage(ChatColor.RED + "/mode start <name>");
        player.sendMessage(ChatColor.RED + "/mode stop <name>");
    }
}
