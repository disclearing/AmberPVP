package com.amberpvp.hcfactions.factions.commands.system;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.commands.FactionCommand;
import com.amberpvp.hcfactions.factions.type.SystemFaction;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionColorCommand extends FactionCommand {
    @Command(name = "f.color", aliases = {"faction.color", "factions.color", "f.setcolor", "faction.setcolor", "factions.setcolor"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getPlayer();

        String[] args = command.getArgs();

        if (!sender.hasPermission("staff.sradmin")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.SET_COLOR"));
            return;
        }

        String name = args[0];

        SystemFaction systemFaction = SystemFaction.getByName(name);

        if (systemFaction == null) {
            sender.sendMessage(langConfig.getString("ERROR.SYSTEM_FACTION_NOT_FOUND").replace("%NAME%", name));
            return;
        }

        ChatColor color;
        try {
            color = ChatColor.valueOf(args[1].toUpperCase());
        } catch (Exception exception) {
            sender.sendMessage(langConfig.getString("ERROR.INVALID_COLOR"));
            return;
        }

        systemFaction.setColor(color);
        sender.sendMessage(langConfig.getString("SYSTEM_FACTION.SET_COLOR").replace("%COLOR%", color + "").replace("%COLOR_NAME%", color.name()).replace("%FACTION%", systemFaction.getName()));
    }
}
