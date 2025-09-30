package org.uhc2.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Uhc2;

import java.io.File;

public class deleteworld implements CommandExecutor {
    private final Uhc2 main;
    public deleteworld(Uhc2 main) { this.main = main; }

    private boolean deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorld(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }

        return path.delete(); // delete the root folder
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("§cUsage: /createworld <worldname>");
                return true;
            }

            String worldName = args[0];
            World world = Bukkit.getWorld(worldName);

            // on decharge le monde
            if (world != null) {
                for (Player p : world.getPlayers()) {
                    p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                }

                Bukkit.unloadWorld(world, false);
            }

            // si le folder n'existe pas
            File worldFolder = new File(Bukkit.getServer().getWorldContainer(), worldName);
            if (!worldFolder.exists()) {
                player.sendMessage(main.uhctag_error + "le dossier du monde §e" + worldName + "§4 n'existe pas");
                return true;
            }

            // et on supprime le folder (si possible)
            if (deleteWorld(worldFolder)) {
                player.sendMessage(main.uhctag_valid + "le monde §e" + worldName + "§2 a bien été supprimé.");
            } else {
                player.sendMessage(main.uhctag_error + "erreur pendant la suppression du monde " + worldName + "§4.");
            }

            return true;
        }

        return true;
    }
}
