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

public class ModeStartCommand extends PluginCommand {
    @Command(name = "mode.start")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /mode start <sotw/eotw>");
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
                mode.setActive(true);
                mode.setStartingTime(System.currentTimeMillis());
            }

            else if(mode.getModeType() == ModeType.EOTW) {
                mode.setActive(true);
                mode.setStartingTime(System.currentTimeMillis());

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "f setdtr * -50000");

                Event event = EventManager.getInstance().getByName("EOTW");

                if(event instanceof KothEvent) {
                    KothEvent kothEvent = (KothEvent) event;
                    kothEvent.start(900000);
                }
            }

            player.sendMessage(ChatColor.RED + "Mode named '" + name + "' successfully started.");

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /mode start <sotw/eotw>");
        }
    }
}
