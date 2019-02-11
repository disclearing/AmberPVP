package com.amberpvp.hcfactions.factions.claims;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.profile.Profile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class ClaimProfile {

    private Player player;
    private Profile profile;
    private Faction faction;
    private boolean resetClicked;
    private ClaimPillar[] pillars;

    public ClaimProfile(Player player, Faction faction) {
        this.player = player;
        this.faction = faction;
        this.pillars = new ClaimPillar[2];
        this.profile = Profile.getByPlayer(player);
        this.profile.setClaimProfile(this);
    }

    public void removePillars() {
        for (ClaimPillar claimPillar : pillars) {
            if (claimPillar != null) {
                claimPillar.remove();
            }
        }

        this.pillars = new ClaimPillar[2];
    }

}
