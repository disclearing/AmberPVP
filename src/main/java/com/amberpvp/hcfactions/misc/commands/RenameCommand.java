package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand extends PluginCommand {

    @Command(name = "rename")
    public void onCommand(final CommandArgs command) {
        final Player player = (Player)command.getSender();

        if (!player.hasPermission("command.rename")) {
            player.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        }

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "You have nothing in your hand...");
            return;
        }
        if (command.getArgs().length < 1) {
            player.sendMessage(ChatColor.RED + "You did not supply a name.");
            return;
        }

        String name = ChatColor.translateAlternateColorCodes('&', StringUtils.join(command.getArgs(), ' ', 0, command.getArgs().length));

        final ItemStack itemStack = player.getItemInHand().clone();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);

        player.getInventory().setItemInHand(itemStack);
        player.updateInventory();
        player.sendMessage(ChatColor.GOLD + "Renamed item to " + name);
    }
}
