package org.uhc2.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Uhc2;

public class tpworld implements CommandExecutor {
    private final Uhc2 main;
    public tpworld(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                String worldName = args[0];
                World otherWorld = player.getWorld();
                World world = Bukkit.getWorld(worldName);

                // si le monde n'existe pas
                if (world == null) {
                    player.sendMessage(main.uhctag_error + "§b" + worldName + "§4 n'existe pas.");
                    return true;
                }

                // on teleport le player au monde voulu
                player.teleport(world.getSpawnLocation());
                player.sendMessage(main.uhctag_valid + "teleportation dans le monde §e" + worldName + "§2.");

                player.sendMessage("isSame: " + (world == otherWorld));

                // on décharge l'ancien monde (alors pas sur si ya tjrs qqn dedans mais bon azy)
                if (world != otherWorld) {
                    if (Bukkit.unloadWorld(otherWorld, true)) {
                        player.sendMessage(main.uhctag_valid + "l'ancien monde a bien été décharger.");
                    } else {
                        player.sendMessage(main.uhctag_error + "l'ancien monde n'a pas pu être décharger.");
                    }
                }

            } else {
                player.sendMessage(main.uhctag_error + "le nom du monde n'a pas été precisé.");
            }

            return true;
        }

        return true;
    }
}
