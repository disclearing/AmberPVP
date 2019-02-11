package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.entity.Player;

public class StackCommand extends PluginCommand {

    @Command(name = "stack", aliases = {"more"}, inGameOnly = true)
    public void onCommand(CommandArgs command) {

        if (!command.getSender().hasPermission("staff.sradmin")) {
            command.getSender().sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        Player player = command.getPlayer();
        player.getInventory().getItemInHand().setAmount(64);
    }


}
