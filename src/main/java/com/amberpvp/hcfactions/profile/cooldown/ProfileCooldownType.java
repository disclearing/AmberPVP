package com.amberpvp.hcfactions.profile.cooldown;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.FactionsPlugin;

public enum ProfileCooldownType {
    ENDER_PEARL,
    SPAWN_TAG,
    ARCHER_TAG,
    GOLDEN_APPLE,
    LOGOUT;

    private static FactionsPlugin main = FactionsPlugin.getInstance();

    public int getDuration() {
        return main.getMainConfig().getInt("COOLDOWN." + name());
    }

    public String getMessage() {
        return main.getLanguageConfig().getString("COOLDOWN." + name());
    }

}
