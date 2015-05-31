package pl.dans.plugins.sharedhealth.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class DamageListener implements Listener {

    private final SharedHealth sharedHealth;

    public DamageListener(final SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(final EntityDamageEvent event) {

        if (!sharedHealth.isRunning()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final Player player = (Player) event.getEntity();
        

        sharedHealth.setSharedDamage(player.getName(), false);
        
        

        if ((event.getCause() == EntityDamageEvent.DamageCause.LAVA)
                || (event.getCause() == EntityDamageEvent.DamageCause.FIRE)
                || (event.getCause() == EntityDamageEvent.DamageCause.POISON)) {

            return;
        }

        final Team team = player.getScoreboard().getPlayerTeam(player);

        if (team == null || team.getSize() <= 1) {
            return;
        }
        

        double damage = event.getFinalDamage();
        double teamSize =team.getSize();
        double sharedDamage = damage / teamSize;

        for (OfflinePlayer teammate : team.getPlayers()) {
            Player onlineTeammate = Bukkit.getServer().getPlayer(teammate.getUniqueId());
            
            
            if (onlineTeammate != null) {
                double currentHealth = onlineTeammate.getHealth();
                double finalHealth = currentHealth - sharedDamage;
                

                if (finalHealth < 0.0D) {
                    finalHealth = 0.0D;
                }

                //double finalHealth = (currentHealth - sharedDamage > 0.0D) ? currentHealth - sharedDamage : 0.0D;
                //player who took damage will be handled differently
                if (!teammate.getUniqueId().equals(player.getUniqueId())) {
                    onlineTeammate.setHealth(finalHealth);
                    onlineTeammate.damage(0);
                    sharedHealth.setSharedDamage(onlineTeammate.getName(), true);
                }

            } else {
                sharedHealth.setPlayersDamageBalance(teammate.getName(), sharedDamage * -1.0D);
                sharedHealth.setSharedDamage(teammate.getName(), true);
            }
        }

        //don't modify the health of the player who took damage if he will die anyway
        //this prevents the death message
        double currentHealth = player.getHealth();

        double finalHealth = currentHealth - sharedDamage;

        if (finalHealth < 0.0D) {
            finalHealth = 0;
        }
        

        
        //apply damage later to avoid reduction from enchants if player wouldn't die
        //or kill immediately if he is supposed to die anyway
        if (finalHealth > 0) {
            
            player.setHealth(finalHealth);
            
            event.setCancelled(true); 
            
        } else {
            event.setDamage(event.getDamage() * 1000);
        }

    }
}
