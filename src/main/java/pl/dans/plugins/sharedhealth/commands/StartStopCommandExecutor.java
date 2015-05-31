package pl.dans.plugins.sharedhealth.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pl.dans.plugins.sharedhealth.SharedHealth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dans
 */
public class StartStopCommandExecutor implements CommandExecutor {

    private final SharedHealth sharedHealth;

    public StartStopCommandExecutor(SharedHealth sharedHealth) {
        this.sharedHealth = sharedHealth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().compareToIgnoreCase("shstart") == 0) {

            if (sharedHealth.isRunning()) {
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "SharedHealth is already running!");
            } else {

                sharedHealth.setRunning(true);
                sharedHealth.resetDamageBalance();
                sharedHealth.resetSharedDamage();
                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "SharedHealth activated!");
            }

        } else if (command.getName().compareToIgnoreCase("shstop") == 0) {

            if (!sharedHealth.isRunning()) {
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "SharedHealth is not running!");
            } else {

                sharedHealth.setRunning(false);
                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "SharedHealth deactivated!");
            }
        }

        return true;
    }

}
