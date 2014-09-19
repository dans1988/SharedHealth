/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dans.plugins.sharedhealth.listeners;

import pl.dans.plugins.sharedhealth.SharedHealth;
import com.dans.plugins.sharedhealth.helpers.SharedHealthUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scoreboard.Team;

/**
 * @author Bergasms
 * @author Dans
 */
public class DamageListener implements Listener {

    private final SharedHealth sharedHealth;

    public DamageListener(SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {

        if (!sharedHealth.isRunning()) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        
        if ((event.getCause() != EntityDamageEvent.DamageCause.LAVA) && (event.getCause() != EntityDamageEvent.DamageCause.FIRE)) {
            Player p = (Player) event.getEntity();

            Team team = p.getScoreboard().getPlayerTeam(p);
            if (team != null) {

                List<String> playersNames = new ArrayList<String>();

                for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                    playersNames.add(offlinePlayer.getName());
                }

                double damage = event.getDamage();

                
                List<Player> plist = SharedHealthUtils.orderedPlayerListForNames(playersNames);
                Map<String, Double> damageMap = new HashMap<String, Double>();
                for (Player plp : plist) {
                    damageMap.put(plp.getName(), plp.getHealth());
                }
                double curhealth;
                while (damage > 0) {
                    String most = null;
                    curhealth = 0;
                    for (String key : damageMap.keySet()) {
                        double val =  damageMap.get(key);
                        if (most == null) {
                            most = key;
                            curhealth = val;
                        } else if (val > curhealth) {
                            most = key;
                            curhealth = val;
                        }
                    }
                    
                    Random random = new Random(new Date().getTime());
                    
                    if (most != null) {
                        double threshold = (1.0D - SharedHealthUtils.getArmourMultiplier(event, p)) * 100.0D;
                        if (random.nextInt(10) < 4) {
                            threshold = 0.0D;
                        }
                        if (random.nextInt(100) > threshold) {
                            damageMap.put(most, damageMap.get(most) - 1.0D);
                        }
                    }
                    damage--;
                }
                for (String key : damageMap.keySet()) {
                    
                    if (!p.getName().equals(key)) {
                        Player thep = Bukkit.getServer().getPlayer(key);
                        thep.damage(thep.getHealth() - damageMap.get(key));
                    }
                }
                
                event.setDamage(p.getHealth() - damageMap.get(p.getName()));

                //event.setCancelled(true);
            }

        }
    }
}
