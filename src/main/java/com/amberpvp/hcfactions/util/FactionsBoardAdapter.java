package com.amberpvp.hcfactions.util;

import com.amberpvp.hcfactions.event.Event;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.mode.Mode;
import com.amberpvp.hcfactions.mode.ModeType;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldown;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldownType;
import com.amberpvp.hcfactions.profile.teleport.ProfileTeleportTask;
import com.amberpvp.hcfactions.profile.teleport.ProfileTeleportType;
import me.joeleoli.nucleus.board.Board;
import me.joeleoli.nucleus.board.BoardAdapter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FactionsBoardAdapter implements BoardAdapter {

    public static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    private FactionsPlugin main;
    private ConfigFile configFile;

    public FactionsBoardAdapter(FactionsPlugin main) {
        this.main = main;
        this.configFile = main.getScoreboardConfig();
    }

    @Override
    public String getTitle(Player player) {
        return configFile.getString("TITLE");
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    @Override
    public void onScoreboardCreate(Player player, Scoreboard scoreboard) {

    }

    @Override
    public void preLoop() {
    }


    @Override
    public List<String> getScoreboard(Player player, Board board) {
        List<String> toReturn = new ArrayList<>();
        Profile profile = Profile.getByPlayer(player);

        for (String line : configFile.getStringList("LINES")) {

            if (line.contains("%HOME%")) {
                ProfileTeleportTask teleportTask = profile.getTeleportWarmup();

                if (teleportTask != null && teleportTask.getEvent().getTeleportType() == ProfileTeleportType.HOME_TELEPORT) {
                    toReturn.add(line.replace("%HOME%", SECONDS_FORMATTER.format(((teleportTask.getEvent().getInit() + (teleportTask.getEvent().getTime() * 1000) + 50) - System.currentTimeMillis()) / 1000)));
                }

                continue;
            }

            if (line.contains("%STUCK%")) {
                ProfileTeleportTask teleportTask = profile.getTeleportWarmup();

                if (teleportTask != null && teleportTask.getEvent().getTeleportType() == ProfileTeleportType.STUCK_TELEPORT) {
                    toReturn.add(line.replace("%STUCK%", DurationFormatUtils.formatDuration((long) ((teleportTask.getEvent().getInit() + (teleportTask.getEvent().getTime() * 1000) + 500) - System.currentTimeMillis()), "mm:ss")));
                }

                continue;
            }

            if (line.contains("%KOTH%")) {
                for (Event event : EventManager.getInstance().getEvents()) {
                    if (event instanceof KothEvent && event.isActive()) {
                        toReturn.addAll(event.getScoreboardText());
                    }
                }

                continue;
            }

            if (line.contains("%SOTW%")) {
                for (Mode mode : Mode.getModes()) {
                    if (mode.getModeType() == ModeType.SOTW && mode.isActive()) {
                        toReturn.addAll(mode.getScoreboardText());
                        break;
                    }
                }

                continue;
            }
          /*  if (line.contains("%VANISH%")) {
                if (Basic.getPlugin().getModModeManager().isInStaffMode(player))
                    toReturn.add(line.replace("%VANISH%", ChatColor.YELLOW + "Vanish: " + (Basic.getPlugin().getVanishManager().isVanished(player.getUniqueId()) ? ChatColor.GREEN + "Vanished" : ChatColor.RED + "Visible")));
                continue;
            }*/


            if (line.contains("%KILLS%")) {
                toReturn.add(line.replace("%KILLS%", profile.getKillCount() + ""));
                continue;
            }

            if (line.contains("%DEATHS%")) {
                toReturn.add(line.replace("%DEATHS%", profile.getDeathCount() + ""));
                continue;
            }

            if (line.contains("%KILL_STREAK%")) {
                toReturn.add(line.replace("%KILL_STREAK%", profile.getKillStreak() + ""));
                continue;
            }

            if (line.contains("%BALANCE%")) {
                toReturn.add(line.replace("%BALANCE%", profile.getBalance() + ""));
                continue;
            }

            if (line.contains("%LOGOUT%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.LOGOUT);

                if (cooldown != null) {
                    toReturn.add(line.replace("%LOGOUT%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%ARCHER_TAG%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.ARCHER_TAG);

                if (cooldown != null) {
                    toReturn.add(line.replace("%ARCHER_TAG%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%SPAWN_TAG%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.SPAWN_TAG);

                if (cooldown != null) {
                    toReturn.add(line.replace("%SPAWN_TAG%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%ENDER_PEARL%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.ENDER_PEARL);

                if (cooldown != null) {
                    toReturn.add(line.replace("%ENDER_PEARL%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%GOLDEN_APPLE%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.GOLDEN_APPLE);

                if (cooldown != null) {
                    toReturn.add(line.replace("%GOLDEN_APPLE%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%PVP_PROTECTION%")) {
                if (profile.getProtection() != null) {
                    toReturn.add(line.replace("%PVP_PROTECTION%", profile.getProtection().getTimeLeft()));
                }
                continue;
            }

            if (line.contains("%CLASS%") || line.contains("%CLASS_WARMUP%")) {
                if (profile.getKitWarmup() != null) {

                    if (profile.getKitWarmup().getKit() != null) {
                        toReturn.add(line.replace("%CLASS%", profile.getKitWarmup().getKit().getName()).replace("%CLASS_WARMUP%", profile.getKitWarmup().getTimeLeft()));
                    }
                }
                continue;
            }

            if (line.contains("%BARD%")) {
                if (profile.getEnergy() != null && profile.getEnergy().getEnergy() > 0.0D) {
                    for (String string : main.getScoreboardConfig().getStringList("PLACE_HOLDER.BARD")) {
                        toReturn.add(string.replace("%ENERGY%", profile.getEnergy().getFormattedString()));
                    }
                }
                continue;
            }

            toReturn.add(line);
        }

        if (toReturn.size() <= 2)

        {
            return null;
        }

        return toReturn;
    }
}
