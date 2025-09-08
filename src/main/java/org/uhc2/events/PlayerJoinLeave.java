package org.uhc2.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;
import org.uhc2.enums.roles;
import org.uhc2.scoreboard.ScoreboardSign;

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

            // scoreboardsign sauvegarde pas -> create a chaque fois x)
            joueur.getScoreboard().destroy();
            ScoreboardSign scoreboard = main.utils.makeScoreboard(player);
            joueur.setScoreboard(scoreboard);

            // on set les nouveaux trucs (tu connais)
            joueur.setUUID(player.getUniqueId());
            joueur.setPlayer();
            joueur.setName();

        } else {
            // 10c
            player.setMaxHealth(20);

            // no effect :)
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }

        event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "»" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW + ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.WHITE + " (" + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + ChatColor.WHITE + "/" + ChatColor.RED + Bukkit.getMaxPlayers() + ChatColor.WHITE + ")");
    }

    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "«" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW + ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.WHITE + " (" + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + ChatColor.WHITE + "/" + ChatColor.RED + Bukkit.getMaxPlayers() + ChatColor.WHITE + ")");
    }
}
