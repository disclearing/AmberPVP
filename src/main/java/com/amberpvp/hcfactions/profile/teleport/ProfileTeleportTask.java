package com.amberpvp.hcfactions.profile.teleport;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.factions.events.player.PlayerInitiateFactionTeleportEvent;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.factions.events.player.PlayerInitiateFactionTeleportEvent;
import com.amberpvp.hcfactions.profile.Profile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileTeleportTask extends BukkitRunnable {

    @Getter private PlayerInitiateFactionTeleportEvent event;

    public ProfileTeleportTask(PlayerInitiateFactionTeleportEvent event) {
        this.event = event;

        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void run() {
        if (!(event.isCancelled())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(event.getLocation());
                }
            }.runTask(FactionsPlugin.getInstance());

            Profile.getByPlayer(event.getPlayer()).setTeleportWarmup(null);
        }
    }

}