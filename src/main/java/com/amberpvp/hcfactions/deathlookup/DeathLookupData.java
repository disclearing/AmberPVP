package com.amberpvp.hcfactions.deathlookup;

import com.amberpvp.hcfactions.profile.fight.ProfileFight;
import lombok.Getter;
import lombok.Setter;

public class DeathLookupData {

    @Getter @Setter private ProfileFight fight;
    @Getter @Setter private int page;
    @Getter @Setter private int index;

}
