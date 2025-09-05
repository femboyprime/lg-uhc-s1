package org.uhc2.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;

public class PlayerFallDamage implements Listener {
    private final Uhc2 main;
    public PlayerFallDamage(Uhc2 main) { this.main = main; }

    @EventHandler
    public void OnFall(EntityDamageEvent event) {
        // Fall check
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        // Player check
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getEntity();

        // Joueur check
        if (!main.utils.isPlayerJoueur(player)) {
            return;
        }

        Joueur joueur = main.utils.getJoueur(player);

        // isProtected check
        if (joueur.isProtected) {
            event.setCancelled(true);
        }

    }
}
