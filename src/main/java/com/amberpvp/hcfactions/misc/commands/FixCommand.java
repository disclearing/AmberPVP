package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 10/30/2018.
 */
public class FixCommand extends PluginCommand {

    @Command(name = "fix", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        if (!player.hasPermission("command.fix")) {
            player.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }
        player.getItemInHand().setDurability((short)0);
        player.sendMessage(ChatColor.GREEN + "You have just fixed this item!");
    }
}


