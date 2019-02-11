package com.amberpvp.hcfactions.factions.commands.admin;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.claims.Claim;
import com.amberpvp.hcfactions.factions.commands.FactionCommand;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class FactionDisbandAllCommand extends FactionCommand {

    @Command(name = "f.disbandall", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (!sender.hasPermission("staff.owner")) {
            sender.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (command.getArgs().length == 0 && sender instanceof ConsoleCommandSender) {
            for (Faction faction : Faction.getFactions()) {
                if (faction instanceof PlayerFaction) {
                    Set<Claim> claims = new HashSet<>(faction.getClaims());

                    for (Claim claim : claims) {
                        claim.remove();
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            main.getFactionsDatabase().getDatabase().getCollection("playerFactions").deleteOne(eq("uuid", faction.getUuid().toString()));
                        }
                    });

                    Faction.getFactions().remove(faction);
                }
            }

            sender.sendMessage(langConfig.getString("ADMIN.DISBAND_ALL"));
        }
    }
}
