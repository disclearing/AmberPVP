package com.amberpvp.hcfactions.factions.type;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.player.SimpleOfflinePlayer;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.player.SimpleOfflinePlayer;
import lombok.Getter;
import lombok.Setter;
import me.joeleoli.nucleus.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class PlayerFaction extends Faction {

    private static ConfigFile mainConfig = FactionsPlugin.getInstance().getMainConfig();

    private UUID leader;
    private Set<UUID> officers, members;
    private Set<PlayerFaction> allies;
    private Set<UUID> requestedAllies;
    private BigDecimal deathsTillRaidable;
    private int[] freezeInformation;
    private Map<UUID, UUID> invitedPlayers;
    private int balance, points, lives;
    private UUID focusPlayer;

    public PlayerFaction(String name, UUID leader, UUID uuid) {
        super(name, uuid);

        this.leader = leader;

        officers = new HashSet<>();
        members = new HashSet<>();
        invitedPlayers = new HashMap<>();
        deathsTillRaidable = BigDecimal.valueOf(mainConfig.getDouble("FACTION_GENERAL.STARTING_DTR"));
        requestedAllies = new HashSet<>();
        allies = new HashSet<>();
    }

    public void setFocusPlayer(UUID focusPlayer) {
        this.focusPlayer = focusPlayer;
    }

    public UUID getFocusPlayer() {
        return this.focusPlayer;
    }

    public boolean isRaidable() {
        return getDeathsTillRaidable().doubleValue() <= 0;
    }

    public boolean isFrozen() {
        return freezeInformation != null;
    }

    public void freeze(int duration) {
        freezeInformation = new int[]{duration, (int) (System.currentTimeMillis() / 1000)};
    }

    public BigDecimal getMaxDeathsTillRaidable() {
        return BigDecimal.valueOf(mainConfig.getDouble("FACTION_GENERAL.STARTING_DTR") + mainConfig.getDouble("FACTION_GENERAL.DTR_PER_PLAYER") * getAllPlayerUuids().size());
    }

    public List<UUID> getAllPlayerUuids() {
        List<UUID> toReturn = new ArrayList<>();

        toReturn.add(leader);
        toReturn.addAll(officers);
        toReturn.addAll(members);

        return toReturn;
    }

    public void sendMessage(String message) {
        for (Player player : getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public Set<Player> getOnlinePlayers() {
        Set<Player> toReturn = new HashSet<>();
        for (UUID uuid : getAllPlayerUuids()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                toReturn.add(player);
            }
        }
        return toReturn;
    }

    public Set<Player> getNoRoleMembers() {
        Set<Player> toReturn = new HashSet<>();
        for (UUID uuid : getAllPlayerUuids()) {

            if(this.leader == uuid) {
                continue;
            }

            if(this.officers.contains(uuid)) {
                continue;
            }

            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                toReturn.add(player);
            }
        }
        return toReturn;
    }

    public String getFormattedFreezeDuration(){
        int timeLeft = (int) (getFreezeInformation()[0] + getFreezeInformation()[1] - (System.currentTimeMillis() / 1000));
        long hours = TimeUnit.SECONDS.toHours(timeLeft);
        long minutes = TimeUnit.SECONDS.toMinutes(timeLeft) - (hours * 60);
        long seconds = TimeUnit.SECONDS.toSeconds(timeLeft) - ((hours * 60 * 60) + (minutes * 60));

        String formatted;

        if (hours == 0 && minutes > 0 && seconds > 0) {
            formatted = minutes + " minutes and " + seconds + " seconds";
        } else if (hours == 0 && minutes > 0 && seconds == 0) {
            formatted = minutes + " minutes";
        } else if (hours == 0 && minutes == 0 && seconds > 0) {
            formatted = seconds + " seconds";
        } else if (hours > 0 && minutes > 0 && seconds == 0) {
            formatted = hours + " hours and " + minutes + " minutes";
        } else if (hours > 0 && minutes == 0 && seconds > 0) {
            formatted = hours + " hours and " + seconds + " seconds";
        } else {
            formatted = hours + " hours, " + minutes + " minutes and " + seconds + " seconds";
        }

        if (hours == 1) {
            formatted = formatted.replace("hours", "hour");
        }

        if (minutes == 1) {
            formatted = formatted.replace("minutes", "minute");
        }

        if (seconds == 1) {
            formatted = formatted.replace("seconds", "second");
        }

        return formatted;
    }

    public Set<UUID> getAllyUuids() {
        Set<UUID> toReturn = new HashSet<>();

        for (PlayerFaction playerFaction : getAllies()) {
            toReturn.add(playerFaction.getUuid());
        }

        return toReturn;
    }

    public static PlayerFaction getByPlayer(Player player) {
        Profile profile = Profile.getByPlayer(player);
        return profile.getFaction();
    }

    public static PlayerFaction getByPlayerName(String name) {
        for (Faction faction : getFactions()) {
            if (faction instanceof PlayerFaction) {
                PlayerFaction playerFaction = (PlayerFaction) faction;

                for (UUID uuid : playerFaction.getAllPlayerUuids()) {
                    SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);

                    if (offlinePlayer != null) {
                        if (offlinePlayer.getName().equalsIgnoreCase(name)) {
                            return playerFaction;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static Faction getAnyByString(String factionName) {
        Faction faction = getByName(factionName);

        if (faction == null) {
            faction = PlayerFaction.getByPlayerName(factionName);

            if (faction == null) {
                return null;
            }
        }

        return faction;
    }

    public static Set<PlayerFaction> getPlayerFactions() {
        Set<PlayerFaction> toReturn = new HashSet<>();

        for (Faction faction : getFactions()) {
            if (faction instanceof PlayerFaction) {
                toReturn.add((PlayerFaction) faction);
            }
        }

        return toReturn;
    }

    public static void runTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
                    if (playerFaction.getDeathsTillRaidable().doubleValue() > playerFaction.getMaxDeathsTillRaidable().doubleValue()) {
                        playerFaction.setDeathsTillRaidable(playerFaction.getMaxDeathsTillRaidable());
                    }

                    if (playerFaction.getDeathsTillRaidable().doubleValue() < mainConfig.getDouble("FACTION_GENERAL.MIN_DTR")) {
                        playerFaction.setDeathsTillRaidable(BigDecimal.valueOf(mainConfig.getDouble("FACTION_GENERAL.MIN_DTR")));
                    }

                    if (playerFaction.isFrozen()) {
                        if (System.currentTimeMillis() / 1000 - playerFaction.getFreezeInformation()[1] >= playerFaction.getFreezeInformation()[0]) {
                            playerFaction.setFreezeInformation(null);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(FactionsPlugin.getInstance(), 20L, 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
                    if (!(playerFaction.isFrozen()) && playerFaction.getDeathsTillRaidable().doubleValue() < playerFaction.getMaxDeathsTillRaidable().doubleValue()) {
                        playerFaction.setDeathsTillRaidable(playerFaction.getDeathsTillRaidable().add(BigDecimal.valueOf(0.1)));
                    }
                }
            }
        }.runTaskTimerAsynchronously(FactionsPlugin.getInstance(), mainConfig.getInt("FACTION_GENERAL.REGEN_DELAY") * 20, mainConfig.getInt("FACTION_GENERAL.REGEN_DELAY") * 20);
    }

    public static Inventory getManageInventory(PlayerFaction playerFaction) {
        Inventory toReturn = Bukkit.createInventory(null, 9, playerFaction.getName());

        final String line = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------";

        List<String> punishLore = new ArrayList<>();
        punishLore.add(line);
        punishLore.add(ChatColor.GRAY + " * " + ChatColor.GOLD + "Ban");
        punishLore.add(ChatColor.GRAY + " * " + ChatColor.GOLD + "Kick");
        punishLore.add(ChatColor.GRAY + " * " + ChatColor.GOLD + "Mute");
        punishLore.add(ChatColor.GRAY + " * " + ChatColor.GOLD + "Freeze");
        punishLore.add(line);

        toReturn.setItem(0, (new ItemBuilder(Material.DIAMOND_SWORD)).name(ChatColor.RED + ChatColor.BOLD.toString() + "Punish Faction").lore(punishLore).build());



        toReturn.setItem(8, new ItemBuilder(Material.CARPET).durability(7).name(ChatColor.RED + "Cancel").build());

        for (int i = 0; i < 9; i++) {
            toReturn.setItem(9 + i, (new ItemBuilder(Material.STAINED_GLASS_PANE).enchantment(Enchantment.DURABILITY, 10)).data((short) 2).name(" ").build());
        }

        return toReturn;
    }


    public void addPoints(int value) {
        this.setPoints(this.getPoints() + value);
    }

    public void removePoints(int value) {
        this.setPoints(this.getPoints() - value);
    }

    public void addLives(int value) {
        this.setLives(this.getLives() + value);
    }

    public void removeLives(int value) {
        this.setLives(this.getLives() - value);
    }
}
