package com.amberpvp.hcfactions.event.glowstone.procedure.command;

import com.amberpvp.hcfactions.event.glowstone.GlowstoneEvent;
import com.amberpvp.hcfactions.event.Event;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.glowstone.GlowstoneEvent;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class GlowstoneRemoveCommand extends PluginCommand {

    @Command(name = "glowstone.remove", aliases = {"glowstone.delete", "glowstoneremove", "removeglowstone"})
    public void onCommand(CommandArgs command) {


        CommandSender sender = command.getSender();

        if (!sender.hasPermission("staff.sradmin")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/glowstone remove <zone>");
            return;
        }

        Event event = EventManager.getInstance().getByName(args[0]);

        if (event == null || (!(event instanceof GlowstoneEvent))) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid Glowstone Mountain.");
            return;
        }


        GlowstoneEvent glowstoneEvent = (GlowstoneEvent) event;
        sender.sendMessage(ChatColor.YELLOW + "(" + glowstoneEvent.getName() + ") Glowstone Mountain has been removed.");
        glowstoneEvent.remove();

    }
}
