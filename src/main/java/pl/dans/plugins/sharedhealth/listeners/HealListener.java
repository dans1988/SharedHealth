/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dans.plugins.sharedhealth.listeners;

import pl.dans.plugins.sharedhealth.SharedHealth;
import com.dans.plugins.sharedhealth.helpers.SharedHealthUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scoreboard.Team;

/**
 * @author Bergasms
 * @author Dans
 */
public class HealListener implements Listener {

    private final SharedHealth sharedHealth;

    public HealListener(SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void gainHealth(EntityRegainHealthEvent event) {
        if (!sharedHealth.isRunning()) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();

        Team team = p.getScoreboard().getPlayerTeam(p);
        if (team != null) {

            List<String> playersNames = new ArrayList<String>();

            for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                playersNames.add(offlinePlayer.getName());
            }

            double damage = event.getAmount();

            List<Player> plist = SharedHealthUtils.orderedPlayerListForNames(playersNames);
            while (damage > 0) {
                if (!plist.isEmpty()) {
                    Player pl = null;
                    for (Player a : plist) {
                        if (pl == null) {
                            pl = a;
                        } else if (pl.getHealth() > a.getHealth()) {
                            pl = a;
                        }
                    }
                    if (pl != null) {
                        pl.setHealth(pl.getHealth() + 1);
                    }
                }
                damage--;
            }
            event.setCancelled(true);
        }

    }

    
}
