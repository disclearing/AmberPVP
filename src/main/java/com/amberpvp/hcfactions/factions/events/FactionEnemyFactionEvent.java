package com.amberpvp.hcfactions.factions.events;

import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import lombok.Getter;

@Getter
public class FactionEnemyFactionEvent extends FactionEvent {

    private PlayerFaction[] factions;

    public FactionEnemyFactionEvent(PlayerFaction[] factions) {
        this.factions = factions;
    }

}
