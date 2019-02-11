package com.amberpvp.hcfactions.combatlogger.commands;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldown;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldownType;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.entity.Player;

public class CombatLoggerCommand extends PluginCommand {
    @Command(name = "logout", aliases = "combatlog")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getCooldownByType(ProfileCooldownType.LOGOUT) != null) {
            player.sendMessage(langFile.getString("COMBAT_LOGGER.LOGOUT_ALREADY"));
            return;
        }

        player.sendMessage(langFile.getString("COMBAT_LOGGER.LOGOUT").replace("%TIME%", main.getMainConfig().getInt("COMBAT_LOGGER.LOGOUT_TIME") + ""));
        profile.getCooldowns().add(new ProfileCooldown(ProfileCooldownType.LOGOUT, main.getMainConfig().getInt("COMBAT_LOGGER.LOGOUT_TIME")));
        profile.setLogoutLocation(player.getLocation());
    }
}
