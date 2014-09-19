package pl.dans.plugins.sharedhealth;

import com.dans.plugins.sharedhealth.commands.StartStopCommandExecutor;
import com.dans.plugins.sharedhealth.listeners.DamageListener;
import com.dans.plugins.sharedhealth.listeners.HealListener;
import com.dans.plugins.sharedhealth.listeners.KillListener;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class SharedHealth extends JavaPlugin
{
    private boolean running;
    
    
    
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "{0}onEnable", ChatColor.RED);
        
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new KillListener(this), this);
        getServer().getPluginManager().registerEvents(new HealListener(this), this);
        
        StartStopCommandExecutor startStopCommandExecutor = new StartStopCommandExecutor(this);
        
        getCommand("startGame").setExecutor(startStopCommandExecutor);
        getCommand("stopGame").setExecutor(startStopCommandExecutor);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}onDisable", ChatColor.RED);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    
    
}
