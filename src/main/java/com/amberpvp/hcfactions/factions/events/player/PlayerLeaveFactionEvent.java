package com.amberpvp.hcfactions.factions.events.player;

import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.factions.events.FactionEvent;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerLeaveFactionEvent extends FactionEvent {

    private PlayerFaction faction;
    private Player player;

    public PlayerLeaveFactionEvent(Player player, PlayerFaction faction) {
        this.player = player;
        this.faction = faction;
    }

}
