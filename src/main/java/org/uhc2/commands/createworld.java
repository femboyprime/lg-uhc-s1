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

/*
TODO:
- only one biome (roofed)
- custom world border size
*/

public class createworld implements CommandExecutor {
    private final Uhc2 main;
    public createworld(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("§cUsage: /createworld <worldname>");
                return true;
            }

            String worldName = args[0];

            // le monde existe déjà
            if (Bukkit.getWorld(worldName) != null) {
                player.sendMessage(main.uhctag_error + "§e" + worldName + " §4existe déjà ou es en cours d'utilisation.");
                return true;
            }

            player.sendMessage(main.uhctag_valid + "creation du monde §e" + worldName + "§2.");

            WorldCreator worldCreator = new WorldCreator(worldName);
            World world = worldCreator.createWorld();

            if (world == null) {
                player.sendMessage(main.uhctag_error + "erreur pendant la creation du monde."); // on envoie si ya une erreur
                return true;
            } else {
                player.sendMessage(main.uhctag_valid + "le monde §e" + worldName + "§2 a bien été crée."); // aussinon on continue
            }


            // world spawn
            world.setSpawnFlags(true, true);
            world.setSpawnLocation(0, 100, 0); // alors euh, je sais pas comment mais trust bro (logique: new Location(0, 100, 0) mais bon)

            // world border
            WorldBorder worldBorder = world.getWorldBorder();
            worldBorder.setCenter(world.getSpawnLocation()); // le centre de la bordure au (0, 0)
            worldBorder.setSize(100); // j'dois faire size*2 si j'veux la distance entre 0 et la bordure a size (oui c'est des maths)

            return true;
        }

        return true;
    }
}
