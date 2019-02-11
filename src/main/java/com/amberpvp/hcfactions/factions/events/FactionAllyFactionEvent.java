package com.amberpvp.hcfactions.factions.events;

import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import lombok.Getter;

@Getter
public class FactionAllyFactionEvent extends FactionEvent {

    private PlayerFaction[] factions;

    public FactionAllyFactionEvent(PlayerFaction[] factions) {
        this.factions = factions;
    }

}
