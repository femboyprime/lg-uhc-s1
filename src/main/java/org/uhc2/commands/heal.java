package org.uhc2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Uhc2;
import org.uhc2.enums.states;

public class heal implements CommandExecutor {
    private final Uhc2 main;
    public heal(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                String playerName = args[0];
                Player healedPlayer = Bukkit.getPlayer(playerName);

                if (healedPlayer == null) {
                    player.sendMessage(main.gameTag_Public + "Le joueur '§b" + playerName + "'§9 n'existe pas.");
                    return true;
                }

                healedPlayer.setHealth(healedPlayer.getMaxHealth());
                player.sendMessage(main.gameTag_Public + "Successfully healed '§b" +  playerName + "'§9.");

                if (! (healedPlayer == player)) {
                    healedPlayer.sendMessage(main.gameTag_Prive + "You were healed by '§b" +  player.getName() + "§9'.");
                }
            } else {
                player.setHealth(player.getMaxHealth());
                player.sendMessage(main.gameTag_Prive + "Healed yourself.");
            }
        }

        return true;
    }
}
