package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.factions.claims.Claim;
import com.amberpvp.hcfactions.factions.type.SystemFaction;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChestCommand extends PluginCommand {

    @Command(name = "chest", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Profile.getByPlayer(player);

        if (profile == null) {
            return;
        }

        if (!FactionsPlugin.getInstance().isKitmapMode()) {
            player.sendMessage(ChatColor.RED + "You can only do this on a kitmap.");
            return;
        }

        Claim claimAt = Claim.getProminentClaimAt(player.getLocation());

        assert claimAt != null;
        SystemFaction systemFaction = (SystemFaction) claimAt.getFaction();

        if (systemFaction.isDeathban()) {
            player.sendMessage(ChatColor.RED + "You can only do this in spawn.");
        } else {
            player.openInventory(player.getEnderChest());
        }
    }
}

