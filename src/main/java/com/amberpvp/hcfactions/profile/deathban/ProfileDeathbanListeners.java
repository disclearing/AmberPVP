package com.amberpvp.hcfactions.profile.deathban;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileDeathbanListeners implements Listener {

    private static FactionsPlugin main = FactionsPlugin.getInstance();

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Profile.getByPlayer(player);

        if (!main.isKitmapMode()) {
            if (ProfileDeathban.getDuration(player) > 0) {
                profile.setDeathban(new ProfileDeathban(ProfileDeathban.getDuration(player)));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline()) {
                            profile.setDeathban(new ProfileDeathban(ProfileDeathban.getDuration(player)));
                            player.kickPlayer(ProfileDeathban.KICK_MESSAGE.replace("%TIME%", profile.getDeathban().getTimeLeft()));
                        }
                    }
                }.runTaskLater(main, (long) 20);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (!main.isKitmapMode()) {
            if (profile.getDeathban() != null) {
                profile.setDeathban(new ProfileDeathban(ProfileDeathban.getDuration(player)));
                player.kickPlayer(ProfileDeathban.KICK_MESSAGE.replace("%TIME%", profile.getDeathban().getTimeLeft()));
            }
        }
    }

}
