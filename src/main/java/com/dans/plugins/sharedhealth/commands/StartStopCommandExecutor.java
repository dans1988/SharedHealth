/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dans.plugins.sharedhealth.commands;

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
        if(command.getName().compareToIgnoreCase("startGame") == 0) {
            
            sharedHealth.setRunning(true);
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "SharedHealth started!");
            
        } else if (command.getName().compareToIgnoreCase("stopGame") == 0) {
            
            sharedHealth.setRunning(false);
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "SharedHealth stopped!");
        }
        
        
        return true;
    }
    
}
