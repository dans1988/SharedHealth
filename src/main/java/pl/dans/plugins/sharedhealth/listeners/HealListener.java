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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void gainHealth(final EntityRegainHealthEvent event) {
        if (!sharedHealth.isRunning()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();

        Team team = p.getScoreboard().getPlayerTeam(p);
        if (team != null && team.getSize() > 1) {

            double divider = team.getSize();

            double amount = event.getAmount();
            double gain = amount / divider;

            for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                Player teammate = Bukkit.getPlayer(offlinePlayer.getUniqueId());

                if (teammate != null) {
                    double teammateHealth = teammate.getHealth();
                    double finalHealth = teammateHealth + gain;
                    
                    if (finalHealth > teammate.getMaxHealth()) {
                        finalHealth = teammate.getMaxHealth();
                    }
                    
                    teammate.setHealth(finalHealth);
                } else {

                    sharedHealth.setPlayersDamageBalance(offlinePlayer.getName(), gain);
                }
            }

            event.setCancelled(true);

        }
    }

}
