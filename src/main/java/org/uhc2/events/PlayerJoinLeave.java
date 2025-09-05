package org.uhc2.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;
import org.uhc2.enums.roles;

import java.util.UUID;

public class PlayerJoinLeave implements Listener {
    private final Uhc2 main;

    public PlayerJoinLeave(Uhc2 main) { this.main = main; }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        boolean isPlaying = main.utils.isPlayerJoueur(player);

        if (isPlaying) {
            Joueur joueur = main.utils.getJoueur(player);
            if (joueur.getRole() != roles.LG_Blanc) {
                player.setMaxHealth(20);
            }

            joueur.setUUID(player.getUniqueId());
            joueur.setPlayer();
            joueur.setName();
        } else {
            player.setMaxHealth(20);
        }

        event.setJoinMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "+" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.DARK_AQUA + " a rejoint le serveur! " + ChatColor.WHITE + "(" + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + ChatColor.WHITE + "/" + ChatColor.AQUA + Bukkit.getMaxPlayers() + ChatColor.WHITE + ")");
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(ChatColor.AQUA + "[" + ChatColor.RED + "-" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.DARK_AQUA + " a quitté le serveur! " + ChatColor.WHITE + "(" + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + ChatColor.WHITE + "/" + ChatColor.AQUA + Bukkit.getMaxPlayers() + ChatColor.WHITE + ")");
    }
}
