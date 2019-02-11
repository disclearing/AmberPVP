package com.amberpvp.hcfactions.misc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;

public class StrengthListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player) {
            Player p = (Player)e.getDamager();
            if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                Iterator var3 = p.getActivePotionEffects().iterator();

                while(var3.hasNext()) {
                    PotionEffect effect = (PotionEffect)var3.next();
                    if(effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                        int level = effect.getAmplifier() + 1;
                        double newDamage = e.getDamage(EntityDamageEvent.DamageModifier.BASE) / ((double)level * 1.60D + 1.1D) * 1.7D;
                        double damagePercent = newDamage / e.getDamage(EntityDamageEvent.DamageModifier.BASE);

                        try {
                            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, e.getDamage(EntityDamageEvent.DamageModifier.ARMOR) * damagePercent);
                        } catch (Exception var14) {
                            var14.printStackTrace();
                        }

                        try {
                            e.setDamage(EntityDamageEvent.DamageModifier.MAGIC, e.getDamage(EntityDamageEvent.DamageModifier.MAGIC) * damagePercent);
                        } catch (Exception var13) {
                            var13.printStackTrace();
                        }

                        try {
                            e.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, e.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) * damagePercent);
                        } catch (Exception var12) {
                            var12.printStackTrace();
                        }

                        if(e.getEntity() instanceof Player) {
                            try {
                                e.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, e.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) * damagePercent);
                            } catch (Exception var11) {
                                var11.printStackTrace();
                            }
                        }

                        e.setDamage(EntityDamageEvent.DamageModifier.BASE, newDamage);
                        break;
                    }
                }
            }
        }

    }
}

