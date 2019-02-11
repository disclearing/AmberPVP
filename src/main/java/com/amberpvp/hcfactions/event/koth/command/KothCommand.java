package com.amberpvp.hcfactions.event.koth.command;

import com.amberpvp.hcfactions.event.schedule.Schedule;
import com.amberpvp.hcfactions.event.schedule.ScheduleHandler;
import com.amberpvp.hcfactions.event.Event;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import com.amberpvp.hcfactions.event.schedule.Schedule;
import com.amberpvp.hcfactions.event.schedule.ScheduleHandler;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.TimeZone;

public class KothCommand extends PluginCommand {

    @Command(name = "koth", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();


        for (Event possibleEvent : EventManager.getInstance().getEvents()) {

            if (possibleEvent instanceof KothEvent && possibleEvent.isActive()) {
                player.sendMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE  + possibleEvent.getName() + ChatColor.GOLD + " can be contested now.");
                return;
            }
        }

        Schedule kothEvent = ScheduleHandler.getNextEvent();
        FastDateFormat formatter = FastDateFormat.getInstance("EEEE, hh:mma", TimeZone.getDefault(), Locale.ENGLISH);

        player.sendMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.YELLOW  + kothEvent.getName() + ChatColor.GOLD + " can be captured at " + ChatColor.BLUE + kothEvent.getFormatDay() + ChatColor.GOLD + ".");
        player.sendMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.YELLOW  + "It is currently " + ChatColor.BLUE + formatter.format(System.currentTimeMillis()) + ChatColor.GOLD + ".");
        player.sendMessage(ChatColor.YELLOW + "Type '/koth schedule' to see more upcoming KOTHs.");

    }
}
