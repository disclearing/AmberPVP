package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.FactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class HelpCommand implements Listener {

    public HelpCommand() {
        Bukkit.getPluginManager().registerEvents(this, FactionsPlugin.getInstance());
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/help")) {

            if (!event.getMessage().equalsIgnoreCase("/help") && event.getMessage().toCharArray()[5] != ' ') {
                return;
            }

            event.getPlayer().sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getStringList("HELP").toArray(new String[FactionsPlugin.getInstance().getLanguageConfig().getStringList("HELP").size()]));
            event.setCancelled(true);
        }
    }

}
