package com.amberpvp.hcfactions.misc.commands;

import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class XPShopCommand extends PluginCommand {


    @Command(name = "xpshop")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (!player.hasPermission("staff.sradmin")) {
            player.sendMessage(PluginCommand.NO_PERMISSION);
            return;
        } else {
            player.openInventory(xpShop());
        }

    }

    public Inventory xpShop() {

        Inventory xpShop = Bukkit.createInventory(null, 27, "XP Shop");

        return xpShop;
    }

}
