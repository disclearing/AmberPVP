package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.DateUtil;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.DateUtil;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MedicReviveCommand extends PluginCommand {

    @Command(name = "medic.revive", aliases = {"medic.revive", "medic"}, inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (!player.hasPermission("staff.sradmin")) {
            player.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /medic <player>");
            return;
        }

        Profile profile = Profile.getByPlayer(player);

        if(profile == null) {
            return;
        }

        if(profile.getMedicCooldown() > System.currentTimeMillis()) {
            player.sendMessage(ChatColor.RED + "You can use this again in " + ChatColor.BOLD + this.getTimeLeft(profile));
            return;
        }

        Player target = Bukkit.getPlayer(StringUtils.join(args));
        Profile targetProfile;

        if (target != null) {
            targetProfile = Profile.getByPlayer(target);
        } else {
            targetProfile = Profile.getByName(StringUtils.join(args));
        }

        if (target == null) {
            player.sendMessage(ChatColor.RED + "No player with name '" + StringUtils.join(args) + "' found.");
            return;
        }

        if (targetProfile.getDeathban() == null) {
            player.sendMessage(langFile.getString("PVP_PROTECTION.COMMAND.REVIVE.NOT_DEATHBANNED").replace("%PLAYER%", targetProfile.getName()));
            return;
        }

        targetProfile.setDeathban(null);
        profile.setMedicCooldown(System.currentTimeMillis() + (60 * 60) * 1000);
        player.sendMessage(ChatColor.YELLOW + "You have used medic to revive " + targetProfile.getName());
        player.sendMessage(ChatColor.YELLOW + "Applied Cooldown: " + ChatColor.GOLD + " 1 hour");

        if (Bukkit.getPlayer(profile.getUuid()) == null) {

            new BukkitRunnable() {

                @Override
                public void run() {
                    targetProfile.save();
                    profile.save();
                    Profile.getProfilesMap().remove(targetProfile.getUuid());
                }
            }.runTaskAsynchronously(this.main);

        }
    }

    private String getTimeLeft(Profile profile) {

        if (profile.getMedicCooldown() == 0L) {
            return "None";
        }

        long delay = profile.getMedicCooldown();

        return DateUtil.convertTime((delay - System.currentTimeMillis()) / 1000L);
    }
}
