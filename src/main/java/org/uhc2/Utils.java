package org.uhc2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.uhc2.enums.camps;
import org.uhc2.enums.roles;
import org.uhc2.enums.states;

import org.uhc2.scoreboard.ScoreboardSign;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Utils {
    private final Uhc2 main;
    public Utils(Uhc2 main) {this.main = main;}

    // functions
    private void startGame() {
        main.state = states.PREGAME;

        for (Player player : getServer().getOnlinePlayers()) {
            Joueur joueur = new Joueur(player, player.getUniqueId(), this.main);
            ScoreboardSign scoreboard = makeScoreboard(player);
            main.joueursScoreboard.put(scoreboard, joueur);
        }

        main.mainTimer = new Timer();
        main.mainTimerTask = new TimerTask() {
            @Override
            public void run() {
                main.mainTimerInt = main.mainTimerInt + 1;

                for (ScoreboardSign scoreboard : main.joueursScoreboard.keySet()) {
                    scoreboard.setLine(9, ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + String.format("%02d:%02d", main.mainTimerInt / 60, main.mainTimerInt % 60));
                }
            }
        };

        main.mainTimer.scheduleAtFixedRate(main.mainTimerTask, Calendar.getInstance().getTime(), 1000);

        main.cycleTimer = new Timer();
        main.cycleTimerTask = new TimerTask() {
            final World world = (World) Bukkit.getServer().getWorld("world");

            @Override
            public void run() {
                main.cycle = !main.cycle;

                if (main.cycle) {
                    for (ScoreboardSign scoreboard : main.joueursScoreboard.keySet()) {
                        // set scoreboard
                        scoreboard.setLine(10, ChatColor.GOLD + "Cycle: " + ChatColor.YELLOW + "Jour");

                        // le ingame time
                        if (world != null) {
                            world.setTime(6000);
                        }

                        // effets de jours
                        for (Joueur joueur : main.joueurPlayer.keySet()) {
                            main.pouvoirs.giveDay(joueur);
                        }

                    }

                } else {
                    for (ScoreboardSign scoreboard : main.joueursScoreboard.keySet()) {
                        // set scoreboard
                        scoreboard.setLine(10, ChatColor.GOLD + "Cycle: " + ChatColor.YELLOW + "Nuit");

                        // le ingame time
                        if (world != null) {
                            world.setTime(18000);
                        }

                        // effets de nuits
                        for (Joueur joueur : main.joueurPlayer.keySet()) {
                            main.pouvoirs.giveNight(joueur);
                        }

                    }
                }
            }
        };

        main.cycleTimer.scheduleAtFixedRate(main.cycleTimerTask, Calendar.getInstance().getTime(), main.timeForCycle);

        main.episodeTimer = new Timer();
        main.episodeTimerTask = new TimerTask() {
            @Override
            public void run() {
                if ( !(main.episodeInt == 0) ) {
                    sendMessageToAll(ChatColor.AQUA + "-------- Fin Episode " + main.episodeInt + " --------");
                }

                main.episodeInt = main.episodeInt + 1;

                if (main.episodeInt == 2) {
                    main.state = states.GAME;
                    giveRoles();
                }

                for (ScoreboardSign scoreboard : main.joueursScoreboard.keySet()) {
                    scoreboard.setLine(7, ChatColor.AQUA + "Episode " + ChatColor.GOLD + main.episodeInt);
                }
            }
        };

        main.episodeTimer.scheduleAtFixedRate(main.episodeTimerTask, Calendar.getInstance().getTime(), main.timeForEpisode); // 1200000 = 20 min
    }

    public void stopGame() {
        main.state = states.WAITING;

        main.episodeInt = 0;
        main.mainTimerInt = 0;
        main.cycle = false;

        sendTitleToAll("§cArrêt de l'§6UHC§c.", "§4le /stopuhc");
        sendMessageToAll("§cArrêt de l'§6UHC§c.");

        main.mainTimer.cancel();
        main.episodeTimer.cancel();
        main.cycleTimer.cancel();

        for (ScoreboardSign scoreboard : main.joueursScoreboard.keySet()) {
            scoreboard.destroy();
        }

        for (Joueur joueur : main.joueurPlayer.keySet()) {
            joueur.getPlayer().setMaxHealth(20);

            for (PotionEffect effect : joueur.getPlayer().getActivePotionEffects()) {
                joueur.getPlayer().removePotionEffect(effect.getType());
            }
        }

        main.joueursScoreboard.clear();
        main.joueurPlayer.clear();
        main.playerJoueur.clear();
    }

    public void giveRoles() {
        sendMessageToAll(main.gameTag_Public + "Attribution des rôles.");

        Random rng = new Random();
        for (roles role : roles.values()) {

            Joueur joueurToGive = main.playerJoueur.values().stream()
                    .skip(rng.nextInt(main.playerJoueur.size()))
                    .findFirst().get();

            if (role.number > 0 && joueurToGive.role == null) {
                role.number = role.number - 1;

                // set role & camp
                joueurToGive.setRole(role);
                joueurToGive.setCamp(role.camp);

                // give effects
                main.pouvoirs.givePermanent(joueurToGive);
                main.pouvoirs.giveOneTime(joueurToGive);

                // petit sounds
                if (role.camp == camps.Village) {
                    playSound(joueurToGive, Sound.VILLAGER_IDLE);
                } else if (role.camp == camps.LoupGarou) {
                    playSound(joueurToGive, Sound.WOLF_GROWL);
                } else if (role.camp == camps.Neutre) {
                    playSound(joueurToGive, Sound.ENDERDRAGON_GROWL);
                }

                // envoie les messages
                joueurToGive.sendMessage(new String[]{main.gameTag_Prive + "Vous êtes " + role.strcolor + role.nom + "§9.", role.description});
            }


        }


    }

    @SuppressWarnings("deprecation")
    public void sendTitleToAll(String title, String subtitle) {
        for (Player player1 : getServer().getOnlinePlayers()) {
            player1.sendTitle(title, subtitle);
        }
    }

    public void sendMessageToAll(String message) {
        for (Player player1: getServer().getOnlinePlayers()) {
            player1.sendMessage(message);
        }
    }

    public void sendMessageToAll(String[] messages) {
        for (Player player1: getServer().getOnlinePlayers()) {
            player1.sendMessage(messages);
        }
    }

    public void playSoundToAll(Sound soundPlayed) {
        for (Player player1: getServer().getOnlinePlayers()) {
            Bukkit.getScheduler().runTask(this.main, () -> player1.getWorld().playSound(player1.getLocation(), soundPlayed, 1, 1));
        }
    }

    public void playSound(Joueur joueur, Sound soundPlayed) {
        Player player = joueur.getPlayer();

        Bukkit.getScheduler().runTask(this.main, () -> player.getWorld().playSound(player.getLocation(), soundPlayed, 1, 1));
    }

    public void playSound(Player player, Sound soundPlayed) {
        Bukkit.getScheduler().runTask(this.main, () -> player.getWorld().playSound(player.getLocation(), soundPlayed, 1, 1));
    }

    public void timerPause() {
        sendTitleToAll("§cArrêt de l'§6UHC§c.", "§4le /stopuhc");
        sendMessageToAll("§cArrêt de l'§6UHC§c.");

        main.state = states.WAITING;
        main.timer.cancel();
    }

    public void timerResume() {
        int nombreRoles = 0;
        for (roles role : roles.values()) {
            nombreRoles = nombreRoles + role.number;
        }

        sendMessageToAll("nombreRoles: " + nombreRoles + " // getOnlinePlayers(): " + Bukkit.getOnlinePlayers().size());

        if (nombreRoles != Bukkit.getOnlinePlayers().size()) {
            if (nombreRoles > Bukkit.getOnlinePlayers().size()) {
                sendMessageToAll(main.gameTag_Public + "Il n'y a pas assez de §fjoueurs§9 pour commencer la partie.");
            } else {
                sendMessageToAll(main.gameTag_Public + "Il n'y a pas assez de §frôles§9 pour commencer la partie.");
            }

            main.state = states.WAITING;
            return;
        }

        main.state = states.STARTING;
        main.timer = new Timer();
        main.timerTask = new TimerTask() {
            int counter = 10; // le temps avant que l'UHC se lance.

            @Override
            public void run() {
                playSoundToAll(Sound.ORB_PICKUP);

                if (counter > 0) {
                    // le counter n'est pas a 0.
                    sendTitleToAll("§eLançement de l'§n§6UHC§r§e.", ("§a" + counter + "s"));

                    counter = counter - 1;
                } else {
                    // le counter est a 0.
                    sendTitleToAll("§eBon jeux!", "§aGL §2HF§a!");

                    startGame();
                    main.timer.cancel();
                }

            }

        };

        main.timer.scheduleAtFixedRate(main.timerTask, Calendar.getInstance().getTime(), 1000);
    }

    public boolean isPlayerJoueur(Player player) {
        return (getJoueur(player) != null);
    }

    public Joueur getJoueur(Player player) {
        return main.playerJoueur.get(player);
    }

    private ScoreboardSign makeScoreboard(Player player) {
        ScoreboardSign scoreboard = new ScoreboardSign(player, "§l§nLG UHC S1");
        scoreboard.create();

        scoreboard.setLine(5," ");
        scoreboard.setLine(6, ChatColor.AQUA + "Episode " + ChatColor.GOLD + "1");
        scoreboard.setLine(7, ChatColor.RED + "##" + ChatColor.DARK_RED + " Joueurs");
        scoreboard.setLine(8, "  ");
        scoreboard.setLine(9, ChatColor.GOLD + "Timer: " + ChatColor.YELLOW + "00:00");
        scoreboard.setLine(10, ChatColor.GOLD + "Cycle: " + ChatColor.YELLOW + "Nuit");
        scoreboard.setLine(11, "   ");
        scoreboard.setLine(12, ChatColor.DARK_GREEN + "Border: " + ChatColor.GREEN + "####");
        scoreboard.setLine(13, "    ");
        scoreboard.setLine(14,  ChatColor.UNDERLINE + "@femboysanslimite");

        return scoreboard;
    }
}
