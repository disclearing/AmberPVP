package com.amberpvp.hcfactions.factions.type;

import com.amberpvp.hcfactions.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class SystemFaction extends Faction {

    private ChatColor color = ChatColor.WHITE;
    private boolean deathban = true;

    public SystemFaction(String name, UUID uuid) {
        super(name, uuid);
    }

    public static Set<SystemFaction> getSystemFactions() {
        Set<SystemFaction> toReturn = new HashSet<>();

        for (Faction faction : getFactions()) {
            if (faction instanceof SystemFaction) {
                toReturn.add((SystemFaction) faction);
            }
        }

        return toReturn;
    }

    public static SystemFaction getByName(String name) {
        for (SystemFaction systemFaction : getSystemFactions()) {
            if (systemFaction.getName().replace(" ", "").equalsIgnoreCase(name)) {
                return systemFaction;
            }
        }

        return null;
    }

}
