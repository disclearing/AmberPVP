package com.amberpvp.hcfactions.factions.commands;

import com.amberpvp.hcfactions.factions.events.player.PlayerCreateFactionEvent;
import com.amberpvp.hcfactions.mode.Mode;
import com.amberpvp.hcfactions.mode.ModeType;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.events.player.PlayerCreateFactionEvent;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.mode.Mode;
import com.amberpvp.hcfactions.mode.ModeType;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionCreateCommand extends FactionCommand {
    @Command(name = "f.create", aliases = {"faction.create", "factions.create"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        for(Mode mode : Mode.getModes()) {
            if(mode.getModeType() == ModeType.EOTW && mode.isActive()) {
                player.sendMessage(langConfig.getString("ERROR.EOTW_FACTION_CREATE"));
                return;
            }
        }

        if (args.length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.CREATE"));
            return;
        }

        Profile profile = Profile.getByPlayer(player);

        if (profile.getFaction() != null) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_IN_FACTION"));
            return;
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }

        String name = sb.toString().trim().replace(" ", "");

        if (name.length() < mainConfig.getInt("FACTION_NAME.MIN_CHARACTERS")) {
            player.sendMessage(langConfig.getString("ERROR.TAG_TOO_SHORT"));
            return;
        }

        if (name.length() > mainConfig.getInt("FACTION_NAME.MAX_CHARACTERS")) {
            player.sendMessage(langConfig.getString("ERROR.TAG_TOO_LONG"));
            return;
        }

        if (!(StringUtils.isAlphanumeric(name))) {
            player.sendMessage(langConfig.getString("ERROR.NOT_ALPHANUMERIC"));
            return;
        }

        for (String string : mainConfig.getStringList("FACTION_NAME.BLOCKED_NAMES")) {
            if (name.contains(string)) {
                player.sendMessage(langConfig.getString("ERROR.BLOCKED_NAME"));
                return;
            }
        }

        if (Faction.getByName(name) != null) {
            player.sendMessage(langConfig.getString("ERROR.NAME_TAKEN"));
            return;
        }

        PlayerFaction playerFaction = new PlayerFaction(name, player.getUniqueId(), null);
        profile.setFaction(playerFaction);

        Bukkit.broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_CREATED").replace("%PLAYER%", player.getDisplayName()).replace("%NAME%", name));

        Bukkit.getPluginManager().callEvent(new PlayerCreateFactionEvent(player, playerFaction));
    }
}
