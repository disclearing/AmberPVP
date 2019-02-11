package com.amberpvp.hcfactions.mode.command.subcommand;

import com.amberpvp.hcfactions.event.Event;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import com.amberpvp.hcfactions.mode.Mode;
import com.amberpvp.hcfactions.mode.ModeType;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.event.Event;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import com.amberpvp.hcfactions.mode.Mode;
import com.amberpvp.hcfactions.mode.ModeType;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ModeStopCommand extends PluginCommand {
    @Command(name = "mode.stop")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /mode stop <sotw/eotw>");
            return;
        }
        String name = StringUtils.join(args).toLowerCase();

        if(name.equalsIgnoreCase("sotw") || name.equalsIgnoreCase("eotw")) {
            Mode mode = Mode.getByName(name);

            if (mode == null) {
                player.sendMessage(ChatColor.RED + "A mode named '" + name + "' does not exist.");
                return;
            }

            if(mode.getModeType() == ModeType.SOTW) {
                mode.setActive(false);
                mode.setStartingTime(0L);
            }
            else if(mode.getModeType() == ModeType.EOTW) {

                mode.setActive(false);
                mode.setStartingTime(0L);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "f setdtr * 0");

                Event event = EventManager.getInstance().getByName("EOTW");

                if(event instanceof KothEvent) {
                    KothEvent kothEvent = (KothEvent) event;
                    if(kothEvent.isActive()) {
                        kothEvent.stop(true);
                    }
                }
            }

            player.sendMessage(ChatColor.RED + "Mode named '" + name + "' successfully stoped.");

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /mode stop <sotw/eotw>");
        }
    }
}
