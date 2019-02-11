package com.amberpvp.hcfactions.factions.events.player;

import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.events.FactionEvent;
import com.amberpvp.hcfactions.profile.teleport.ProfileTeleportType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
public class PlayerInitiateFactionTeleportEvent extends FactionEvent {

    private Faction faction;
    private Player player;
    private ProfileTeleportType teleportType;
    private Location initialLocation;
    private long init;
    @Setter private double time;
    @Setter private Location location;
    @Setter private boolean cancelled;

    public PlayerInitiateFactionTeleportEvent(Player player, Faction faction, ProfileTeleportType teleportType, double time, Location location, Location initialLocation) {
        this.player = player;
        this.faction = faction;
        this.teleportType = teleportType;
        this.time = time;
        this.init = System.currentTimeMillis();
        this.location = location;
        this.initialLocation = initialLocation;
    }


}
