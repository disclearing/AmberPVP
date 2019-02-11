package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CobbleCommand extends PluginCommand {

    @Command(name = "cobble", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Profile.getByPlayer(player);

        if (profile == null) {
            return;
        }

        profile.setCobble(!profile.isCobble());

        player.sendMessage(ChatColor.YELLOW + "You have " + (profile.isCobble() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + ChatColor.YELLOW + " picking up cobblestone whilst in Miner Class!");

    }


}
