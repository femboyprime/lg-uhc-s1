package org.uhc2;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.uhc2.commands.*;
import org.uhc2.events.eventsManager;
import org.uhc2.enums.states;
import org.uhc2.enums.roles;

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

    public String _cyan = "§b";
    public String _gold = "§6";
    public String _bold = "§l";
    public String _gray = "§8";
    public String _red = "§c";
    public String _green = "§a";
    public String _dred = "§4";
    public String _dgreen = "§2";

    // strings
    public String uhctag_error = (_red+"UHC "+_gray+"| "+_dred);
    public String uhctag_valid = (_green+"UHC "+_gray+"| "+_dgreen);
    public String gameTag_Show = (_cyan+"["+_gold+_bold+"LOUPS-GAROUS"+_cyan+"]" + _text);
    public String gameTag_Public = (gameTag_Show + ": ");
    public String gameTag_Prive = (gameTag_Show + " [Privé]: ");

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
    public HashMap<Player, Joueur> playerJoueur = new HashMap<>();
    public HashMap<Joueur, Player> joueurPlayer = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup events
        eventsManager.registerEvents(this);

        // Plugin startup vars
        state = states.WAITING;

        // register commands
        this.getCommand("deleteworld").setExecutor(new deleteworld(this));
        this.getCommand("createworld").setExecutor(new createworld(this));
        this.getCommand("tpworld").setExecutor(new tpworld(this));

        this.getCommand("startuhc").setExecutor(new startuhc(this));
        this.getCommand("stopuhc").setExecutor(new stopuhc(this));
        this.getCommand("enduhc").setExecutor(new enduhc(this));

        this.getCommand("compo").setExecutor(new compo(this));

        this.getCommand("heal").setExecutor(new heal(this));
        this.getCommand("feed").setExecutor(new feed(this));
        this.getCommand("test").setExecutor(new test(this));

        this.getCommand("lg").setExecutor(new lg(this));
        this.getCommand("gm").setExecutor(new gm(this));

        // les descriptions
        roles.Petite_Fille.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Vous disposez de l'effet " + _nv + "Night Vision" + _text + " en permanence, ainsi que des effets " + _inv + "Invisibility I" + _text + " et " + _wk + "Weakness I" + _text + " la nuit. Vous disposez également de 2 potions de " + _sp + "Speed I" + _text + ". Au crépuscule et au milieu de la nuit, vous connaîtrez les pseudos des joueurs situés dans un rayon de 100 blocks autour de vous.");
        roles.Sorciere.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Pour ce faire, vous disposez de 3 potions splash d'"+_ih+"Instant Health I"+_text+", d'une potion splash de "+_rg+"Regeneration I"+_text+" et de 3 potions splash d'"+_id+"Instant Damage I"+_text+". Vous avez le pouvoir de ressuciter un joueur une fois dans la partie, à l'aide de la commande "+_cmd+"/lg sauver <pseudo>"+_text+".");
        roles.Voyante_Bavarde.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Vous disposez de l'effet " + _nv + "Night Vision" + _text + ", de 4 bibliothèques et de 4 blocks d'obsidienne. A chaque début de journée, vous pourrez connaître le rôle d'un joueur à l'aide de la commande "+_cmd+"/lg voir <pseudo>"+_text+".");
        roles.Chasseur.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Pour ce faire, vous disposez d'un livre Power IV, 128 flèches, 3 oeufs de loup et de 15 os. A votre mort, vous pourrez tirer sur une personne pour lui faire perdre la moitié de sa vie effective avec la commande "+_cmd+"/lg tirer <pseudo>"+_text+".");
        roles.Salvateur.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Pour ce faire, vous disposez de 2 potions splash d'"+_ih+"Instant Health I"+_text+". A chaque début de journée, vous pouvez choisir un joueur à qui vous conférez "+_nf+"NoFall"+_text+" et "+_res+"Resistance"+_text+" jusqu'a que vous changez de joueur avec la commande "+_cmd+"/lg salvater <pseudo>"+_text+".");
        roles.Ancien.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Vous disposez de l'effet "+_res+"Resistance I"+_text+" et d'une canne à peche enchanté Luck of the Sea V. A votre mort, si vous êtes tué par un "+_loupgarou+"Loup-Garou"+_text+", vous ressucitez, mais vous perdez l'effet "+_res+"Résistance"+_text+", aussi non, vous mourez, et votre tueur perdra la moitié de sa vie effective.");
        roles.Pyromane.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Vous disposez de l'effet "+_fres+"Fire Resistance"+_text+" en permanence, ainsi qu'un livre "+_fres+"Flame I"+_text+", un livre "+_fres+"Fire Aspect I"+_text+", 2 sceaux de lave et d'un briquet.");
        roles.Chaman.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". A chaque mort, le joueur mort pourra vous envoyer un message §4anonyme"+_text+" que seul vous pouvez voir.");
        roles.Simple_Villageois.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Vous ne disposez d'aucun pouvoirs à votre disposition. §4(petite merde :p)");

        roles.Enfant_Sauvage.setDescription(_text + "Votre objectif est de gagner avec les " + _villageois + "Villageois" + _text + ". Vous choisissez un modèle parmi les joueurs (commande : " + _cmd + " /lg choisir" + _text + "). Si celui-ci meurt, vous devenez un " + _loupgarou + "Loup-Garou" + _text + " et devez gagner avec eux.");

        roles.LG_Simple.setDescription(_text + "Votre objectif est de gagner avec les " + _loupgarou + "Loups-Garous" + _text + ". Pour ce faire, vous disposez des effets " + _fr + "Strength I" + _text + " et " + _nv + "Night Vision" + _text + ". A chaque kill, vous gagnez 1 minute de "+_sp+"Speed" + _text + " et 4 coeurs d'"+_abs+"Absorption" + _text + " pendant 4 minutes.");
        roles.LG_Blanc.setDescription(_text + "Votre objectif est de gagner " + _solo + "seul" + _text + ". Pour ce faire, vous disposez des effets " + _fr + "Strength I" + _text + " et " + _nv + "Night Vision" + _text + ", ainsi que d'une deuxième barre de vie. A chaque kill, vous gagnez 1 minute de "+_sp+"Speed" + _text + " et 4 coeurs d'"+_abs+"Absorption" + _text + " pendant 4 minutes.");
    }

    @Override
    public void onDisable() {}
}
