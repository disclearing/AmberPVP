package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import me.joeleoli.nucleus.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReclaimCommand extends PluginCommand {

    @Command(name = "reclaim", aliases = {"rc", "claim"}, inGameOnly = true)
    public void onCommand(CommandArgs command) {
        this.runCommands(command.getPlayer());
    }

    private void runCommands(Player player) {

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if(profile == null) {
            return;
        }

        if(profile.isReclaim()) {
            player.sendMessage(this.main.getLanguageConfig().getString("RECLAIM.NONE"));
            return;
        }

        String rankName = PlayerData.getByUuid(player.getUniqueId()).getActiveRank().getName();

        if(!this.main.getConfig().contains("RECLAIM." + rankName)) {
            player.sendMessage(this.main.getLanguageConfig().getString("RECLAIM.NONE"));
            return;
        }

        for(String key : this.main.getConfig().getStringList("RECLAIM." + rankName)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), key.replace("%PLAYER%", player.getName()));
        }

        profile.setReclaim(true);

        new BukkitRunnable() {

            @Override
            public void run() {
                profile.save();
            }
        }.runTaskAsynchronously(this.main);

    }

}
