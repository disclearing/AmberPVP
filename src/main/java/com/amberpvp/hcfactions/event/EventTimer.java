package com.amberpvp.hcfactions.event;

import com.amberpvp.hcfactions.event.glowstone.GlowstoneEvent;
import com.amberpvp.hcfactions.event.schedule.ScheduleHandler;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.event.glowstone.GlowstoneEvent;
import com.amberpvp.hcfactions.event.schedule.ScheduleHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class EventTimer extends BukkitRunnable {

    private FactionsPlugin plugin;
    private HashMap<String, Long> glowstoneMountain;

    public EventTimer(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.glowstoneMountain = new HashMap<>();
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 20L, 20L);
    }

    @Override
    public void run() {

        ScheduleHandler.runSchedule();
        this.checkGlowstoneMountain();
    }

    private void checkGlowstoneMountain() {

        if (!this.glowstoneMountain.containsKey("glowstone")) {
            this.glowstoneMountain.put("glowstone", System.currentTimeMillis());
        }

        if (elapsed(this.glowstoneMountain.get("glowstone"), this.plugin.getMainConfig().getInt("GLOWSTONE.RESET_TIME"))) {
            this.glowstoneMountain.put("glowstone", System.currentTimeMillis());

            for(Event event : EventManager.getInstance().getEvents()) {
                if(event != null && event instanceof GlowstoneEvent) {
                    ((GlowstoneEvent) event).start();
                }
            }
        }
    }


    private boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }
}
