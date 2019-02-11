package com.amberpvp.hcfactions.profile;

import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.profile.kit.ProfileKit;
import com.amberpvp.hcfactions.profile.kit.ProfileKitCooldown;
import com.amberpvp.hcfactions.profile.kit.ProfileKitEnergy;
import com.amberpvp.hcfactions.profile.kit.ProfileKitWarmup;
import com.amberpvp.hcfactions.FactionsPlugin;
import com.amberpvp.hcfactions.claimwall.ClaimWall;
import com.amberpvp.hcfactions.deathlookup.DeathLookup;
import com.amberpvp.hcfactions.event.glowstone.procedure.GlowstoneCreateProcedure;
import com.amberpvp.hcfactions.event.koth.procedure.KothCreateProcedure;
import com.amberpvp.hcfactions.factions.claims.Claim;
import com.amberpvp.hcfactions.factions.claims.ClaimPillar;
import com.amberpvp.hcfactions.factions.claims.ClaimProfile;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldown;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldownType;
import com.amberpvp.hcfactions.profile.deathban.ProfileDeathban;
import com.amberpvp.hcfactions.profile.fight.ProfileFight;
import com.amberpvp.hcfactions.profile.fight.ProfileFightEffect;
import com.amberpvp.hcfactions.profile.fight.killer.ProfileFightKiller;
import com.amberpvp.hcfactions.profile.fight.killer.type.ProfileFightEnvironmentKiller;
import com.amberpvp.hcfactions.profile.fight.killer.type.ProfileFightPlayerKiller;
import com.amberpvp.hcfactions.profile.kit.ProfileKit;
import com.amberpvp.hcfactions.profile.kit.ProfileKitCooldown;
import com.amberpvp.hcfactions.profile.kit.ProfileKitEnergy;
import com.amberpvp.hcfactions.profile.kit.ProfileKitWarmup;
import com.amberpvp.hcfactions.profile.kit.ability.ProfileKitAbility;
import com.amberpvp.hcfactions.profile.kit.events.ArcherTagRemoveEvent;
import com.amberpvp.hcfactions.profile.options.ProfileOptions;
import com.amberpvp.hcfactions.profile.ore.ProfileOreType;
import com.amberpvp.hcfactions.profile.protection.ProfileProtection;
import com.amberpvp.hcfactions.profile.protection.life.ProfileProtectionLifeType;
import com.amberpvp.hcfactions.profile.teleport.ProfileTeleportTask;
import com.amberpvp.hcfactions.util.InventorySerialisation;
import com.amberpvp.hcfactions.util.LocationSerialization;
import com.amberpvp.hcfactions.util.player.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@Getter
public class Profile {

    @Getter
    private static FactionsPlugin main = FactionsPlugin.getInstance();
    @Getter
    private static MongoCollection collection = main.getFactionsDatabase().getProfiles();
    @Getter
    private static Map<UUID, Profile> profiles = new HashMap<>();

    private UUID uuid;
    private List<ProfileCooldown> cooldowns;
    private List<ProfileKitCooldown> kitCooldowns;
    private Map<ProfileProtectionLifeType, Integer> lives;
    private Map<ProfileOreType, Integer> ores;
    private ProfileOptions options;
    private List<PotionEffect> cachedEffects;
    private Map<Location, ClaimWall> walls;
    private List<ProfileFight> fights;
    private List<String> boughtKits;
    private LinkedHashMap<UUID, Boolean> previousFights;
    @Setter
    private ProfileKitEnergy energy;
    @Setter
    private String name;
    @Setter
    private ProfileProtection protection;
    @Setter
    private ProfileKit kit;
    @Setter
    private ProfileKitWarmup kitWarmup;
    @Setter
    private ProfileDeathban deathban;
    @Setter
    private boolean leftSpawn;
    @Setter
    private boolean respawning;
    @Setter
    private boolean combatLogged;
    @Setter
    private long playTime;
    @Setter
    private int balance;
    @Getter
    @Setter
    int killStreak;
    @Setter
    private boolean reclaim;
    @Setter
    private boolean cobble;
    @Getter
    @Setter
    long medicCooldown;
    @Setter
    private DeathLookup deathLookup;
    @Setter
    private boolean safeLogout;
    @Setter
    private Location logoutLocation;
    @Setter
    private KothCreateProcedure kothCreateProcedure;
    @Setter
    private GlowstoneCreateProcedure glowstoneCreateProcedure;
    @Setter
    private Map.Entry<UUID, ItemStack> lastDamager;
    @Setter
    private Location pearlLocation;
    @Setter
    private PlayerFaction faction;
    @Setter
    private ProfileChatType chatType;
    @Setter
    private ClaimProfile claimProfile;
    @Setter
    private Claim lastInside;
    @Setter
    private boolean viewingMap;
    @Setter
    private Set<ClaimPillar> mapPillars;
    @Setter
    private ProfileTeleportTask teleportWarmup;
    @Setter
    private boolean inAdminMode;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.leftSpawn = true;
        this.lives = new HashMap<>();
        this.ores = new HashMap<>();
        this.cooldowns = new ArrayList<>();
        this.kitCooldowns = new ArrayList<>();
        this.cachedEffects = new ArrayList<>();
        this.options = new ProfileOptions();
        this.fights = new ArrayList<>();
        this.boughtKits = new ArrayList<>();
        this.previousFights = new LinkedHashMap<>();
        this.walls = new HashMap<>();
        this.balance = 0;
        this.killStreak = 0;
        this.medicCooldown = 0L;
        this.reclaim = false;
        this.cobble = false;
        this.mapPillars = new HashSet<>();
        this.chatType = ProfileChatType.PUBLIC;

        for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
            if (playerFaction.getAllPlayerUuids().contains(uuid)) {
                this.faction = playerFaction;
            }
        }

        for (ProfileProtectionLifeType type : ProfileProtectionLifeType.values()) {
            this.lives.put(type, 0);
        }

        for (ProfileOreType type : ProfileOreType.values()) {
            this.ores.put(type, 0);
        }

        load();

        profiles.put(uuid, this);
    }

    public ProfileFight getLatestFight() {
        if (!(this.fights.isEmpty())) {
            return this.fights.get(this.fights.size() - 1);
        }

        return null;
    }

    public ProfileCooldown getCooldownByType(ProfileCooldownType type) {

        Iterator<ProfileCooldown> iterator = this.cooldowns.iterator();

        while (iterator.hasNext()) {

            ProfileCooldown cooldown = iterator.next();

            if (cooldown == null || type == null) {
                continue;
            }

            if (cooldown.getType() == type) {

                if (!cooldown.isFinished()) {
                    return cooldown;
                }

                if (type == ProfileCooldownType.LOGOUT) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            safeLogout = true;
                            Player player = Bukkit.getPlayer(uuid);

                            if (player != null) {
                                player.kickPlayer(main.getLanguageConfig().getString("COMBAT_LOGGER.LOGOUT_KICK"));
                            }
                        }
                    }.runTask(main);
                } else if (type == ProfileCooldownType.ARCHER_TAG) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Player player = Bukkit.getPlayer(uuid);

                            if (player != null) {
                                Bukkit.getServer().getPluginManager().callEvent(new ArcherTagRemoveEvent(player));
                            }
                        }
                    }.runTask(main);
                }

                iterator.remove();

            }
        }

        return null;
    }

    public ProfileKitCooldown getKitCooldownByType(ProfileKitAbility ability) {
        Iterator<ProfileKitCooldown> iterator = this.kitCooldowns.iterator();

        while (iterator.hasNext()) {
            ProfileKitCooldown cooldown = iterator.next();

            if (cooldown.isFinished()) {
                iterator.remove();
            } else {
                return cooldown;
            }
        }

        return null;
    }

    public ProfileProtection getProtection() {
        if (this.protection != null && this.protection.getDurationLeft() <= 0) {
            this.protection = null;
        }

        return this.protection;
    }

    public ProfileDeathban getDeathban() {
        if (this.deathban != null && (this.deathban.getCreatedAt() + this.deathban.getDuration() <= System.currentTimeMillis())) {
            this.deathban = null;
        }

        return this.deathban;
    }

    private void saveFights() {
        for (ProfileFight fight : this.fights) {
            if (!(this.previousFights.containsKey(fight.getUuid()))) {
                this.previousFights.put(fight.getUuid(), fight.getKiller() instanceof ProfileFightPlayerKiller && ((ProfileFightPlayerKiller) fight.getKiller()).getUuid().equals(uuid));
            }

            if (fight.getKiller() instanceof ProfileFightPlayerKiller && ((ProfileFightPlayerKiller) fight.getKiller()).getUuid().equals(uuid)) {
                continue;
            }

            Document document = new Document();
            document.put("uuid", fight.getUuid().toString());
            document.put("killed", this.uuid.toString());
            document.put("ping", fight.getPing());
            document.put("occurred_at", fight.getOccurredAt());
            document.put("health", 0);
            document.put("hunger", fight.getHunger());
            document.put("location", LocationSerialization.serializeLocation(fight.getLocation()));

            JsonArray effects = new JsonArray();

            for (ProfileFightEffect effect : fight.getEffects()) {
                JsonObject effectObject = new JsonObject();
                effectObject.addProperty("type", effect.getType().getName());
                effectObject.addProperty("level", effect.getLevel());
                effectObject.addProperty("duration", effect.getDuration());
                effects.add(effectObject);
            }

            document.put("effects", effects.toString());

            Document killerDocument = new Document();

            if (fight.getKiller() instanceof ProfileFightPlayerKiller) {
                ProfileFightPlayerKiller killer = (ProfileFightPlayerKiller) fight.getKiller();

                killerDocument.put("type", "PLAYER");
                killerDocument.put("name", killer.getName());
                killerDocument.put("uuid", killer.getUuid().toString());
                killerDocument.put("ping", killer.getPing());
                killerDocument.put("health", killer.getHealth());
                killerDocument.put("hunger", killer.getHunger());

                JsonArray killerEffects = new JsonArray();

                for (ProfileFightEffect effect : killer.getEffects()) {
                    JsonObject effectObject = new JsonObject();
                    effectObject.addProperty("type", effect.getType().getName());
                    effectObject.addProperty("level", effect.getLevel());
                    effectObject.addProperty("duration", effect.getDuration());
                    killerEffects.add(effectObject);
                }

                killerDocument.put("effects", killerEffects.toString());
                killerDocument.put("contents", InventorySerialisation.itemStackArrayToJson(killer.getContents()));
                killerDocument.put("armor", InventorySerialisation.itemStackArrayToJson(killer.getArmor()));

                document.put("killer_uuid", killer.getUuid().toString());
            } else if (fight.getKiller() instanceof ProfileFightEnvironmentKiller) {
                ProfileFightEnvironmentKiller killer = (ProfileFightEnvironmentKiller) fight.getKiller();

                killerDocument.put("type", killer.getType().name());
            } else {
                ProfileFightKiller killer = fight.getKiller();

                killerDocument.put("type", "MOB");
                killerDocument.put("mob_type", killer.getEntityType().name());
                killerDocument.put("name", killer.getName());
            }

            document.put("killer", killerDocument);
            document.put("contents", InventorySerialisation.itemStackArrayToJson(fight.getContents()));
            document.put("armor", InventorySerialisation.itemStackArrayToJson(fight.getArmor()));

            main.getFactionsDatabase().getFights().replaceOne(eq("uuid", fight.getUuid().toString()), document, new UpdateOptions().upsert(true));
        }

        this.fights.clear();
    }

    public void save() {
        Document document = new Document();
        document.put("uuid", uuid.toString());

        if (this.name != null) {
            document.put("recentName", this.name);
            document.put("recentNameLowercase", this.name.toLowerCase());
        }


        if (this.protection != null) {
            document.put("protection", this.protection.getDurationLeft());
        }

        if (this.respawning) {
            document.put("respawning", this.respawning);
        }

        if (this.combatLogged) {
            document.put("combatLogged", this.combatLogged);
        }

        if (!(this.leftSpawn)) {
            document.put("leftSpawn", this.leftSpawn);
        }

        if (this.playTime != 0) {
            document.put("playTime", this.playTime);
        }

        document.put("balance", this.balance);
        document.put("killStreak", this.killStreak);
        document.put("kills", getKillCount());
        document.put("deaths", getDeathCount());
        document.put("reclaim", this.reclaim);
        document.put("cobble", this.cobble);
        document.put("medicCooldown", this.medicCooldown);

        if (!main.isKitmapMode()) {
            Document livesDocument = new Document();

            for (ProfileProtectionLifeType type : this.lives.keySet()) {
                int amount = this.lives.get(type);

                if (amount > 0) {
                    livesDocument.put(type.name().toLowerCase(), amount);
                }
            }

            if (!(livesDocument.isEmpty())) {
                document.put("lives", livesDocument);
            }
        }

        if (!main.isKitmapMode() && this.deathban != null) {
            Document deathbanDocument = new Document();
            deathbanDocument.put("createdAt", this.deathban.getCreatedAt());
            deathbanDocument.put("duration", this.deathban.getDuration());
            document.put("deathban", deathbanDocument);
        }

        JsonArray fightsArray = new JsonArray();

        for (UUID fight : this.previousFights.keySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("uuid", fight.toString());
            object.addProperty("killer", this.previousFights.get(fight));
            fightsArray.add(object);
        }

        for (ProfileFight fight : this.fights) {
            JsonObject object = new JsonObject();
            object.addProperty("uuid", fight.getUuid().toString());
            object.addProperty("killer", (fight.getKiller() instanceof ProfileFightPlayerKiller && fight.getKiller().getName().equals(name)));
            fightsArray.add(object);
        }

        if (fightsArray.size() > 0) {
            document.put("fights", fightsArray.toString());
        }


        JsonArray achievementArray = new JsonArray();


        if (achievementArray.size() > 0) {
            document.put("achievements", achievementArray.toString());
        }

        Document oresDocument = new Document();

        for (ProfileOreType type : this.ores.keySet()) {
            oresDocument.put(type.name().toLowerCase(), this.ores.get(type));
        }

        document.put("ores", oresDocument);

        Document cooldowns = new Document();

        for (ProfileCooldown profileCooldown : this.cooldowns.stream().filter(profileCooldown -> !profileCooldown.isFinished()).collect(Collectors.toList())) {
            cooldowns.put(profileCooldown.getType().name(), profileCooldown.getSecondsLeft());
        }

        document.put("cooldowns", cooldowns);


        if (protection != null) {
            document.put("protection", protection.getLongDurationLeft());
        }

        collection.replaceOne(eq("uuid", this.uuid.toString()), document, new UpdateOptions().upsert(true));

        this.saveFights();
    }

    private void load() {
        Document document = (Document) collection.find(eq("uuid", uuid.toString())).first();

        if (document != null) {
            if (document.containsKey("recentName")) {
                this.name = document.getString("recentName");
            }

            if (document.containsKey("leftSpawn")) {
                this.leftSpawn = document.getBoolean("leftSpawn");
            }

            if (document.containsKey("combatLogged")) {
                this.combatLogged = document.getBoolean("combatLogged");
            }

            if (document.containsKey("respawning")) {
                this.respawning = document.getBoolean("respawning");
            }

            if (document.containsKey("reclaim")) {
                this.reclaim = document.getBoolean("reclaim");
            }

            if (document.containsKey("cobble")) {
                this.cobble = document.getBoolean("cobble");
            }

            if (document.containsKey("medicCooldown")) {
                this.medicCooldown = document.getLong("medicCooldown");
            }

            if (document.containsKey("balance")) {
                this.balance = document.getInteger("balance");
            }

            if (document.containsKey("killStreak")) {
                this.killStreak = document.getInteger("killStreak");
            }

            if (document.containsKey("deathban")) {
                Document deathbanDocument = (Document) document.get("deathban");
                this.deathban = new ProfileDeathban(deathbanDocument.getLong("createdAt"), deathbanDocument.getLong("duration"));
            }

            if (document.containsKey("lives")) {
                Document livesDocument = (Document) document.get("lives");

                for (String key : livesDocument.keySet()) {
                    this.lives.put(ProfileProtectionLifeType.valueOf(key.toUpperCase()), livesDocument.getInteger(key));
                }
            }


            if (document.containsKey("ores")) {
                Document oresDocument = (Document) document.get("ores");

                for (String typeName : oresDocument.keySet()) {
                    this.ores.put(ProfileOreType.valueOf(typeName.toUpperCase()), oresDocument.getInteger(typeName));
                }
            }

            if (document.containsKey("cooldowns")) {
                Document cooldownDocument = (Document) document.get("cooldowns");
                for (String typeName : cooldownDocument.keySet()) {
                    ProfileCooldown cooldown = new ProfileCooldown(ProfileCooldownType.valueOf(typeName), cooldownDocument.getLong(typeName));
                    if (!cooldown.isFinished()) {
                        this.cooldowns.add(cooldown);
                    }
                }
            }

            if (document.containsKey("fights")) {
                if (document.get("fights") instanceof String) {
                    JsonArray fightsArray = new JsonParser().parse(document.getString("fights")).getAsJsonArray();

                    for (JsonElement jsonElement : fightsArray) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        try {
                            previousFights.put(UUID.fromString(jsonObject.get("uuid").getAsString()), jsonObject.get("killer").getAsBoolean());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            if (!main.isKitmapMode()) {
                if (document.containsKey("protection")){
                    this.protection = new ProfileProtection(document.getLong("protection"));
                    this.protection.pause();
                }
            }

            this.leftSpawn = false;
        }
    }

    public int getKillCount() {
        int toReturn = 0;

        for (UUID uuid : this.previousFights.keySet()) {
            if (this.previousFights.get(uuid)) {
                toReturn++;
            }
        }

        for (ProfileFight fight : this.fights) {
            if (fight.getKiller() instanceof ProfileFightPlayerKiller) {
                ProfileFightPlayerKiller killer = (ProfileFightPlayerKiller) fight.getKiller();
                if (killer.getUuid().equals(this.uuid)) {
                    toReturn++;
                }
            }
        }

        return toReturn;
    }

    public int getDeathCount() {
        int toReturn = 0;

        for (Boolean b : this.previousFights.values()) {
            if (!(b)) {
                toReturn++;
            }
        }

        for (ProfileFight fight : this.fights) {
            if (fight.getKiller() instanceof ProfileFightPlayerKiller) {
                ProfileFightPlayerKiller killer = (ProfileFightPlayerKiller) fight.getKiller();

                if (!killer.getUuid().equals(this.uuid)) {
                    toReturn++;
                }
            } else {
                toReturn++;
            }
        }

        return toReturn;
    }

    public void updateTab() {
        Player player = Bukkit.getPlayer(this.uuid);

        if (player != null) {
            Scoreboard scoreboard;
            boolean newScoreboard = false;

            if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
                scoreboard = player.getScoreboard();
            } else {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                newScoreboard = true;
            }

            ConfigFile mainConfig = main.getMainConfig();

            if (mainConfig.getBoolean("TAB_LIST.ENABLED")) {
                Team friendly = getExistingOrCreateNewTeam("friendly", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.FRIENDLY_COLOR")));
                Team ally = getExistingOrCreateNewTeam("ally", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ALLY_COLOR")));
                Team enemy = getExistingOrCreateNewTeam("enemy", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ENEMY_COLOR")));
                Team archer = getExistingOrCreateNewTeam("archer_tagged", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ARCHER_TAG")));
                Team focus = getExistingOrCreateNewTeam("focus", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.FOCUS")));


                if (faction != null) {
                    for (Player friendlyPlayer : faction.getOnlinePlayers()) {
                        if (friendlyPlayer.equals(player)) {
                            continue;
                        }

                        if (!(friendly.hasEntry(friendlyPlayer.getName()))) {
                            friendly.addEntry(friendlyPlayer.getName());
                        }
                    }

                    for (PlayerFaction allyFaction : faction.getAllies()) {
                        for (Player allyPlayer : allyFaction.getOnlinePlayers()) {
                            if (!(ally.hasEntry(allyPlayer.getName()))) {
                                ally.addEntry(allyPlayer.getName());
                            }
                        }
                    }

                    if (faction.getFocusPlayer() != null) {

                        Player online = Bukkit.getPlayer(faction.getFocusPlayer());

                        if (online != null && !focus.hasEntry(online.getName())) {
                            focus.addEntry(online.getName());
                        }
                    }

                }

                for (Player archerPlayer : PlayerUtility.getOnlinePlayers()) {
                    if (!(archerPlayer.getName().equals(player.getName()))) {
                        Profile archerProfile = getByPlayer(archerPlayer);

                        if (archerProfile != null && archerProfile.getCooldownByType(ProfileCooldownType.ARCHER_TAG) != null) {
                            if (!(archer.hasEntry(archerPlayer.getName()))) {
                                archer.addEntry(archerPlayer.getName());
                            }
                        }
                    }
                }

                for (Player enemyPlayer : PlayerUtility.getOnlinePlayers()) {
                    if (!(enemyPlayer.getName().equals(player.getName()))) {

                        if (friendly.hasEntry(enemyPlayer.getName()) && (faction == null || !faction.getOnlinePlayers().contains(enemyPlayer))) {
                            friendly.removeEntry(enemyPlayer.getName());
                        }

                        if (ally.hasEntry(enemyPlayer.getName())) {
                            Profile enemyProfile = Profile.getByPlayer(enemyPlayer);
                            PlayerFaction enemyFaction = enemyProfile.getFaction();

                            if (enemyFaction == null || faction == null || !faction.getAllies().contains(enemyFaction)) {
                                ally.removeEntry(enemyPlayer.getName());
                            }
                        }

                        if (archer.hasEntry(enemyPlayer.getName())) {
                            Profile enemyProfile = getByPlayer(enemyPlayer);

                            if (enemyProfile != null && (enemyProfile.getCooldownByType(ProfileCooldownType.ARCHER_TAG) == null)) {
                                archer.removeEntry(enemyPlayer.getName());
                            }
                        }

                        if (focus.hasEntry(enemyPlayer.getName())) {
                            Profile enemyProfile = Profile.getByPlayer(enemyPlayer);

                            if (enemyProfile != null && (faction == null || faction.getFocusPlayer() == null)) {
                                focus.removeEntry(enemyPlayer.getName());
                            }
                        }

                        if (!(friendly.hasEntry(enemyPlayer.getName())) && (!(ally.hasEntry(enemyPlayer.getName()))) && (!(focus.hasEntry(enemyPlayer.getName()))) && (!(archer.hasEntry(enemyPlayer.getName())))) {
                            enemy.addEntry(enemyPlayer.getName());
                        }
                    }
                }

                if (!(friendly.hasEntry(player.getName()))) {
                    friendly.addEntry(player.getName());
                }

                if (newScoreboard) {
                    player.setScoreboard(scoreboard);
                }
            }
        }
    }

    public void updateTab(Player toUpdate) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            Scoreboard scoreboard;
            boolean newScoreboard = false;

            if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
                scoreboard = player.getScoreboard();
            } else {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                newScoreboard = true;
            }

            ConfigFile mainConfig = main.getMainConfig();

            if (mainConfig.getBoolean("TAB_LIST.ENABLED")) {
                Team friendly = getExistingOrCreateNewTeam("friendly", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.FRIENDLY_COLOR")));
                Team ally = getExistingOrCreateNewTeam("ally", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ALLY_COLOR")));
                Team enemy = getExistingOrCreateNewTeam("enemy", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ENEMY_COLOR")));
                Team archer = getExistingOrCreateNewTeam("archer_tagged", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ARCHER_TAG")));
                Team focus = getExistingOrCreateNewTeam("focus", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.FOCUS")));


                if (faction != null) {
                    if (faction.getOnlinePlayers().contains(toUpdate) && !(friendly.hasEntry(toUpdate.getName()))) {
                        friendly.addEntry(toUpdate.getName());
                    }

                    for (PlayerFaction allyFaction : faction.getAllies()) {
                        if (allyFaction.getOnlinePlayers().contains(toUpdate) && !(ally.hasEntry(toUpdate.getName()))) {
                            ally.addEntry(toUpdate.getName());
                        }
                    }

                    if (faction.getFocusPlayer() != null) {

                        Player online = Bukkit.getPlayer(faction.getFocusPlayer());

                        if (online != null && !focus.hasEntry(online.getName())) {
                            focus.addEntry(online.getName());
                        }
                    }
                }

                for (Player archerPlayer : PlayerUtility.getOnlinePlayers()) {
                    Profile archerProfile = Profile.getByPlayer(archerPlayer);

                    if (archerProfile != null && archerProfile.getCooldownByType(ProfileCooldownType.ARCHER_TAG) != null) {
                        if (!(archer.hasEntry(archerPlayer.getName()))) {
                            archer.addEntry(archerPlayer.getName());
                        }
                    }
                }

                if (friendly.hasEntry(toUpdate.getName()) && (faction == null || !faction.getOnlinePlayers().contains(toUpdate))) {
                    friendly.removeEntry(toUpdate.getName());
                }

                if (ally.hasEntry(toUpdate.getName())) {
                    Profile enemyProfile = Profile.getByPlayer(toUpdate);
                    PlayerFaction enemyFaction = enemyProfile.getFaction();

                    if (enemyFaction == null || faction == null || !faction.getAllies().contains(enemyFaction)) {
                        ally.removeEntry(toUpdate.getName());
                    }
                }


                if (archer.hasEntry(toUpdate.getName())) {
                    Profile enemyProfile = Profile.getByPlayer(toUpdate);

                    if (enemyProfile != null && (enemyProfile.getCooldownByType(ProfileCooldownType.ARCHER_TAG) == null)) {
                        archer.removeEntry(toUpdate.getName());
                    }
                }

                if (focus.hasEntry(toUpdate.getName())) {
                    Profile enemyProfile = Profile.getByPlayer(toUpdate);

                    if (enemyProfile != null && (faction == null || faction.getFocusPlayer() == null)) {
                        focus.removeEntry(toUpdate.getName());
                    }
                }

                if (!(friendly.hasEntry(toUpdate.getName())) && (!(ally.hasEntry(toUpdate.getName()))) && (!(focus.hasEntry(toUpdate.getName()))) && (!(archer.hasEntry(toUpdate.getName())))) {
                    enemy.addEntry(toUpdate.getName());
                }

                if (archer.hasEntry(toUpdate.getName()) && enemy.hasEntry(toUpdate.getName())) {
                    enemy.removeEntry(toUpdate.getName());
                }

                if (focus.hasEntry(toUpdate.getName()) && enemy.hasEntry(toUpdate.getName())) {
                    enemy.removeEntry(toUpdate.getName());
                }

                if (!(friendly.hasEntry(player.getName()))) {
                    Team vanished = scoreboard.getTeam("hidden");

                    if (vanished == null || !vanished.hasEntry(player.getName())) {
                        friendly.addEntry(player.getName());
                    }
                }

                if (newScoreboard) {
                    player.setScoreboard(scoreboard);
                }
            }
        }

    }

    private Team getExistingOrCreateNewTeam(String string, Scoreboard scoreboard, ChatColor prefix) {
        Team toReturn = scoreboard.getTeam(string);

        if (toReturn == null) {
            toReturn = scoreboard.registerNewTeam(string);
            toReturn.setPrefix(prefix + "");
        }

        return toReturn;
    }

    public static void sendGlobalTabUpdate() {
        for (Player player : PlayerUtility.getOnlinePlayers()) {
            getByPlayer(player).updateTab();
        }
    }

    public static void sendPlayerTabUpdate(Player toUpdate) {
        for (Player player : PlayerUtility.getOnlinePlayers()) {
            getByPlayer(player).updateTab(toUpdate);
        }
    }

    public static Profile getByName(String name) {
        for (Profile profile : profiles.values()) {
            if (profile.getName() != null && profile.getName().equals(name)) {
                return profile;
            }
        }

        Document document = (Document) collection.find(eq("recentNameLowercase", name.toLowerCase())).first();

        if (document != null) {
            return new Profile(UUID.fromString(document.getString("uuid")));
        }

        return null;
    }

    public static Profile getByUuid(UUID uuid) {
        Profile profile = profiles.get(uuid);

        if (profile == null) {
            profile = new Profile(uuid);
        }

        return profile;
    }

    public static Profile getByPlayer(Player player) {
        Profile profile = profiles.get(player.getUniqueId());

        if (profile == null) {
            profile = new Profile(player.getUniqueId());
        }

        return profile;
    }

    public static Map<UUID, Profile> getProfilesMap() {
        return profiles;
    }

    public static Collection<Profile> getProfiles() {
        return profiles.values();
    }

}