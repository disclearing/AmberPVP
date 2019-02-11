package com.amberpvp.hcfactions.factions.commands.officer;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.commands.FactionCommand;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FactionRenameCommand extends FactionCommand {
    @Command(name = "f.tag", aliases = {"faction.tag", "factions.tag", "factions.rename", "f.rename", "faction.rename"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.RENAME"));
            return;
        }

        Faction faction;
        if (command.getArgs().length >= 2) {
            String name = command.getArgs(0);
            Faction faction1 = PlayerFaction.getAnyByString(name);
            if (faction1 != null) {
                faction = faction1;
            } else {
                player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                return;
            }
        } else {
            faction = Profile.getByPlayer(player).getFaction();

            if (faction == null) {
                player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
                return;
            }

            PlayerFaction playerFaction = (PlayerFaction) faction;

            if (!playerFaction.getLeader().equals(player.getUniqueId()) && !playerFaction.getOfficers().contains(player.getUniqueId())) {
                player.sendMessage(langConfig.getString("ERROR.NOT_OFFICER_OR_LEADER"));
                return;
            }

        }

        StringBuilder sb = new StringBuilder();
        int start = 0;
        if (command.getArgs().length >= 2) {
            start = 1;
        }
        for (int i = start; i < command.getArgs().length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }

        String name;

        if (faction instanceof PlayerFaction) {
            name = sb.toString().trim().replace(" ", "");
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
        } else {
            name = sb.toString().trim();
        }

        Faction otherFaction = Faction.getByName(name);

        if (otherFaction != null) {
            if (otherFaction.equals(faction)) {
                if (otherFaction.getName().equals(name)) { //allow case changing but not exact duplicates. e.g "Faction" -> "factioN"
                    player.sendMessage(langConfig.getString("ERROR.NAME_TAKEN"));
                    return;
                }
            } else {
                player.sendMessage(langConfig.getString("ERROR.NAME_TAKEN"));
                return;
            }
        }

        if (faction instanceof PlayerFaction) {
            Bukkit.broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_RENAMED").replace("%OLD_NAME%", faction.getName()).replace("%NEW_NAME%", name).replace("%PLAYER%", player.getDisplayName()));
        } else {
            player.sendMessage(langConfig.getString("SYSTEM_FACTION.RENAMED").replace("%OLD_NAME%", faction.getName()).replace("%NEW_NAME%", name));
        }
        faction.setName(name);
    }
}
