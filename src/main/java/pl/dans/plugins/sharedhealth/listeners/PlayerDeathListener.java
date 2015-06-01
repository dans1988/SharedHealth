package pl.dans.plugins.sharedhealth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team;
import pl.dans.plugins.sharedhealth.SharedHealth;

/**
 *
 * @author Dans
 */
public class PlayerDeathListener implements Listener {

    private final SharedHealth sharedHealth;

    public PlayerDeathListener(SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {

        if (!sharedHealth.isRunning()) {
            return;
        }

        String playerName = event.getEntity().getName();
        
        String playerDisplayName = event.getEntity().getDisplayName();
        
        

        if (sharedHealth.getSharedDamage().get(playerName) != null && sharedHealth.getSharedDamage().get(playerName) == true) {
            event.setDeathMessage(playerDisplayName + ChatColor.WHITE + " died from sharing health");
        }

        Player player = event.getEntity();

        Team team = player.getScoreboard().getPlayerTeam(player);

        if (team != null) {
            team.removePlayer(player);
        }
    }

}
