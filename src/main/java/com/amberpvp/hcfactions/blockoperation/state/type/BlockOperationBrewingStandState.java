package com.amberpvp.hcfactions.blockoperation.state.type;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.blockoperation.state.BlockOperationState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BrewingStand;

public class BlockOperationBrewingStandState implements BlockOperationState {

    private static FactionsPlugin main = FactionsPlugin.getInstance();
    private static final String NAME = "BlockOperationBrewingStandState";

    @Getter @Setter private final BrewingStand brewingStand;

    public BlockOperationBrewingStandState(BrewingStand brewingStand) {
        this.brewingStand = brewingStand;
    }

    @Override
    public Location getLocation() {
        return brewingStand.getLocation();
    }

    @Override
    public int getIncrease() {
        return main.getMainConfig().getInt("BLOCK_MODIFIER.BREWING_STAND_BREW_INCREASE");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
