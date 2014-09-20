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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

/**
 * @author Dans
 */
public class DamageListener implements Listener {

    private final SharedHealth sharedHealth;

    private static final double ADDED_HEALTH = 200D;
    
    private static final int DAMAGE_TASK_DELAY = 1;

    public DamageListener(final SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(final EntityDamageEvent event) {

        if (!sharedHealth.isRunning()) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if ((event.getCause() == EntityDamageEvent.DamageCause.LAVA)
                || (event.getCause() == EntityDamageEvent.DamageCause.FIRE)
                ||  (event.getCause() == EntityDamageEvent.DamageCause.POISON)) {

            return;
        }

        final Player player = (Player) event.getEntity();

        final Team team = player.getScoreboard().getPlayerTeam(player);

        if (team == null || team.getSize() <= 1) {
            return;
        }

        
        
        final double initialMaxHealth = player.getMaxHealth();
        final double initialHealth = player.getHealth();

        player.setMaxHealth(player.getMaxHealth() + ADDED_HEALTH);
        player.setHealth(player.getHealth() + ADDED_HEALTH);

        final double raisedInitialHealth = player.getHealth();
        
        new BukkitRunnable() {

            @Override
            public void run() {
                double finalHealth = player.getHealth();
                double difference = raisedInitialHealth - finalHealth;
                
                double divider = team.getSize();
                double damage = difference / divider;
                
                if (initialMaxHealth - damage > 0) {
                    player.setHealth(initialHealth - damage);
                    
                } else {
                    player.setHealth(0);
                }
                player.setMaxHealth(initialMaxHealth);
                
                for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                    Player teammate = Bukkit.getPlayer(offlinePlayer.getName());
                    
                    if (offlinePlayer.getName().equals(player.getName())) {
                        continue;
                    }

                    if (teammate != null) {
                        teammate.damage(damage);
                    } else if (teammate == null) {
                        
                        sharedHealth.setPlayersDamageBalance(offlinePlayer.getName(), new Double(damage * (-1.0D)));
                        
                    }
                }

            }
        }.runTaskLater(sharedHealth, DAMAGE_TASK_DELAY);
        
    }
}
