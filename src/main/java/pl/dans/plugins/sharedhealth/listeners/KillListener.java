/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dans.plugins.sharedhealth.listeners;

import pl.dans.plugins.sharedhealth.SharedHealth;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team;

/**
 * @author Bergasms
 * @author Dans
 */
public class KillListener implements Listener {

    private final SharedHealth sharedHealth;

    public KillListener(SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKill(final PlayerDeathEvent event) {
        if (!sharedHealth.isRunning()) {
            return;
        }
        
        

        Player player = event.getEntity();
        
        Team team = player.getScoreboard().getPlayerTeam(player);
        
        if (team != null) {
            team.removePlayer(player);
        }
    }
}
