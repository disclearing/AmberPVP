package com.amberpvp.hcfactions.economysign;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.FactionsPlugin;

import java.util.List;

public enum EconomySignType {
    BUY,
    SELL;

    private static FactionsPlugin main = FactionsPlugin.getInstance();

    public List<String> getSignText() {
        return main.getLanguageConfig().getStringList("ECONOMY.SIGN." + name() + "_TEXT");
    }

}
