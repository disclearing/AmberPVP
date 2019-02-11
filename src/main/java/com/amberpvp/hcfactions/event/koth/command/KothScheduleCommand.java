package com.amberpvp.hcfactions.event.koth.command;

import com.amberpvp.hcfactions.event.schedule.Schedule;
import com.amberpvp.hcfactions.event.schedule.ScheduleHandler;
import com.amberpvp.hcfactions.event.schedule.Schedule;
import com.amberpvp.hcfactions.event.schedule.ScheduleHandler;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.TimeZone;

public class KothScheduleCommand extends PluginCommand {

    @Command(name = "koth.schedule", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        FastDateFormat formatter = FastDateFormat.getInstance("EEEE, hh:mma", TimeZone.getDefault(), Locale.ENGLISH);

        for(Schedule schedule : ScheduleHandler.schedules) {
            player.sendMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.YELLOW  + schedule.getName() + ChatColor.GOLD + " can be captured at " + ChatColor.BLUE + schedule.getFormatDay() + ChatColor.GOLD + ".");
        }

        player.sendMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.YELLOW  + "It is currently " + ChatColor.BLUE + formatter.format(System.currentTimeMillis()) + ChatColor.GOLD + ".");

    }
}
