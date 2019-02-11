package com.amberpvp.hcfactions;

import com.amberpvp.hcfactions.blockoperation.BlockOperationModifier;
import com.amberpvp.hcfactions.blockoperation.BlockOperationModifierListeners;
import com.amberpvp.hcfactions.chat.FactionsChatFormat;
import com.amberpvp.hcfactions.claimwall.ClaimWallListeners;
import com.amberpvp.hcfactions.combatlogger.CombatLogger;
import com.amberpvp.hcfactions.combatlogger.CombatLoggerListeners;
import com.amberpvp.hcfactions.combatlogger.commands.CombatLoggerCommand;
import com.amberpvp.hcfactions.command.CloneInventoryCommand;
import com.amberpvp.hcfactions.command.GiveInventoryCommand;
import com.amberpvp.hcfactions.command.LastInventoryCommand;
import com.amberpvp.hcfactions.crowbar.CrowbarListeners;
import com.amberpvp.hcfactions.deathlookup.DeathLookupCommand;
import com.amberpvp.hcfactions.deathlookup.DeathLookupListeners;
import com.amberpvp.hcfactions.economysign.EconomySignListeners;
import com.amberpvp.hcfactions.enchantmentlimiter.EnchantmentLimiterListeners;
import com.amberpvp.hcfactions.event.Event;
import com.amberpvp.hcfactions.event.EventManager;
import com.amberpvp.hcfactions.event.glowstone.GlowstoneEvent;
import com.amberpvp.hcfactions.event.glowstone.GlowstoneEventListeners;
import com.amberpvp.hcfactions.event.glowstone.command.GlowstoneForceCommand;
import com.amberpvp.hcfactions.event.glowstone.procedure.GlowstoneCreateProcedureListeners;
import com.amberpvp.hcfactions.event.glowstone.procedure.command.GlowstoneProcedureCommand;
import com.amberpvp.hcfactions.event.glowstone.procedure.command.GlowstoneRemoveCommand;
import com.amberpvp.hcfactions.event.koth.KothEvent;
import com.amberpvp.hcfactions.event.koth.KothEventListeners;
import com.amberpvp.hcfactions.event.koth.command.KothCommand;
import com.amberpvp.hcfactions.event.koth.command.KothScheduleCommand;
import com.amberpvp.hcfactions.event.koth.command.KothStartCommand;
import com.amberpvp.hcfactions.event.koth.command.KothStopCommand;
import com.amberpvp.hcfactions.event.koth.procedure.KothCreateProcedureListeners;
import com.amberpvp.hcfactions.event.koth.procedure.command.KothCreateProcedureCommand;
import com.amberpvp.hcfactions.event.koth.procedure.command.KothRemoveCommand;
import com.amberpvp.hcfactions.factions.Faction;
import com.amberpvp.hcfactions.factions.claims.ClaimListeners;
import com.amberpvp.hcfactions.factions.claims.ClaimPillar;
import com.amberpvp.hcfactions.factions.claims.CustomMovementHandler;
import com.amberpvp.hcfactions.factions.commands.*;
import com.amberpvp.hcfactions.factions.commands.admin.*;
import com.amberpvp.hcfactions.factions.commands.leader.FactionDemoteCommand;
import com.amberpvp.hcfactions.factions.commands.leader.FactionDisbandCommand;
import com.amberpvp.hcfactions.factions.commands.leader.FactionLeaderCommand;
import com.amberpvp.hcfactions.factions.commands.leader.FactionPromoteCommand;
import com.amberpvp.hcfactions.factions.commands.officer.*;
import com.amberpvp.hcfactions.factions.commands.system.FactionColorCommand;
import com.amberpvp.hcfactions.factions.commands.system.FactionCreateSystemCommand;
import com.amberpvp.hcfactions.factions.commands.system.FactionToggleDeathbanCommand;
import com.amberpvp.hcfactions.factions.type.PlayerFaction;
import com.amberpvp.hcfactions.files.ConfigFile;
import com.amberpvp.hcfactions.itemdye.ItemDye;
import com.amberpvp.hcfactions.itemdye.ItemDyeListeners;
import com.amberpvp.hcfactions.misc.commands.*;
import com.amberpvp.hcfactions.misc.commands.economy.AddBalanceCommand;
import com.amberpvp.hcfactions.misc.commands.economy.BalanceCommand;
import com.amberpvp.hcfactions.misc.commands.economy.PayCommand;
import com.amberpvp.hcfactions.misc.commands.economy.SetBalanceCommand;
import com.amberpvp.hcfactions.misc.listeners.*;
import com.amberpvp.hcfactions.mode.Mode;
import com.amberpvp.hcfactions.mode.ModeListeners;
import com.amberpvp.hcfactions.mode.command.ModeCommand;
import com.amberpvp.hcfactions.potionlimiter.PotionLimiterListeners;
import com.amberpvp.hcfactions.profile.Profile;
import com.amberpvp.hcfactions.profile.ProfileAutoSaver;
import com.amberpvp.hcfactions.profile.ProfileListeners;
import com.amberpvp.hcfactions.profile.cooldown.ProfileCooldownListeners;
import com.amberpvp.hcfactions.profile.fight.command.KillStreakCommand;
import com.amberpvp.hcfactions.profile.kit.ProfileKitActionListeners;
import com.amberpvp.hcfactions.profile.kit.command.ProfileKitCommand;
import com.amberpvp.hcfactions.profile.options.command.ProfileOptionsCommand;
import com.amberpvp.hcfactions.profile.ore.ProfileOreCommand;
import com.amberpvp.hcfactions.profile.protection.ProfileProtection;
import com.amberpvp.hcfactions.profile.protection.command.ProfileProtectionCommand;
import com.amberpvp.hcfactions.statracker.StatTrackerListeners;
import com.amberpvp.hcfactions.subclaim.SubclaimListeners;
import com.amberpvp.hcfactions.util.FactionsBoardAdapter;
import com.amberpvp.hcfactions.util.command.CommandFramework;
import com.amberpvp.hcfactions.util.database.FactionsDatabase;
import com.amberpvp.hcfactions.util.player.PlayerUtility;
import com.amberpvp.hcfactions.util.player.SimpleOfflinePlayer;
import lombok.Getter;
import lombok.Setter;
import me.joeleoli.nucleus.Nucleus;
import me.joeleoli.nucleus.board.BoardManager;
import me.joeleoli.ragespigot.RageSpigot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@Getter
public class FactionsPlugin extends JavaPlugin {

    @Getter
    private static FactionsPlugin instance;

    private CommandFramework framework;
    private FactionsDatabase factionsDatabase;
    private ConfigFile mainConfig, scoreboardConfig, languageConfig, kothScheduleConfig;
    @Setter
    private boolean loaded;
    @Setter
    private boolean kitmapMode;

    public void onEnable() {
        instance = this;

        this.mainConfig = new ConfigFile(this, "config");
        this.languageConfig = new ConfigFile(this, "lang");
        this.scoreboardConfig = new ConfigFile(this, "scoreboard");
        this.kothScheduleConfig = new ConfigFile(this, "koth-schedule");
        this.factionsDatabase = new FactionsDatabase(this);
        this.kitmapMode = this.mainConfig.getBoolean("KITMAP_MODE");

        for (Player player : PlayerUtility.getOnlinePlayers()) {
            new Profile(player.getUniqueId());
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == CombatLogger.ENTITY_TYPE) {
                    if (entity instanceof LivingEntity) {
                        if (entity.getCustomName() != null) {
                            entity.remove();
                        }
                    }
                }
            }
        }

        this.framework = new CommandFramework(this);
        SimpleOfflinePlayer.load(this);
        Faction.load();
        Mode.load();
        KothEvent.load();
        GlowstoneEvent.load();
        BlockOperationModifier.run();
        ProfileProtection.run(this);
        registerRecipes();
        registerListeners();
        registerCommands();
        PlayerFaction.runTasks();
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ProfileAutoSaver(this), 5000L, 5000L);
        Nucleus.getInstance().setBoardManager(new BoardManager(this, new FactionsBoardAdapter(this)));
        Nucleus.getInstance().setChatFormat(new FactionsChatFormat());
        RageSpigot.INSTANCE.addMovementHandler(new CustomMovementHandler());

    }

    public void onDisable() {
        Faction.save();

        for (Player player : PlayerUtility.getOnlinePlayers()) {
            Profile profile = Profile.getByPlayer(player);

            if (profile.getClaimProfile() != null) {
                profile.getClaimProfile().removePillars();
            }

            for (ClaimPillar claimPillar : profile.getMapPillars()) {
                claimPillar.remove();
            }

            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        try {
            SimpleOfflinePlayer.save(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Profile profile : Profile.getProfiles()) {
            profile.save();
        }

        for (Mode mode : Mode.getModes()) {
            mode.save();
        }

        for (CombatLogger logger : CombatLogger.getLoggers()) {
            logger.getEntity().remove();
        }

        for (Event event : EventManager.getInstance().getEvents()) {
            if (event instanceof KothEvent) {
                ((KothEvent) event).save();
            } else if (event instanceof GlowstoneEvent) {
                ((GlowstoneEvent) event).save();
            }
        }


        factionsDatabase.getClient().close();
    }

    private void registerRecipes() {
        for (Material material : Material.values()) {
            if (material.name().contains("CHESTPLATE") || material.name().contains("SWORD") || material.name().contains("LEGGINGS") || material.name().contains("BOOTS") || material.name().contains("HELMET") || material.name().contains("AXE") || material.name().contains("SPADE")) {
                for (ItemDye dye : ItemDye.values()) {
                    Bukkit.addRecipe(ItemDye.getRecipe(material, dye));
                }
            }
        }

        Bukkit.addRecipe(new ShapelessRecipe(new ItemStack(Material.EXP_BOTTLE)).addIngredient(1, Material.GLASS_BOTTLE));
    }

    private void registerCommands() {
        new ProfileProtectionCommand();
        new ProfileOreCommand();
        new CloneInventoryCommand();
        new LastInventoryCommand();
        new GiveInventoryCommand();
        new DeathLookupCommand();
        new ProfileKitCommand();
        new KillStreakCommand();
        new CombatLoggerCommand();
        new ModeCommand();
        new KothCommand();
        new KothScheduleCommand();
        new KothCreateProcedureCommand();
        new KothRemoveCommand();
        new KothStartCommand();
        new KothStopCommand();
        new GlowstoneProcedureCommand();
        new GlowstoneRemoveCommand();
        new GlowstoneForceCommand();
        new BalanceCommand();
        new ReclaimCommand();
        new ReclaimRemoveCommand();
        new StackCommand();
        new CobbleCommand();
        new PayCommand();
        new SetBalanceCommand();
        new AddBalanceCommand();
        new HelpCommand();
        new SpawnCommand();
        new ProfileOptionsCommand();
        new MapKitCommand();
        new RenameCommand();
        new PlayTimeCommand();
        new TellLocationCommand();
        new FocusCommand();
        new SetGKitCommand();
        new MedicReviveCommand();
        new FactionHelpCommand();
        new FactionDisbandCommand();
        new FactionCreateCommand();
        new FactionDisbandAllCommand();
        new FactionInviteCommand();
        new FactionJoinCommand();
        new FactionRenameCommand();
        new FactionPromoteCommand();
        new FactionDemoteCommand();
        new FactionLeaderCommand();
        new FactionUninviteCommand();
        new FactionChatCommand();
        new FactionSetHomeCommand();
        new FactionMessageCommand();
        new FactionAnnouncementCommand();
        new FactionLeaveCommand();
        new FactionShowCommand();
        new FactionKickCommand();
        new FactionInvitesCommand();
        new FactionDepositCommand();
        new FactionWithdrawCommand();
        new FactionClaimCommand();
        new FactionMapCommand();
        new FactionUnclaimCommand();
        new FactionListCommand();
        new FactionHomeCommand();
        new FactionStuckCommand();
        new FactionCreateSystemCommand();
        new FactionToggleDeathbanCommand();
        new FactionColorCommand();
        new FactionFreezeCommand();
        new FactionThawCommand();
        new FactionSetDtrCommand();
        new FactionAdminCommand();
        new FixCommand();

        if (this.mainConfig.getBoolean("FACTION_GENERAL.ALLIES.ENABLED")) {
            new FactionAllyCommand();
        }

        if (mainConfig.getBoolean("FACTION_GENERAL.ALLIES.ENABLED")) {
            new FactionEnemyCommand();
        }

        if (kitmapMode) {
            new ChestCommand();
        }
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ProfileListeners(this), this);
        pluginManager.registerEvents(new CrowbarListeners(), this);
        pluginManager.registerEvents(new EconomySignListeners(), this);
        pluginManager.registerEvents(new StatTrackerListeners(), this);
        pluginManager.registerEvents(new ProfileCooldownListeners(), this);
        pluginManager.registerEvents(new ProfileKitActionListeners(), this);
        pluginManager.registerEvents(new ClaimWallListeners(this), this);
        pluginManager.registerEvents(new EnchantmentLimiterListeners(), this);
        pluginManager.registerEvents(new PotionLimiterListeners(), this);
        pluginManager.registerEvents(new DeathLookupListeners(), this);
        pluginManager.registerEvents(new CombatLoggerListeners(this), this);
        pluginManager.registerEvents(new BlockOperationModifierListeners(), this);
        pluginManager.registerEvents(new KothCreateProcedureListeners(), this);
        pluginManager.registerEvents(new GlowstoneCreateProcedureListeners(), this);
        pluginManager.registerEvents(new KothEventListeners(), this);
        pluginManager.registerEvents(new GlowstoneEventListeners(), this);
        pluginManager.registerEvents(new SubclaimListeners(), this);
        pluginManager.registerEvents(new ItemDyeListeners(), this);
        pluginManager.registerEvents(new GlitchListeners(), this);
        pluginManager.registerEvents(new ModeListeners(), this);
        pluginManager.registerEvents(new ScoreboardListeners(), this);
        pluginManager.registerEvents(new ClaimListeners(), this);
        pluginManager.registerEvents(new BorderListener(), this);
        pluginManager.registerEvents(new EnderpearlListener(this), this);
        pluginManager.registerEvents(new StrengthListener(), this);
        pluginManager.registerEvents(new DurabilityListener(), this);
        pluginManager.registerEvents(new KitmapSigns(this), this);
    }

}
