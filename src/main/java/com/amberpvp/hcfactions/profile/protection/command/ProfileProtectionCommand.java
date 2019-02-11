package com.amberpvp.hcfactions.profile.protection.command;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionEnableCommand;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionLivesCommand;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionReviveCommand;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionTimeCommand;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionEnableCommand;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionLivesCommand;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionReviveCommand;
import com.amberpvp.hcfactions.profile.protection.command.subcommand.ProfileProtectionTimeCommand;
import com.amberpvp.hcfactions.profile.protection.life.command.ProfileProtectionLifeCommand;
import com.amberpvp.hcfactions.util.PluginCommand;
import com.amberpvp.hcfactions.util.command.Command;
import com.amberpvp.hcfactions.util.command.CommandArgs;

import java.util.List;

public class ProfileProtectionCommand extends PluginCommand {

    public static final List<String> HELP_MESSAGE = FactionsPlugin.getInstance().getLanguageConfig().getStringList("PVP_PROTECTION.COMMAND.HELP");

    public ProfileProtectionCommand() {
        new ProfileProtectionEnableCommand();
        new ProfileProtectionLivesCommand();
        new ProfileProtectionReviveCommand();
        new ProfileProtectionTimeCommand();
        new ProfileProtectionLifeCommand();
    }

    @Command(name = "pvp", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        command.getSender().sendMessage(getHelp());
    }

    public static String[] getHelp() {
        return HELP_MESSAGE.toArray(new String[HELP_MESSAGE.size()]);
    }
}
