package pl.dans.plugins.sharedhealth;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.dans.plugins.sharedhealth.commands.StartStopCommandExecutor;
import pl.dans.plugins.sharedhealth.listeners.DamageListener;
import pl.dans.plugins.sharedhealth.listeners.HealListener;
import pl.dans.plugins.sharedhealth.listeners.KillListener;
import pl.dans.plugins.sharedhealth.listeners.PlayerJoinListener;

/**
 * Hello world!
 *
 */
public class SharedHealth extends JavaPlugin
{
    private boolean running;
    
    private Map<String, Double> damageBalance;
    
    
    
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "{0}onEnable", ChatColor.RED);
        
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new KillListener(this), this);
        getServer().getPluginManager().registerEvents(new HealListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        StartStopCommandExecutor startStopCommandExecutor = new StartStopCommandExecutor(this);
        
        getCommand("startGame").setExecutor(startStopCommandExecutor);
        getCommand("stopGame").setExecutor(startStopCommandExecutor);
        
        damageBalance = new HashMap<String, Double>();
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

    public Double getPlayersDamageBalance(String player) {
        if (damageBalance.containsKey(player)) {
            return damageBalance.get(player);
        } else {
            return 0.0D;
        }
    }

    public void setPlayersDamageBalance(String player, Double balance) {
        
        if (!damageBalance.containsKey(player)) {
            damageBalance.put(player, balance);
        } else {
            Double previousBalance = damageBalance.get(player);
            damageBalance.put(player, previousBalance + balance);
        }
    }
    
    public void resetDamageBalance() {
        this.damageBalance = new HashMap<String, Double>();
    }
    
    public void resetPlayersDamageBalance(String player) {
        if (damageBalance.containsKey(player)) {
            damageBalance.put(player, 0.0D);
        }
    }
    
    
    
}
