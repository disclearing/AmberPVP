package com.amberpvp.hcfactions.profile.options;

import com.amberpvp.hcfactions.profile.options.item.ProfileOptionsItem;
import com.amberpvp.hcfactions.profile.options.item.ProfileOptionsItemState;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@Accessors(chain = true)
public class ProfileOptions {

    @Getter @Setter private boolean viewFoundDiamondMessages = true;
    @Getter @Setter private boolean viewDeathMessages = true;
    @Getter @Setter private boolean receivePublicMessages = true;

    public Inventory getInventory() {
        Inventory toReturn = Bukkit.createInventory(null, 9, "Options");

        toReturn.setItem(2  , ProfileOptionsItem.FOUND_DIAMOND_MESSAGES.getItem(viewFoundDiamondMessages ? ProfileOptionsItemState.ENABLED : ProfileOptionsItemState.DISABLED));
        toReturn.setItem(4, ProfileOptionsItem.DEATH_MESSAGES.getItem(viewDeathMessages ? ProfileOptionsItemState.ENABLED : ProfileOptionsItemState.DISABLED));
        toReturn.setItem(6, ProfileOptionsItem.PUBLIC_MESSAGES.getItem(receivePublicMessages ? ProfileOptionsItemState.ENABLED : ProfileOptionsItemState.DISABLED));

        return toReturn;
    }

}
