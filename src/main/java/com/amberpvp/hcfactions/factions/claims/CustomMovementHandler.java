package com.amberpvp.hcfactions.factions.claims;

import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.claimwall.ClaimWallType;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.factions.type.SystemFaction;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.misc.listeners.BorderListener;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.util.LocationSerialization;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.claimwall.ClaimWallType;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.events.player.PlayerCancelFactionTeleportEvent;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.factions.type.SystemFaction;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.misc.listeners.BorderListener;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldownType;
import com.amberpvp.hcfactions.profile.teleport.ProfileTeleportType;
import com.amberpvp.hcfactions.util.LocationSerialization;
import me.joeleoli.ragespigot.handler.MovementHandler;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CustomMovementHandler implements MovementHandler {

    private final FactionsPlugin plugin = FactionsPlugin.getInstance();
    private ConfigFile mainConfig = plugin.getMainConfig();
    private ConfigFile langConfig = plugin.getLanguageConfig();

    @Override
    public void handleUpdateLocation(Player p, Location to, Location from, PacketPlayInFlying packetPlayInFlying) {

        Profile profile = Profile.getByPlayer(p);

        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {

            if (profile.getTeleportWarmup() != null) {
                if (profile.getTeleportWarmup().getEvent().getTeleportType() == ProfileTeleportType.HOME_TELEPORT) {
                    profile.getTeleportWarmup().getEvent().setCancelled(true);
                    Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(p, null, ProfileTeleportType.HOME_TELEPORT));
                    profile.setTeleportWarmup(null);
                    p.sendMessage(langConfig.getString("ERROR.TELEPORT_CANCELLED"));
                } else {
                    if (p.getLocation().distance(profile.getTeleportWarmup().getEvent().getInitialLocation()) >= mainConfig.getInt("TELEPORT_COUNTDOWN.STUCK.DISTANCE")) {
                        profile.getTeleportWarmup().getEvent().setCancelled(true);
                        Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(p, profile.getTeleportWarmup().getEvent().getFaction(), ProfileTeleportType.STUCK_TELEPORT));
                        profile.setTeleportWarmup(null);
                        p.sendMessage(langConfig.getString("ERROR.TELEPORT_CANCELLED"));
                    }
                }
            }
        }

        if (profile.getCooldownByType(ProfileCooldownType.LOGOUT) != null && profile.getLogoutLocation() != null) {
            if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ() || from.getBlockY() != to.getBlockY()) {
                if (to.distance(profile.getLogoutLocation()) > mainConfig.getInt("COMBAT_LOGGER.LOGOUT_CANCEL_RANGE")) {
                    p.sendMessage(langConfig.getString("COMBAT_LOGGER.LOGOUT_CANCELLED"));
                    profile.getCooldowns().remove(profile.getCooldownByType(ProfileCooldownType.LOGOUT));
                    profile.setLogoutLocation(null);
                }
            }
        }

        if (profile.getCooldownByType(ProfileCooldownType.SPAWN_TAG) != null) {
            Claim entering = Claim.getProminentClaimAt(to);
            Claim leaving = Claim.getProminentClaimAt(from);

            if (entering != null && (leaving == null || !leaving.equals(entering))) {
                if (ClaimWallType.SPAWN_TAG.isValid(entering)) {
                    p.teleport(from);
                    p.setSprinting(false);

                    if (p.getVehicle() != null) {
                        p.getVehicle().eject();
                    }

                    p.setVelocity(new Vector());
                }
            }
        }

        if (profile.getProtection() != null) {
            Claim entering = Claim.getProminentClaimAt(to);
            Claim leaving = Claim.getProminentClaimAt(from);

            if (entering != null && (leaving == null || !leaving.equals(entering))) {
                if (ClaimWallType.PVP_PROTECTION.isValid(entering)) {
                    p.teleport(from);

                    if (p.getVehicle() != null) {
                        p.getVehicle().eject();
                    }

                    p.setSprinting(false);
                    p.setVelocity(new Vector());
                }

            }
        }

        if (!BorderListener.isWithinBorder(to) && BorderListener.isWithinBorder(from)) {

            if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
                return;
            }

            p.sendMessage(ChatColor.RED + "You can't go past the border.");
            p.teleport(from);
            final Entity vehicle = p.getVehicle();
            if (vehicle != null) {
                vehicle.eject();
                vehicle.teleport(from);
                vehicle.setPassenger(p);

            }
        }

        if (p.getWorld().getEnvironment() == World.Environment.THE_END) {
            if ((from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ() || from.getBlockY() != to.getBlockY()) && to.getBlock() != null && from.getWorld().getEnvironment() == World.Environment.THE_END) {
                final Material type = p.getWorld().getBlockAt(p.getLocation()).getType();

                if (type == Material.STATIONARY_WATER || type == Material.WATER) {

                    Faction faction = Faction.getByName("South Road");
                    if (faction != null && faction instanceof SystemFaction) {

                        if (faction.getHome() == null) {
                            p.sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getString("ERROR.HOME_NOT_SET"));
                            return;
                        }

                        Location location = LocationSerialization.deserializeLocation(faction.getHome());

                        if (location != null) {
                            p.teleport(location);
                        }
                    }
                }

            }
        }

        if (to.getX() != from.getX() || to.getZ() != from.getZ()) {

            final Claim claim = Claim.getProminentClaimAt(to);

            if (claim != null) {
                if (claim.isInside(to)) {
                    if (profile.getLastInside() == null) {
                        profile.setLastInside(claim);
                        p.sendMessage(langConfig.getString("FACTION_CLAIM.LEAVE.WILDERNESS"));
                        p.sendMessage(getEnteringMessage(profile, claim));
                        return;
                    }

                    if (profile.getLastInside().getFaction() != claim.getFaction()) {
                        if (profile.getLastInside().isInside(to) && !profile.getLastInside().isGreaterThan(claim)) {
                            return;
                        }

                        p.sendMessage(getLeavingMessage(profile, profile.getLastInside()));
                        p.sendMessage(getEnteringMessage(profile, claim));
                    }

                    profile.setLastInside(claim);
                } else {
                    if (profile.getLastInside() != null && profile.getLastInside() == claim) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (profile.getLastInside() != null && profile.getLastInside() == claim) {
                                    p.sendMessage(getLeavingMessage(profile, claim));
                                    p.sendMessage(langConfig.getString("FACTION_CLAIM.ENTER.WILDERNESS"));
                                    profile.setLastInside(null);
                                }
                            }
                        }.runTaskLater(plugin, 1L);
                    }
                }
            } else {
                if (profile.getLastInside() != null) {
                    p.sendMessage(getLeavingMessage(profile, profile.getLastInside()));
                    p.sendMessage(langConfig.getString("FACTION_CLAIM.ENTER.WILDERNESS"));
                    profile.setLastInside(profile.getLastInside());
                    profile.setLastInside(null);
                }
            }

        }
    }

    @Override
    public void handleUpdateRotation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {

    }

    private String getLeavingMessage(Profile profile, Claim inside) {

        if (profile.getFaction() != null && profile.getFaction() == inside.getFaction()) {
            return langConfig.getString("FACTION_CLAIM.LEAVE.FRIENDLY").replace("%FACTION%", inside.getFaction().getName());
        } else if (profile.getFaction() != null && inside.getFaction() instanceof PlayerFaction && profile.getFaction().getAllies().contains(inside.getFaction())) {
            return langConfig.getString("FACTION_CLAIM.LEAVE.ALLY").replace("%FACTION%", inside.getFaction().getName());
        } else if (!(inside.getFaction() instanceof PlayerFaction)) {
            SystemFaction systemFaction = (SystemFaction) inside.getFaction();

            if (systemFaction.isDeathban()) {
                return langConfig.getString("FACTION_CLAIM.LEAVE.SYSTEM_FACTION_DEATHBAN").replace("%FACTION%", systemFaction.getName()).replace("%COLOR%", systemFaction.getColor() + "");
            } else {

                return langConfig.getString("FACTION_CLAIM.LEAVE.SYSTEM_FACTION_NON-DEATHBAN").replace("%FACTION%", systemFaction.getName()).replace("%COLOR%", systemFaction.getColor() + "");
            }
        } else {
            return langConfig.getString("FACTION_CLAIM.LEAVE.ENEMY").replace("%FACTION%", inside.getFaction().getName());
        }
    }

    private String getEnteringMessage(Profile profile, Claim inside) {
        if (profile.getFaction() != null && profile.getFaction() == inside.getFaction()) {
            return langConfig.getString("FACTION_CLAIM.ENTER.FRIENDLY").replace("%FACTION%", inside.getFaction().getName());
        } else if (profile.getFaction() != null && inside.getFaction() instanceof PlayerFaction && profile.getFaction().getAllies().contains(inside.getFaction())) {
            return langConfig.getString("FACTION_CLAIM.ENTER.ALLY").replace("%FACTION%", inside.getFaction().getName());
        } else if (!(inside.getFaction() instanceof PlayerFaction)) {
            SystemFaction systemFaction = (SystemFaction) inside.getFaction();

            if (systemFaction.isDeathban()) {
                return langConfig.getString("FACTION_CLAIM.ENTER.SYSTEM_FACTION_DEATHBAN").replace("%FACTION%", systemFaction.getName()).replace("%COLOR%", systemFaction.getColor() + "");
            } else {
                Player player = Bukkit.getPlayer(profile.getUuid());

                if (player != null) {
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setFireTicks(0);
                    player.setSaturation(4.0F);

                }

                return langConfig.getString("FACTION_CLAIM.ENTER.SYSTEM_FACTION_NON-DEATHBAN").replace("%FACTION%", systemFaction.getName()).replace("%COLOR%", systemFaction.getColor() + "");
            }
        } else {
            return langConfig.getString("FACTION_CLAIM.ENTER.ENEMY").replace("%FACTION%", inside.getFaction().getName());
        }
    }
}
