package com.amberpvp.hcfactions.factions.commands;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.files.ConfigFile;

public class FactionCommand {

    public FactionsPlugin main = FactionsPlugin.getInstance();
    public ConfigFile langConfig = main.getLanguageConfig();
    public ConfigFile mainConfig = main.getMainConfig();

    public FactionCommand() {
        main.getFramework().registerCommands(this);
    }

}
