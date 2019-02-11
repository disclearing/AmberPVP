package com.amberpvp.hcfactions.chat;

import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.profile.ProfileChatType;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.profile.ProfileChatType;
import me.joeleoli.nucleus.chat.ChatFormat;
import me.joeleoli.nucleus.player.PlayerData;
import me.joeleoli.nucleus.util.CC;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FactionsChatFormat implements ChatFormat {

    @Override
    public String format(Player sender, Player receiver, String message) {
        final PlayerData playerData = PlayerData.getByUuid(sender.getUniqueId());
        final Profile profile = Profile.getByUuid(sender.getUniqueId());
        final PlayerFaction playerFaction = PlayerFaction.getByPlayer(sender);
        ProfileChatType chatType = playerFaction == null ? ProfileChatType.PUBLIC : profile.getChatType();

        if (chatType == ProfileChatType.PUBLIC) {
            return (playerFaction != null ? CC.GOLD + "[" + CC.YELLOW + (playerFaction == profile.getFaction() ? ChatColor.GREEN + playerFaction.getName(): ChatColor.RED + playerFaction.getName()) + CC.GOLD + "]" : "") + playerData.getActiveRank().getPrefix() + (playerData.getActivePrefix() ==  null ? "":playerData.getActivePrefix().getPrefix()) + sender.getDisplayName() + CC.GRAY + ": " + CC.WHITE + message;
        } else {
            if (!sender.getUniqueId().equals(receiver.getUniqueId())) {
                if (chatType == ProfileChatType.ALLY) {
                    final PlayerFaction targetFaction = PlayerFaction.getByPlayer(receiver);

                    if (targetFaction == null) {
                        return null;
                    }

                    if (!playerFaction.getAllies().contains(targetFaction)) {
                        return null;
                    }
                } else if (chatType == ProfileChatType.FACTION) {
                    if (!playerFaction.getMembers().contains(receiver.getUniqueId())) {
                        return null;
                    }
                }
            }

            return chatType == ProfileChatType.ALLY ? CC.YELLOW + "(" + CC.BLUE + "Ally" + CC.YELLOW + ") " + sender.getName() + ": " + CC.BLUE + message:CC.YELLOW + "(" + CC.DARK_GREEN + "Faction" + CC.YELLOW + ") " + sender.getName() + ": " + CC.DARK_GREEN + message;
        }
    }

}
