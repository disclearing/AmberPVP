package com.amberpvp.hcfactions.command;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CloneInventoryCommand extends PluginCommand {

    @Command(name = "cloneinventory", aliases = {"cloneinv", "copyinv", "copyinventory", "cpfrom"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (!player.hasPermission("staff.sradmin")) {
            player.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        Player toClone;
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/" + command.getLabel() + " <player>");
            return;
        } else {
            toClone = Bukkit.getPlayer(StringUtils.join(args));
            if (toClone == null) {
                player.sendMessage(ChatColor.RED + "No player named '" + StringUtils.join(args) + "' found.");
                return;
            }
        }

        player.getInventory().setContents(toClone.getInventory().getContents());
        player.getInventory().setArmorContents(toClone.getInventory().getArmorContents());
        player.sendMessage(ChatColor.RED + "Inventory successfully cloned");
    }
}
