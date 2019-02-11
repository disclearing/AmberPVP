package com.amberpvp.hcfactions.profile.protection.command.subcommand;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.profile.protection.life.ProfileProtectionLifeType;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileProtectionLivesCommand extends PluginCommand {
    @Command(name = "pvp.lives", aliases = {"pvp.lifes"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        Profile profile;
        if (args.length == 0) {
            if (sender instanceof Player) {
                profile = Profile.getByPlayer((Player) sender);
            } else {
                sender.sendMessage(ChatColor.RED + "You're console dumbass.");
                return;
            }
        } else {
            Player player = Bukkit.getPlayer(StringUtils.join(args));
            if (player != null) {
                profile = Profile.getByPlayer(player);
            } else {
                profile = Profile.getByName(StringUtils.join(args));
            }
        }

        if (profile == null) {
            sender.sendMessage(ChatColor.RED + "No player with name '" + StringUtils.join(args) + "' found.");
            return;
        }

        for (String message : langFile.getStringList("PVP_PROTECTION.COMMAND.LIVES.MESSAGE")) {
            message = message.replace("%PLAYER%", profile.getName());

            for (ProfileProtectionLifeType type : ProfileProtectionLifeType.values()) {
                message = message.replace("%" + type.name() + "_LIVES%", profile.getLives().get(type) + "");
            }

            sender.sendMessage(message);
        }

        if (Bukkit.getPlayer(profile.getUuid()) == null) {
            Profile.getProfilesMap().remove(profile.getUuid());
        }
    }
}
