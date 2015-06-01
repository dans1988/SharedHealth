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
        double teamSize = team.getSize();
        double sharedDamage = damage / teamSize;

        for (OfflinePlayer teammate : team.getPlayers()) {
            final Player onlineTeammate = Bukkit.getServer().getPlayer(teammate.getUniqueId());

            if (onlineTeammate != null) {
                double currentHealth = onlineTeammate.getHealth();
                double finalHealth = currentHealth - sharedDamage;

                if (finalHealth < 0.0D) {
                    finalHealth = 0.0D;
                }

                final double finalHealthAsynch = finalHealth;

                //player who took damage will be handled differently
                if (!teammate.getUniqueId().equals(player.getUniqueId())) {

                    sharedHealth.setSharedDamage(onlineTeammate.getName(), true);
                    onlineTeammate.damage(0.0D);
                    onlineTeammate.setHealth(finalHealthAsynch);

                }

            } else {
                sharedHealth.setPlayersDamageBalance(teammate.getName(), sharedDamage * -1.0D);
                sharedHealth.setSharedDamage(teammate.getName(), true);
            }
        }

        double currentHealth = player.getHealth();

        double finalHealth = currentHealth - sharedDamage;

        if (finalHealth < 0.0D) {
            finalHealth = 0;
        }

        //apply damage later to avoid reduction from enchants if player wouldn't die
        //or kill immediately if he is supposed to die anyway
        //cancelling event would prevent knockback, delay damage until after the event instead
        if (finalHealth > 0) {

            final double finalHealthAsynch = finalHealth;

            event.setDamage(0.0D);

            new BukkitRunnable() {

                @Override
                public void run() {
                    player.setHealth(finalHealthAsynch);
                }
            }.runTaskLater(sharedHealth, 1);

        } else {
            event.setDamage(event.getDamage() * 1000);
        }

    }
}
