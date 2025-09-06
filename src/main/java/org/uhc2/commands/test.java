package org.uhc2.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;

public class test implements CommandExecutor {
    private final Uhc2 main;
    public test(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!main.utils.isPlayerJoueur(player)) {
                player.sendMessage(ChatColor.RED + "rien à test, sorry ! -femboyprime"); //
            } else {
                Joueur joueur = main.utils.getJoueur(player);

                main.pouvoirs.giveEffect(joueur, main.pouvoirs.lg_speed);
                main.pouvoirs.giveEffect(joueur, main.pouvoirs.lg_abso);
            }
        }



        return true;
    }
}
