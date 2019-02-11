package com.amberpvp.hcfactions.factions.commands;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionMessageCommand extends FactionCommand {
    @Command(name = "f.msg", aliases = {"faction.msg", "factions.msg", "f.message", "faction.message", "factions.message"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.MESSAGE"));
            return;
        }

        Profile profile = Profile.getByPlayer(player);

        PlayerFaction playerFaction = profile.getFaction();

        if (playerFaction == null) {
            player.sendMessage(langConfig.getString("ERROR.MUST_BE_IN_FACTION_FOR_CHAT_TYPE"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.getArgs().length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }
        String message = sb.toString().trim();

        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_FACTION_CHAT").replace("%PLAYER%", player.getName()).replace("%MESSAGE%", message).replace("%FACTION%", playerFaction.getName()));
    }
}
