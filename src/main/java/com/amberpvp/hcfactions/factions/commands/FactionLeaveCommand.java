package com.amberpvp.hcfactions.factions.commands;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.events.player.PlayerLeaveFactionEvent;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionLeaveCommand extends FactionCommand {
    @Command(name = "f.leave", aliases = {"faction.leave", "factions.leave", "f.quit", "factions.quit", "faction.quit"}, inFactionOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        PlayerFaction playerFaction = profile.getFaction();

        if (playerFaction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("ERROR.CANT_LEAVE_WHEN_LEADER"));
            return;
        }


        player.sendMessage(langConfig.getString("FACTION_OTHER.LEFT").replace("%FACTION%", playerFaction.getName()));

        playerFaction.getMembers().remove(player.getUniqueId());
        playerFaction.getOfficers().remove(player.getUniqueId());
        profile.setFaction(null);

        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_LEFT").replace("%PLAYER%", player.getName()));

        Bukkit.getPluginManager().callEvent(new PlayerLeaveFactionEvent(player, playerFaction));
    }
}
