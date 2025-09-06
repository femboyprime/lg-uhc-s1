package org.uhc2.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;
import org.uhc2.enums.states;

public class startuhc implements CommandExecutor {
    private final Uhc2 main;
    public startuhc(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if ( !(main.state == states.WAITING) ) {
                player.sendMessage(main.gameTag_Public + "l'UHC a déjà commencé.");
                return true;
            }

            main.utils.timerResume();
        }

        return true;
    }
}
