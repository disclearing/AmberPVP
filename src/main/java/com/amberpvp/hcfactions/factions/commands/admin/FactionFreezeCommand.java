package com.amberpvp.hcfactions.factions.commands.admin;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.commands.FactionCommand;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

public class FactionFreezeCommand extends FactionCommand {

    @Command(name = "faction.freeze", aliases = {"f.freeze", "factions.freeze", "f.setdtrregen","faction.setdtrregen","factions.setdtrregen"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (!sender.hasPermission("staff.triamod")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (command.getArgs().length == 2) {
            String name = command.getArgs(0);
            Faction faction = Faction.getByName(name);
            PlayerFaction playerFaction = null;

            if (faction instanceof PlayerFaction) {
                playerFaction = (PlayerFaction) faction;
            }

            if (faction == null || (!(faction instanceof PlayerFaction))) {
                playerFaction = PlayerFaction.getByPlayerName(name);

                if (playerFaction == null) {
                    sender.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                    return;
                }
            }

            try {
                playerFaction.freeze(getTime(command.getArgs(1)));
                sender.sendMessage(langConfig.getString("ADMIN.FROZEN").replace("%FACTION%", playerFaction.getName()).replace("%TIME%", playerFaction.getFormattedFreezeDuration()));
            } catch (Exception e) {
                sender.sendMessage(langConfig.getString("ERROR.INVALID_TIME"));
            }
        } else {
            sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.FREEZE"));
        }
    }

    private int getTime(String string) {
        int time = 0;
        if (string.contains("m")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 60;
            }
        } else if (string.contains("h")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 3600;
            }
        } else if (string.contains("d")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 86400;
            }
        } else if (string.contains("y")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 31536000;
            }
        } else {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr);
            } else {
                throw new NumberFormatException("Invalid number");
            }
        }
        return time;
    }

    private String strip(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
