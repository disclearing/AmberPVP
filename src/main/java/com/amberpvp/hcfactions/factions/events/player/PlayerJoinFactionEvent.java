package com.amberpvp.hcfactions.factions.events.player;

import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.factions.events.FactionEvent;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerJoinFactionEvent extends FactionEvent {

    private PlayerFaction faction;
    private Player player;

    public PlayerJoinFactionEvent(Player player, PlayerFaction faction) {
        this.player = player;
        this.faction = faction;
    }

}
