package com.amberpvp.hcfactions.factions.commands.system;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
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
public class FactionToggleDeathbanCommand extends FactionCommand {
    @Command(name = "f.toggledeathban", aliases = {"faction.toggledeathban", "factions.toggledeathban", "f.deathban", "faction.deathban", "factions.deathban"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getPlayer();

        String[] args = command.getArgs();

        if (!sender.hasPermission("staff.sradmin")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.TOGGLE_DEATHBAN"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }

        String name = sb.toString().trim();

        SystemFaction systemFaction = SystemFaction.getByName(name);

        if (systemFaction == null) {
            sender.sendMessage(langConfig.getString("ERROR.SYSTEM_FACTION_NOT_FOUND").replace("%NAME%", name));
            return;
        }

        systemFaction.setDeathban(!systemFaction.isDeathban());
        sender.sendMessage(langConfig.getString("SYSTEM_FACTION.TOGGLED_DEATHBAN").replace("%FACTION%", systemFaction.getName()).replace("%BOOLEAN%", systemFaction.isDeathban() + ""));
    }
}
