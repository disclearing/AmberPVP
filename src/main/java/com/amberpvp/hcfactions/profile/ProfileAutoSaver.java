package com.amberpvp.hcfactions.profile;

import com.amberpvp.hcfactions.combatlogger.CombatLogger;
import com.amberpvp.hcfactions.event.Event;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.glowstone.GlowstoneEvent;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.mode.Mode;
import com.amberpvp.hcfactions.util.player.SimpleOfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;


public class ProfileAutoSaver extends BukkitRunnable {

    private JavaPlugin plugin;

    public ProfileAutoSaver(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        Faction.save();

        try {
            SimpleOfflinePlayer.save(plugin);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (Profile profile : Profile.getProfiles()) {
            profile.save();
        }

        for (Mode mode : Mode.getModes()) {
            mode.save();
        }

        for (CombatLogger logger : CombatLogger.getLoggers()) {
            logger.getEntity().remove();
        }

        for (Event event : EventManager.getInstance().getEvents()) {
            if (event instanceof KothEvent) {
                ((KothEvent) event).save();
            }
            else if (event instanceof GlowstoneEvent) {
                ((GlowstoneEvent) event).save();
            }
        }

    }

}
