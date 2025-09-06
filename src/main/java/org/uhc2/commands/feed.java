package org.uhc2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Uhc2;

public class feed implements CommandExecutor {
    private final Uhc2 main;
    public feed(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                String playerName = args[0];
                Player fedPlayer = Bukkit.getPlayer(playerName);

                if (fedPlayer == null) {
                    player.sendMessage(main.gameTag_Public + "Le joueur '§b" + playerName + "'§9 n'existe pas.");
                    return true;
                }

                fedPlayer.setFoodLevel(20);
                player.sendMessage(main.gameTag_Public + "Successfully fed '§b" +  playerName + "'§9.");

                if (! (fedPlayer == player)) {
                    fedPlayer.sendMessage(main.gameTag_Prive + "You were fed by '§b" +  player.getName() + "§9'.");
                }
            } else {
                player.setFoodLevel(20);
                player.sendMessage(main.gameTag_Prive + "Fed yourself.");
            }
        }

        return true;
    }
}
