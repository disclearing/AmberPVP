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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionManageCommand extends FactionCommand {

    @Command(name = "f.manage", aliases = {"faction.manage", "factions.manage"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (!sender.hasPermission("staff.trialmod")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (command.getArgs().length >= 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < command.getArgs().length; i++) {
                sb.append(command.getArgs()[i]).append(" ");
            }
            String name = sb.toString().trim().replace(" ", "");

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

            playerFaction.setFreezeInformation(null);
        } else {
            sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.MANAGE"));
        }
    }
}
