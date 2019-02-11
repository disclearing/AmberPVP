package com.amberpvp.hcfactions.profile.fight.command;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.entity.Player;

public class KillStreakCommand extends PluginCommand {
    @Command(name = "killstreak", aliases = "ks")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getStringList("KILL_STREAK.HELP_MENU").toArray(new String[FactionsPlugin.getInstance().getLanguageConfig().getStringList("KILL_STREAK.HELP_MENU").size()]));
    }
}
