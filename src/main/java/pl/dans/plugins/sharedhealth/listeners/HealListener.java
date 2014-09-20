/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dans.plugins.sharedhealth.listeners;

import pl.dans.plugins.sharedhealth.SharedHealth;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scoreboard.Team;

/**
 * @author Dans
 */
public class HealListener implements Listener {
    
    private final SharedHealth sharedHealth;
    
    public HealListener(SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void gainHealth(final EntityRegainHealthEvent event) {
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
        if (team != null && team.getSize() > 1) {
            
            double divider = team.getSize();
            double gain = event.getAmount() / divider;
            for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                Player teammate = Bukkit.getPlayer(offlinePlayer.getName());
                
                if (teammate != null) {
                    teammate.setHealth(teammate.getHealth() + gain);
                } else {
                    sharedHealth.setPlayersDamageBalance(offlinePlayer.getName(), gain);
                }
            }
            
            event.setCancelled(true);
            
        }
    }
    
}
