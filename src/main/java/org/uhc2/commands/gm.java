package org.uhc2.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Uhc2;

public class gm implements CommandExecutor {
    private final Uhc2 main;
    public gm(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                String gamemodeAsked = args[0];

                //noinspection IfCanBeSwitch
                if (gamemodeAsked.equals("1") || gamemodeAsked.equals("c")) {
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(main.gameTag_Prive + "Set gamemode to §cCREATIVE§9.");
                } else if (gamemodeAsked.equals("0") || gamemodeAsked.equals("s")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(main.gameTag_Prive + "Set gamemode to §cSURVIVAL§9.");
                } else if (gamemodeAsked.equals("2") || gamemodeAsked.equals("a")) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(main.gameTag_Prive + "Set gamemode to §cADVENTURE§9.");
                } else if (gamemodeAsked.equals("3") || gamemodeAsked.equals("sp")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(main.gameTag_Prive + "Set gamemode to §cSPECTATOR§9.");
                } else {
                    player.sendMessage(main.gameTag_Prive + "Le gamemode n'existe pas.");
                }

            } else {
                player.sendMessage(main.gameTag_Prive + "Pas de gamemode donné.");
            }
        }

        return true;
    }
}
