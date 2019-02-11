package com.amberpvp.hcfactions.factions.commands.admin;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.commands.FactionCommand;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionAdminCommand extends FactionCommand {

    @Command(name = "f.admin", aliases = {"faction.admin", "factions.admin", "f.bypass", "faction.bypass", "factions.bypass"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        Player player;

        if (!sender.hasPermission("staff.admin")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }

        if (args.length >= 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < command.getArgs().length; i++) {
                sb.append(command.getArgs()[i]).append(" ");
            }

            String name = sb.toString().trim().replace(" ", "");

            player = Bukkit.getPlayer(name);
            if (player == null) {
                sender.sendMessage(langConfig.getString("ERROR.PLAYER_NOT_FOUND").replace("%PLAYER%", name));
                return;
            }
        } else {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.ADMIN"));
                return;
            }
        }

        Profile profile = Profile.getByPlayer(player);
        profile.setInAdminMode(!profile.isInAdminMode());

        if (player == sender) {
            player.sendMessage(langConfig.getString("ADMIN.ADMIN_MODE").replace("%BOOLEAN%", profile.isInAdminMode() + "").replace("%PLAYER%", player.getName()));
        } else {
            sender.sendMessage(langConfig.getString("ADMIN.ADMIN_MODE").replace("%BOOLEAN%", profile.isInAdminMode() + "").replace("%PLAYER%", player.getName()));
            player.sendMessage(langConfig.getString("ADMIN.ADMIN_MODE").replace("%BOOLEAN%", profile.isInAdminMode() + "").replace("%PLAYER%", player.getName()));
        }
    }
}
