package org.uhc2;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.uhc2.events.eventsManager;
import org.uhc2.enums.states;
import org.uhc2.enums.roles;
import org.uhc2.scoreboard.ScoreboardSign;

import java.util.*;

public final class Uhc2 extends JavaPlugin {
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

    // strings
    public String gameTag_Show = "§b[§6§lLOUPS-GAROUS§b]" + _text;
    public String gameTag_Public = gameTag_Show + ": ";
    public String gameTag_Prive = gameTag_Show + " [Privé]: ";

    // custom
    public states state = states.WAITING;

    //--// TIMERS
    public int timeForEpisode = (1_200_000 / 40); // 1_200_000 = 20 mns
    public int timeForCycle = (timeForEpisode / 2); // 10mns Jour / 10mns Nuit

    //-// pouvoirs.java
    // main
    public Pouvoirs pouvoirs = new Pouvoirs(this);

    //-// utils.java
    // booleans
    public boolean debugMessages = true;

    // timer stuff
    public Timer timer;
    public TimerTask timerTask;

    public int mainTimerInt = -1;
    public Timer mainTimer;
    public TimerTask mainTimerTask;

    public int episodeInt = 0;
    public Timer episodeTimer;
    public TimerTask episodeTimerTask;

    public boolean cycle = false; // true = Jour, false = Nuit
    public Timer cycleTimer;
    public TimerTask cycleTimerTask;

    // main
    public Utils utils = new Utils(this);

    //-// compo.java
    // lists
    public HashMap<Integer, roles> roleListInt = new HashMap<>();

    // main 
    public Compo composition = new Compo(this);

    //-// joueurs
    // public ArrayList<Joueur> joueurs = new ArrayList<>();
    public HashMap<Player, Joueur> playerJoueur = new HashMap<>();
    public HashMap<Joueur, Player> joueurPlayer = new HashMap<>();
    public HashMap<ScoreboardSign, Joueur> joueursScoreboard = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup events
        eventsManager.registerEvents(this);

        // Plugin startup vars
        state = states.WAITING;

        // les descriptions
        // roles.ROLE.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". ";

        roles.Petite_Fille.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Vous disposez de l'effet " + _nv + "Night Vision" + _text + " en permanence, ainsi que des effets " + _inv + "Invisibility I" + _text + " et " + _wk + "Weakness I" + _text + " la nuit. Vous disposez également de 2 potions de " + _sp + "Speed I" + _text + ". Au crépuscule et au milieu de la nuit, vous connaîtrez les pseudos des joueurs situés dans un rayon de 100 blocks autour de vous.";
        roles.Sorciere.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Pour ce faire, vous disposez de 3 potions splash d'"+_ih+"Instant Health I"+_text+", d'une potion splash de "+_rg+"Regeneration I"+_text+" et de 3 potions splash d'"+_id+"Instant Damage I"+_text+". Vous avez le pouvoir de ressuciter un joueur une fois dans la partie, à l'aide de la commande "+_cmd+"/lg sauver <pseudo>"+_text+".";
        roles.Voyante_Bavarde.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Vous disposez de l'effet " + _nv + "Night Vision" + _text + ", de 4 bibliothèques et de 4 blocks d'obsidienne. A chaque début de journée, vous pourrez connaître le rôle d'un joueur à l'aide de la commande "+_cmd+"/lg voir <pseudo>"+_text+".";
        roles.Chasseur.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Pour ce faire, vous disposez d'un livre Power IV, 128 flèches, 3 oeufs de loup et de 15 os. A votre mort, vous pourrez tirer sur une personne pour lui faire perdre la moitié de sa vie effective avec la commande "+_cmd+"/lg tirer <pseudo>"+_text+".";
        roles.Salvateur.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Pour ce faire, vous disposez de 2 potions splash d'"+_ih+"Instant Health I"+_text+". A chaque début de journée, vous pouvez choisir un joueur à qui vous conférez "+_nf+"NoFall"+_text+" et "+_res+"Resistance"+_text+" jusqu'a que vous changez de joueur avec la commande "+_cmd+"/lg salvater <pseudo>"+_text+".";
        roles.Ancien.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Vous disposez de l'effet "+_res+"Resistance I"+_text+" et d'une canne à peche enchanté Luck of the Sea V. A votre mort, si vous êtes tué par un "+_loupgarou+"Loup-Garou"+_text+", vous ressucitez, mais vous perdez l'effet "+_res+"Résistance"+_text+", aussi non, vous mourez, et votre tueur perdra la moitié de sa vie effective.";
        roles.Pyromane.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Vous disposez de l'effet "+_fres+"Fire Resistance"+_text+" en permanence, ainsi qu'un livre "+_fres+"Flame I"+_text+", un livre "+_fres+"Fire Aspect I"+_text+", 2 sceaux de lave et d'un briquet.";
        roles.Chaman.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". A chaque mort, le joueur mort pourra vous envoyer un message §4anonyme"+_text+" que seul vous pouvez voir.";
        roles.Simple_Villageois.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Vous ne disposez d'aucun pouvoirs à votre disposition. §4(petite merde :p)";

        roles.Enfant_Sauvage.description = _text + "Votre objectif est d'éliminer les " + _loupgarou + "Loups-Garous" + _text + ". Vous choisissez un modèle parmi les joueurs (commande : " + _cmd + " /lg choisir" + _text + "). Si celui-ci meurt, vous devenez un " + _loupgarou + "Loup-Garou" + _text + " et devez gagner avec eux.";

        roles.LG_Simple.description = _text + "Votre objectif est d'éliminer les " + _villageois + "Villageois" + _text + ". Pour ce faire, vous disposez des effets " + _fr + "Strenght I" + _text + " et " + _nv + "Night Vision" + _text + ". A chaque kill, vous gagnez 1 minute de "+_sp+"Speed" + _text + " et 4 coeurs d'"+_abs+"Absorption" + _text + " pendant 4 minutes.";
        roles.LG_Blanc.description = _text + "Votre objectif est de gagner " + _solo + "seul" + _text + ". Pour ce faire, vous disposez des effets " + _fr + "Strenght I" + _text + " et " + _nv + "Night Vision" + _text + ", ainsi que d'une deuxième barre de vie. A chaque kill, vous gagnez 1 minute de "+_sp+"Speed" + _text + " et 4 coeurs d'"+_abs+"Absorption" + _text + " pendant 4 minutes.";


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            boolean isOwner = player.getUniqueId().toString().equals("0fc289a2-8dda-429a-b727-7f1e9811d747");
            // boolean isHost = true;

            String commandName = command.getName();

            if (isOwner) {
                // les commandes pour les hosts / owner

                if (commandName.equalsIgnoreCase("gm")) {
                    try {
                        String gamemodeAsked = args[0];

                        //noinspection IfCanBeSwitch
                        if (gamemodeAsked.equals("1") || gamemodeAsked.equals("c")) {
                            player.setGameMode(GameMode.CREATIVE);
                            player.sendMessage("§9[§6§lUHC§9]: Set gamemode to §cCREATIVE§9.");
                        } else if (gamemodeAsked.equals("0") || gamemodeAsked.equals("s")) {
                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage("§9[§6§lUHC§9]: Set gamemode to §cSURVIVAL§9.");
                        } else if (gamemodeAsked.equals("2") || gamemodeAsked.equals("a")) {
                            player.setGameMode(GameMode.ADVENTURE);
                            player.sendMessage("§9[§6§lUHC§9]: Set gamemode to §cADVENTURE§9.");
                        } else if (gamemodeAsked.equals("3") || gamemodeAsked.equals("sp")) {
                            player.setGameMode(GameMode.SPECTATOR);
                            player.sendMessage("§9[§6§lUHC§9]: Set gamemode to §cSPECTATOR§9.");
                        }

                        return true;
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        player.sendMessage("§9[§6§lUHC§9]: Pas de gamemode donné.");
                        return true;
                    }

                } else if (commandName.equalsIgnoreCase("heal")) {
                    try {
                        String playerName = args[0];
                        Player player1 = Bukkit.getPlayer(playerName);

                        if (player1 == null) {
                            player.sendMessage(gameTag_Public + "Le joueur '§b" + playerName + "'§9 n'existe pas.");
                            return true;
                        }

                        player1.setHealth(player1.getMaxHealth());
                        player.sendMessage(gameTag_Public + "Successfully healed '§b" +  playerName + "'§9.");

                        if (! (player1 == player)) {
                            player1.sendMessage(gameTag_Prive + "You were healed by '§b" +  player.getName() + "§9'.");
                        }

                        return true;
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        player.setHealth(player.getMaxHealth());
                        player.sendMessage(gameTag_Public + "Healed yourself.");
                        return true;
                    }
                } else if (commandName.equalsIgnoreCase("feed")) {
                    try {
                        String playerName = args[0];
                        Player player1 = Bukkit.getPlayer(playerName);

                        if (player1 == null) {
                            player.sendMessage(gameTag_Public + "Le joueur '§b" + playerName + "§9' n'existe pas.");
                            return true;
                        }

                        player1.setFoodLevel(20);
                        player.sendMessage(gameTag_Public + "Successfully fed '§b" +  playerName + "§9'.");

                        if (! (player1 == player)) {
                            player1.sendMessage(gameTag_Prive + "§You were fed by '§b" +  player.getName() + "§9'.");
                        }

                        return true;
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        player.setFoodLevel(20);
                        player.sendMessage(gameTag_Public + "Fed yourself.");
                        return true;
                    }
                } else if (commandName.equalsIgnoreCase("startuhc")) {
                    if ( !(state == states.WAITING) ) {
                        player.sendMessage(gameTag_Public + "l'UHC a déjà commencé.");
                        return true;
                    }

                    utils.timerResume();
                    return true;
                } else if (commandName.equalsIgnoreCase("stopuhc")) {

                    if (state == states.WAITING) {
                        player.sendMessage(gameTag_Public + "l'UHC n'a pas encore commencé.");
                    } else if (state == states.STARTING) {
                        utils.timerPause();
                    } else {
                        utils.stopGame();
                    }

                    return true;
                    /*
                } else if (commandName.equalsIgnoreCase("addrole")) {

                    try {
                        String roleToAdd = args[0];

                        if (Arrays.asList(this.roles.rolesLG).contains(roleToAdd)) {
                            composition.roleList.add(roleToAdd);
                            player.sendMessage("§9[§6§lUHC§9]: Le rôle '§b" + roleToAdd + "§9' a bien été ajouté.");
                        } else {
                            player.sendMessage("§9[§6§lUHC§9]: Le rôle '§b" + roleToAdd + "§9' n'existe pas.");
                        }

                        return true;
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        player.sendMessage("§9[§6§lUHC§9]: Pas de rôle donné.");
                        return true;
                    }
                } else if (commandName.equalsIgnoreCase("removerole")) {
                    try {
                        String roleToAdd = args[0];

                        if (composition.roleList.contains(roleToAdd)) {
                            composition.roleList.remove(roleToAdd);
                            player.sendMessage("§9[§6§lUHC§9]: Le rôle '§b" + roleToAdd + "§9' a bien été enlevé.");
                        } else {
                            player.sendMessage("§9[§6§lUHC§9]: '§b" + roleToAdd + "§9' n'est pas dans la composition.");
                        }

                        return true;
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        player.sendMessage("§9[§6§lUHC§9]: Pas de rôle donné.");
                        return true;
                    }
                } else if (commandName.equalsIgnoreCase("getroles")) {
                    StringBuilder stringBuilder = new StringBuilder("§9[§6§lUHC§9]: Rôles present dans la parties: ");

                    for (String role: composition.roleList) {
                        stringBuilder.append(utils.getRoleColor(role)).append(role).append("§9, ");
                    }

                    String messageToSend = stringBuilder.substring(0, stringBuilder.toString().length() - 2); // -2 car on a ", " à la fin.

                    player.sendMessage(messageToSend + "§9.");
                    return true;

                     */
                } else if (commandName.equalsIgnoreCase("compo")) {
                    composition.entityOpenInventory(player);
                } else if (commandName.equalsIgnoreCase("test")) {
                    if (!utils.isPlayerJoueur(player)) {
                        player.sendMessage(ChatColor.RED + "rien à test, sorry ! -femboyprime"); //
                    } else {
                        Joueur joueur = utils.getJoueur(player);

                        pouvoirs.giveEffect(joueur, pouvoirs.lg_speed);
                        pouvoirs.giveEffect(joueur, pouvoirs.lg_abso);
                    }

                    return true;
                } else if (commandName.equalsIgnoreCase("lg")) {
                    Joueur joueur = utils.getJoueur(player);

                    try {
                        // main cmd
                        String sousCommande = args[0];
                        utils.sendMessageToAll("sousCommande: " + sousCommande);

                        if (sousCommande.equalsIgnoreCase("voir")) {
                            // voir cmd -> voyante_bavarde
                            try {
                                String playerName = args[1];
                                Player target = Bukkit.getPlayer(playerName);

                                if (target != null && utils.isPlayerJoueur(target)) {
                                    Joueur targetJoueur = utils.getJoueur(target);

                                    if (targetJoueur != null && joueur.getRole() == roles.Voyante_Bavarde) {
                                        if (!joueur.hasSeen) {
                                            joueur.hasSeen = true;

                                            utils.sendMessageToAll(gameTag_Public + "La " + _villageois + "Voyante Bavarde" + _text + " a espionné un joueur et son rôle est : " + targetJoueur.getRole().getStrColor() + targetJoueur.getRole().getName() + _text + ".");
                                        } else {
                                            joueur.sendMessage(gameTag_Prive + "Vous avez déjà regardé le rôle d'un joueur cette épisode.");
                                        }

                                    }
                                }
                            } catch (Exception ignored) {}

                            return true;
                        } else if (sousCommande.equalsIgnoreCase("salvater")) {
                            // salvater cmd -> salvateur
                            try {
                                String playerName = args[1];
                                Player target = Bukkit.getPlayer(playerName);

                                if (target != null && utils.isPlayerJoueur(target)) {
                                    Joueur targetJoueur = utils.getJoueur(target);

                                    if (targetJoueur != null && joueur.getRole() == roles.Salvateur) {
                                        if (!joueur.hasProtected) {
                                            if (joueur.lastProtected != target) {
                                                joueur.hasProtected = true;
                                                joueur.lastProtected = target;
                                                targetJoueur.isProtected = true;

                                                PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (timeForEpisode / 50), 0, false, false);
                                                pouvoirs.giveEffect(targetJoueur, resistance);

                                                targetJoueur.sendMessage(gameTag_Prive + "Le "+_villageois+"Salvateur"+_text+" vous a salvaté! Vous disposez de l'effet "+_res+"Resistance"+_text+" ainsi que "+_res+"NoFall"+_text+" pendant cette épisode.");

                                            } else {
                                                joueur.sendMessage(gameTag_Prive + "Vous avez déjà salvaté le joueur l'épisode d'avant.");
                                            }
                                        } else {
                                            joueur.sendMessage(gameTag_Prive + "Vous avez déjà salvaté un joueur cette épisode.");
                                        }

                                    }
                                }
                            } catch (Exception ignored) {}
                        }

                        return true;
                    } catch (Exception ignored) {}

                    return true;
                }

            } else {
                player.sendMessage(gameTag_Public + "You do §cnot have"+_text+" permission to run this command.");
                return true;

            }

        }
        return true; // j'ai oublié :(
    }

}
