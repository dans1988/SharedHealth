/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dans.plugins.sharedhealth.helpers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Bergasms +34 619 24 09 38
 * @author Dans
 */
public class SharedHealthUtils {

    public static final List<Player> orderedPlayerListForNames(List<String> playersNames) {
        ArrayList<Player> outlist = new ArrayList();
        for (String name : playersNames) {
            Player p = Bukkit.getServer().getPlayer(name);
            if (p != null) {
                int insertindex = 0;
                for (int i = 0; i < outlist.size(); i++) {
                    if (((Player) outlist.get(i)).getHealth() < p.getHealth()) {
                        insertindex = i;
                        i = outlist.size();
                    }
                }
                outlist.add(insertindex, p);
            }
        }
        return outlist;
    }

    public static double getArmourMultiplier(EntityDamageEvent event, LivingEntity living) {
        Player player = null;
        if ((living instanceof Player)) {
            player = (Player) living;
        }
        if (player == null) {
            return 1.0D;
        }
        if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                && (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)
                && (event.getCause() != EntityDamageEvent.DamageCause.FIRE)
                && (event.getCause() != EntityDamageEvent.DamageCause.LAVA)
                && (event.getCause() != EntityDamageEvent.DamageCause.CONTACT)
                && (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            return 1.0D;
        }
        ItemStack boots = player.getInventory().getBoots();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        double reduction = 0.0D;
        if (helmet != null) {
            if (helmet.getType() == Material.LEATHER_HELMET) {
                reduction += 0.04D;
            } else if (helmet.getType() == Material.GOLD_HELMET) {
                reduction += 0.08D;
            } else if (helmet.getType() == Material.CHAINMAIL_HELMET) {
                reduction += 0.08D;
            } else if (helmet.getType() == Material.IRON_HELMET) {
                reduction += 0.08D;
            } else if (helmet.getType() == Material.DIAMOND_HELMET) {
                reduction += 0.12D;
            }
        }
        if (boots != null) {
            if (boots.getType() == Material.LEATHER_BOOTS) {
                reduction += 0.04D;
            } else if (boots.getType() == Material.GOLD_BOOTS) {
                reduction += 0.04D;
            } else if (boots.getType() == Material.CHAINMAIL_BOOTS) {
                reduction += 0.04D;
            } else if (boots.getType() == Material.IRON_BOOTS) {
                reduction += 0.08D;
            } else if (boots.getType() == Material.DIAMOND_BOOTS) {
                reduction += 0.12D;
            }
        }
        if (leggings != null) {
            if (leggings.getType() == Material.LEATHER_LEGGINGS) {
                reduction += 0.08D;
            } else if (leggings.getType() == Material.GOLD_LEGGINGS) {
                reduction += 0.12D;
            } else if (leggings.getType() == Material.CHAINMAIL_LEGGINGS) {
                reduction += 0.16D;
            } else if (leggings.getType() == Material.IRON_LEGGINGS) {
                reduction += 0.2D;
            } else if (leggings.getType() == Material.DIAMOND_LEGGINGS) {
                reduction += 0.24D;
            }
        }
        if (chestplate != null) {
            if (chestplate.getType() == Material.LEATHER_CHESTPLATE) {
                reduction += 0.12D;
            } else if (chestplate.getType() == Material.GOLD_CHESTPLATE) {
                reduction += 0.2D;
            } else if (chestplate.getType() == Material.CHAINMAIL_CHESTPLATE) {
                reduction += 0.2D;
            } else if (chestplate.getType() == Material.IRON_CHESTPLATE) {
                reduction += 0.24D;
            } else if (chestplate.getType() == Material.DIAMOND_CHESTPLATE) {
                reduction += 0.32D;
            }
        }
        return 1.0D - reduction;
    }
}
