package org.uhc2.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;
import org.uhc2.enums.roles;

public class PlayerDeath implements Listener {
    private final Uhc2 main;
    public PlayerDeath(Uhc2 main) { this.main = main; }

    // color strings
    public String _text = "§9";
    public String _loupgarou = "§c";
    public String _villageois = "§a";

    public String _res = "§7";

    private void _respawn(Player player, Joueur joueurMort) {
        if (joueurMort.getRole() == roles.Ancien) {
            joueurMort.sendMessage("Vous avez été tué par un "+_loupgarou+"Loup-Garou"+_text+"! Vous ressuscitez mais perdez votre effet de "+_res+"résistance"+_text+".");
        }

        Location respawnLocation = new Location(player.getWorld(), -874.5, 19, -384.5); // MAKE RANDOM
        joueurMort.hasRespawned = true;

        player.spigot().respawn();
        player.teleport(respawnLocation);
    }

    private void _kill(Player player, Joueur joueurMort) {
        joueurMort.setMort(true); // CIAAAAAAAAOOOOOO !!!

        if (player.getKiller() != null && player.getKiller().getType() == EntityType.PLAYER && main.utils.isPlayerJoueur(player.getKiller())) {
            Player killer = player.getKiller();
            Joueur joueurKiller = main.utils.getJoueur(killer);

            if (joueurKiller.getUUID().toString().equals(killer.getUniqueId().toString()) && joueurKiller.isLoup_Effect() ) {
                main.pouvoirs.giveEffects(joueurKiller, main.pouvoirs.lg_kill_effects);
            }
        }

        int jrs = 0;
        for (Joueur joueur : main.joueurPlayer.keySet()) {
            if (joueur.isAlive()) {
                jrs = jrs + 1;

                // nique sa race x)
                joueur.getScoreboard().setLine(7, ChatColor.RED + "" + jrs + ChatColor.DARK_RED + " Joueurs");
            }
        }

        main.utils.sendMessageToAll(new String[]{"§c=========☠==========","§2Le village a perdu un de ses membres : " + joueurMort.getRole().getStrColor() + "§l" + joueurMort.getName() + "§r§2, qui était " + joueurMort.getRole().getStrColor() + "§o" + joueurMort.getRole().getName() + "§r§2.", "§c===================="});

    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        boolean isPlaying = main.utils.isPlayerJoueur(player);

        if (isPlaying && main.episodeInt >= 2) {
            event.setDeathMessage(null);

            Joueur joueurMort = main.utils.getJoueur(player);

            // l'ancien est mort
            if (joueurMort.getRole() == roles.Ancien && !joueurMort.hasRespawned) {
                // l'ancien se fait tuer par qqn
                if (player.getKiller() != null && player.getKiller().getType() == EntityType.PLAYER && main.utils.isPlayerJoueur(player.getKiller())) {
                    Player killer = player.getKiller();
                    Joueur joueurKiller = main.utils.getJoueur(killer);

                    // si l'ancien est mort par un lg
                    if (joueurKiller.isLoup_Kill()) {
                        event.setKeepInventory(true);
                        _respawn(player, joueurMort);
                    } else {
                        // aussinon x) (sur les prochaines saisons -> check si le type est solo ou non)
                        killer.setHealth((killer.getHealth() / 2));
                        joueurKiller.sendMessage("Vous avez tué l'"+_villageois+"Ancien"+_text+", vous perdez donc la moitié de votre vie effective.");

                        event.setKeepInventory(false);
                        _kill(player, joueurMort);
                    }
                } else {
                    event.setKeepInventory(false);
                    _kill(player, joueurMort);
                }
            } else {
                event.setKeepInventory(false);
                _kill(player, joueurMort);
            }
        } else {
            event.setDeathMessage("§b[§c☠§b] §3§l" + player.getName() + "§r§3 est mort.");
            event.setKeepInventory(true);
        }

    }
}
