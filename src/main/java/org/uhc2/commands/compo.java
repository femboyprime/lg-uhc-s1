package org.uhc2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Uhc2;
import org.uhc2.enums.states;

public class compo implements CommandExecutor {
    private final Uhc2 main;
    public compo(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            main.composition.entityOpenInventory(player);
        }

        return true;
    }
}
