package org.uhc2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.uhc2.Joueur;
import org.uhc2.Uhc2;
import org.uhc2.enums.roles;

public class lg implements CommandExecutor {
    private final Uhc2 main;
    public lg(Uhc2 main) { this.main = main; }

    //-// all
    // color strings
    public String _text = "§9";
    public String _villageois = "§a";
    public String _loupgarou = "§c";
    public String _solo = "§6";
    public String _cmd = "§f";

    public String _nv = "§1";
    public String _fr = "§c";
    public String _sp = "§f";
    public String _abs = "§e";
    public String _inv = "§7";
    public String _wk = "§8";
    public String _ih = "§d";
    public String _id = "§4";
    public String _rg = "§5";
    public String _res = "§7";
    public String _nf = "§7";
    public String _fres = "§6";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Joueur joueur = main.utils.getJoueur(player);

            if (args.length >= 1) {
                String sousCommande = args[0];

                if (sousCommande.equalsIgnoreCase("voir")) {
                    if (args.length >= 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);

                        if (target != null && main.utils.isPlayerJoueur(target)) {
                            Joueur targetJoueur = main.utils.getJoueur(target);

                            if (targetJoueur != null && joueur.getRole() == roles.Voyante_Bavarde) {
                                if (!joueur.hasSeen) {
                                    joueur.hasSeen = true;

                                    main.utils.sendMessageToAll(main.gameTag_Public + "La " + _villageois + "Voyante Bavarde" + _text + " a espionné un joueur et son rôle est : " + targetJoueur.getRole().getStrColor() + targetJoueur.getRole().getName() + _text + ".");
                                } else {
                                    joueur.sendMessage(main.gameTag_Prive + "Vous avez déjà regardé le rôle d'un joueur cette épisode.");
                                }

                            }
                        }
                    }
                } else if (sousCommande.equalsIgnoreCase("salvater")) {
                    if (args.length >= 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);

                        if (target != null && main.utils.isPlayerJoueur(target)) {
                            Joueur targetJoueur = main.utils.getJoueur(target);

                            if (targetJoueur != null && joueur.getRole() == roles.Salvateur) {
                                if (!joueur.hasProtected) {
                                    if (joueur.lastProtected != target) {
                                        joueur.hasProtected = true;
                                        joueur.lastProtected = target;
                                        targetJoueur.isProtected = true;

                                        PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (main.timeForEpisode / 50), 0, false, false);
                                        main.pouvoirs.giveEffect(targetJoueur, resistance);

                                        targetJoueur.sendMessage(main.gameTag_Prive + "Le "+_villageois+"Salvateur"+_text+" vous a salvaté! Vous disposez de l'effet "+_res+"Resistance"+_text+" ainsi que "+_res+"NoFall"+_text+" pendant cette épisode.");

                                    } else {
                                        joueur.sendMessage(main.gameTag_Prive + "Vous avez déjà salvaté le joueur l'épisode d'avant.");
                                    }
                                } else {
                                    joueur.sendMessage(main.gameTag_Prive + "Vous avez déjà salvaté un joueur cette épisode.");
                                }

                            }
                        }
                    }
                } else if (sousCommande.equalsIgnoreCase("me") || sousCommande.equalsIgnoreCase("role")) {
                    if (joueur != null && joueur.getRole() != null) {
                        // envoie les messages
                        joueur.sendMessage(new String[]{main.gameTag_Prive + "Vous êtes " + joueur.getRole().getStrColor() + joueur.getRole().getName() + _text + ".", joueur.getRole().getDescription()});
                    } else {
                        if (main.episodeInt < 2) {
                            player.sendMessage(main.gameTag_Prive + "Les rôles ne sont pas encore distribuer.");
                        } else {
                            player.sendMessage(main.gameTag_Prive + "Vous ne disposez pas de rôle.");
                        }

                    }
                }


            }
        }

        return true;
    }
}
