/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dans.plugins.sharedhealth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.dans.plugins.sharedhealth.SharedHealth;

/**
 *
 * @author Dans
 */
public class PlayerJoinListener implements Listener {

    private final SharedHealth sharedHealth;

    public PlayerJoinListener(SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {

        if (!sharedHealth.isRunning()) {
            return;
        }

        Player player = event.getPlayer();

        double balance = sharedHealth.getPlayersDamageBalance(player.getName());
        

        if (balance != 0.0D) {
            
            double newHealth = (player.getHealth() + balance > 0.0D) ? player.getHealth() + balance : 0.0D;
            
            player.setHealth(newHealth);
            sharedHealth.resetPlayersDamageBalance(player.getName());
        }

    }
}
