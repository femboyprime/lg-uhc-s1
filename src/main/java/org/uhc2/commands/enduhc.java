package org.uhc2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.uhc2.Uhc2;
import org.uhc2.enums.states;
import org.uhc2.returnTypes.winResult;

public class enduhc implements CommandExecutor {
    private final Uhc2 main;
    public enduhc(Uhc2 main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (main.state == states.WAITING || main.state == states.PREGAME) {
                player.sendMessage(main.gameTag_Public + "l'UHC n'a pas encore commencé / n'a pas encore attribué les rôles.");
            } else {
                winResult result = main.utils.endGame();

                player.sendMessage("isWin: " + result.isWin());
                player.sendMessage("winMessage: " + result.getWinMessage());
                player.sendMessage("camp: " + result.getCamp().toString());
            }

            return true;
        }

        return true;
    }
}
