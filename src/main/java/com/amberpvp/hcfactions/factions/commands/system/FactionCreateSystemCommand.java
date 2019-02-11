package com.amberpvp.hcfactions.factions.commands.system;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.commands.FactionCommand;
import com.amberpvp.hcfactions.factions.type.SystemFaction;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionCreateSystemCommand extends FactionCommand {
    @Command(name = "f.createsystem", aliases = {"faction.createsystem", "factions.createsystem"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getPlayer();

        String[] args = command.getArgs();

        if (!sender.hasPermission("staff.sradmin")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.CREATE_SYSTEM"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }

        String name = sb.toString().trim();

        if (Faction.getByName(name) != null) {
            sender.sendMessage(langConfig.getString("ERROR.NAME_TAKEN"));
            return;
        }

        new SystemFaction(name, null);
        sender.sendMessage(langConfig.getString("SYSTEM_FACTION.CREATED").replace("%NAME%", name));
    }
}
