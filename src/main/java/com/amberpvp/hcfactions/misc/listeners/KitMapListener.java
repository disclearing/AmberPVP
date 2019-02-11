package com.amberpvp.hcfactions.misc.listeners;

import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import com.google.common.cache.CacheBuilder;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class KitMapListener implements Listener {
    private final long COOLDOWN = TimeUnit.SECONDS.toMillis(10L);
    private final String[] KITS = new String[]{"Diamond", "Bard", "Archer", "Rogue"};
    private final ChatColor[] COLOURS = new ChatColor[]{ChatColor.BLACK, ChatColor.BLACK, ChatColor.BLACK, ChatColor.BLACK, ChatColor.BLACK};
    final FactionsPlugin plugin;
    private final String longlines;
    private final ConcurrentMap lastClicks;
    private final AutoKothRunnable autoKothRunnable;

    public KitMapListener(FactionsPlugin plugin) {
        this.longlines = ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat('-', 11);
        this.lastClicks = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).concurrencyLevel(1).build().asMap();
        this.plugin = plugin;
        this.autoKothRunnable = new AutoKothRunnable(this, plugin, TimeUnit.HOURS.toMillis(1L) + TimeUnit.MINUTES.toMillis(15L));
        Bukkit.getScheduler().runTaskTimer(plugin, this.autoKothRunnable, 6000L, 12000L);
    }

    /*public int randomTick() {
        return ThreadLocalRandom.current().nextInt(20);
    }*/

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGHEST
    )
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.VILLAGER || event.getEntityType() != EntityType.ENDER_DRAGON) {
            event.setCancelled(true);
        }
    }


    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (item != null) {
            new BukkitRunnable() {
                public void run() {
                    item.remove();
                }
            }
                    /*.runTaskLater(this.plugin, (long)(100 + this.randomTick()));*/
                    .runTaskLater(this.plugin, 15 * 20);
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();

        if (drops != null) {
            new BukkitRunnable() {
                public void run() {
                    for (ItemStack drop : drops) {
                        drop.setType(Material.AIR);
                    }
                }
            }.runTaskLater(this.plugin, 15 * 20);
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onSignUpdate(SignChangeEvent e) {
        if (e.getPlayer().hasPermission("staff.admin")) {
            int i = 0;
            String[] var3 = KITS;
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String kit = var3[var5];
                if (e.getLine(0).equalsIgnoreCase("[" + kit + "]")) {
                    ChatColor chatColor = COLOURS[i];
                    e.setLine(0, chatColor + "- Kit -");
                    e.setLine(1, chatColor + kit);
                    return;
                }

                ++i;
            }
        }

    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }


    public class AutoKothRunnable implements Runnable {
        private final FactionsPlugin hcf;
        private final List<KothEvent> kothFactions;
        private final long TIME_BETWEEN;
        private final int total;
        private int current;
        private KothEvent lastKoth;
        private long lastKothTime;

        public AutoKothRunnable(KitMapListener kitMapListener, FactionsPlugin hcf, long TIME_BETWEEN) {
            this.current = 0;
            this.hcf = hcf;
            this.TIME_BETWEEN = TIME_BETWEEN;
            this.kothFactions = EventManager.getInstance().getEvents().stream().filter((faction) -> {
                return faction instanceof KothEvent;
            }).map((faction) -> {
                return (KothEvent) faction;
            }).filter((faction) -> {
                return faction.getZone() != null;
            }).collect(Collectors.toList());
            this.total = this.kothFactions.size();
            Collections.shuffle(this.kothFactions);
            hcf.getLogger().info("Using " + this.total + " automatic KOTHS");
        }

        public void run() {
            if (this.total > 1 && (this.lastKoth == null || System.currentTimeMillis() - this.lastKothTime >= this.TIME_BETWEEN)) {
                if (this.current == this.total) {
                    Collections.shuffle(this.kothFactions);
                    this.current = 0;
                }

                KothEvent kothFaction = this.kothFactions.get(this.current);
                kothFaction.start(TimeUnit.MINUTES.toMillis(4L));
                ++this.current;
                this.lastKoth = kothFaction;
                this.lastKothTime = System.currentTimeMillis();
            }
        }

    }
}
