package com.amberpvp.hcfactions.util;

import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.util.command.CommandArgs;
import me.joeleoli.nucleus.util.CC;

public abstract class PluginCommand {

    public FactionsPlugin main = FactionsPlugin.getInstance();
    public ConfigFile configFile = main.getMainConfig();
    public ConfigFile langFile = main.getLanguageConfig();
    public ConfigFile scoreboardFile = main.getScoreboardConfig();
    public static String NO_PERMISSION = CC.RED + "No permission.";

    public PluginCommand() {
        main.getFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
