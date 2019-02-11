package com.amberpvp.hcfactions.misc.commands.economy;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddBalanceCommand extends PluginCommand {

    @Command(name = "addbalance", aliases = {"addbal", "addmoney"})
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if(!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (!sender.hasPermission("staff.sradmin")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /addbalance <player> <amount>");
        } else if(args.length == 2) {

            Player toCheck = Bukkit.getPlayer(args[0]);

            if(!StringUtils.isNumeric(args[1])) {
                sender.sendMessage(ChatColor.RED + "Usage: /addbalance <player> <amount>");
                return;
            }

            int amount = Integer.parseInt(args[1]);


            if (toCheck == null) {
                sender.sendMessage(ChatColor.RED + "No player named '" + args[0] + "' found online.");
                return;
            }

            Profile toProfile = Profile.getByPlayer(toCheck);

            if(toProfile == null) {
                sender.sendMessage(ChatColor.RED + "No player named '" + args[0] + "' found online.");
                return;
            }


            toProfile.setBalance( (toProfile.getBalance() + amount) );
            player.sendMessage(ChatColor.YELLOW + "You added " + ChatColor.GOLD + "$" + amount + ChatColor.YELLOW + " to " + toCheck.getName());
            toCheck.sendMessage(ChatColor.YELLOW + "You received " + ChatColor.GOLD + "$" + amount + ChatColor.YELLOW);
        }

    }

}
