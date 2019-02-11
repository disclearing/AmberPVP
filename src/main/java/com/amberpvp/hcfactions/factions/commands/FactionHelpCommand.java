package com.amberpvp.hcfactions.factions.commands;

import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionHelpCommand extends FactionCommand {
    @Command(name = "f", aliases = {"faction", "factions"}, inGameOnly = false, description = "Base command for all faction-related functions.")
    public void onCommand(CommandArgs command) {
        String[] args = command.getArgs();

        List<List<String>> help = new ArrayList<>();
        for (String key : langConfig.getConfiguration().getConfigurationSection("FACTION_HELP").getKeys(false)) {
            help.add(langConfig.getStringList("FACTION_HELP." + key));
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                if (NumberUtils.isNumber(args[1])) {
                    int page = (int) Double.parseDouble(args[1]);
                    if (page > 0 && page <= help.size()) {
                        for (String msg : help.get(page - 1)) {
                            command.getSender().sendMessage(msg);
                        }
                        return;
                    }
                }
            }
        }

        for (String msg : help.get(0)) {
            command.getSender().sendMessage(msg);
        }
    }
}
