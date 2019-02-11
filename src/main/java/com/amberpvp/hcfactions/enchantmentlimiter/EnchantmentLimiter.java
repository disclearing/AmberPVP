package com.amberpvp.hcfactions.enchantmentlimiter;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.FactionsPlugin;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentLimiter {

    private static final EnchantmentLimiter instance = new EnchantmentLimiter();
    private static FactionsPlugin main = FactionsPlugin.getInstance();

    public int getEnchantmentLimit(Enchantment enchantment) {
        if (main.getMainConfig().getConfiguration().contains("ENCHANTMENT_LIMITER." + enchantment.getName())) {
            return main.getMainConfig().getInt("ENCHANTMENT_LIMITER." + enchantment.getName());
        }

        return enchantment.getMaxLevel();
    }

    public static EnchantmentLimiter getInstance() {
        return instance;
    }

}
