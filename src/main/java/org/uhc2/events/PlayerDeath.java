package org.uhc2.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;
import org.uhc2.enums.roles;

import java.util.UUID;

public class PlayerDeath implements Listener {
    private final Uhc2 main;
    public PlayerDeath(Uhc2 main) { this.main = main; }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        boolean isPlaying = main.utils.isPlayerJoueur(player);

        if (isPlaying && main.episodeInt >= 2) {
            event.setDeathMessage(null);
            event.setKeepInventory(false);

            Joueur joueurMort = main.utils.getJoueur(player);

            if (joueurMort.getRole() == roles.Ancien && !joueurMort.hasRespawned) {
                joueurMort.hasRespawned = true;
            } else {
                joueurMort.setMort(true); // CIAAAAAAAAOOOOOO !!!

                if (player.getKiller() != null && player.getKiller().getType() == EntityType.PLAYER && main.utils.isPlayerJoueur(player.getKiller())) {
                    Player killer = player.getPlayer();
                    UUID killerUUID = killer.getUniqueId();
                    Joueur joueurKiller = main.utils.getJoueur(killer);

                    if (joueurKiller.getUUID().toString().equals(killerUUID.toString()) && joueurKiller.isLoup_Effect() ) {
                        main.utils.sendMessageToAll("GAVE OUT lg_kill_effects !!");
                        main.pouvoirs.giveEffects(joueurKiller, main.pouvoirs.lg_kill_effects);
                    }
                }

                int jrs = 0;
                for (Joueur joueur : main.joueurPlayer.keySet()) {
                    if (!joueur.mort) {
                        jrs = jrs + 1;

                        // nique sa race x)
                        joueur.getScoreboard().setLine(7, ChatColor.RED + "" + jrs + ChatColor.DARK_RED + " Joueurs");
                    }
                }

                main.utils.sendMessageToAll(new String[]{"§c=========☠==========","§2Le village a perdu un de ses membres : " + joueurMort.getRole().getStrColor() + "§l" + joueurMort.getName() + "§r§2, qui était " + joueurMort.getRole().getStrColor() + "§o" + joueurMort.getRole().getName() + "§r§2.", "§c===================="});
            }
        } else {
            event.setDeathMessage("§b[§c☠§b] §3§l" + player.getName() + "§r§3 est mort.");
            event.setKeepInventory(false);
        }

    }
}
