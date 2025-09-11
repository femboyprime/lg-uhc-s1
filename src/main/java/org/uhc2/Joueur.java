package org.uhc2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.uhc2.enums.camps;
import org.uhc2.enums.roles;
import org.uhc2.scoreboard.ScoreboardSign;

import java.util.UUID;

public class Joueur {
    // private stuff
    private final Uhc2 main;

    // default stuff
    private Player player;
    private String username;
    private UUID uuid;
    private boolean mort = false;
    private ScoreboardSign scoreboard = null;

    // lg global stuff
    private camps camp;
    private roles role = null;
    private Joueur couple = null;

    public boolean canVote = true;
    public Joueur votedFor;

    //-// role stuff
    // sorciere
    public boolean hasRevived = false;
    // vb
    public boolean hasSeen = false;
    // chasseur
    public boolean hasFired = false;
    // salva
    public boolean hasProtected = false;
    public boolean isProtected = false;
    public Player lastProtected = null;
    // ancien
    public boolean hasRespawned = false;
    // chaman
    public boolean isChaman = false;
    // es
    public boolean isTransfo = false;

    //--// MAIN
    public Joueur(Player player, UUID uuid, Uhc2 main) {
        // setup this
        this.uuid = uuid;
        this.player = player;

        // server stuff
        setName();

        // setup main
        this.main = main;
    }
    //-// MAIN

    // "set" methods
    public void setPlayer() {
        player = Bukkit.getPlayer(uuid);
    }

    public void setName() {
        if (player == null) { setPlayer(); }

        username = player.getName();
    }

    public void setRole(roles role) {
        this.role = role;

        if (role == roles.Chaman) { isChaman = true; }
    }

    public void setCamp(camps camp) {
        this.camp = camp;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setScoreboard(ScoreboardSign scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setMort(boolean bool) {
        this.mort = bool;
    }

    // "get" methods
    public Player getPlayer() { return player; }
    public String getName() { return username; }
    public roles getRole() { return role; }
    public camps getCamp() { return camp; }
    public UUID getUUID() { return uuid; }
    public ScoreboardSign getScoreboard() { return scoreboard; }

    // quicker methods
    public void sendMessage(String message) {
        getPlayer().sendMessage(main.gameTag_Prive + message);
    }

    public void sendMessage(String[] messages) {
        getPlayer().sendMessage(messages);
    }

    // "is" methods
    public boolean isOwner() {
        return (getUUID().toString().equals("0fc289a2-8dda-429a-b727-7f1e9811d747"));
    }

    public boolean isCouple() {return hasCouple(); }
    public boolean isDead() {return (!isAlive()); }
    public boolean isAlive() { return (!mort); }
    public boolean isLoup_Effect() {
        if (getRole()  == roles.LG_Blanc) {
            return true;
        } else if (getRole()  == roles.LG_Simple) {
            return true;
        } else if (getRole() == roles.Enfant_Sauvage && isTransfo) {
            return true;
        } else if (getCamp() == camps.LoupGarou) { //-// omg l'infection (ya pas en saison 1 ...)
            return true;
        } else {
            return false;
        }
    }

    public boolean isLoup_Kill() {
        if (getRole()  == roles.LG_Blanc) {
            return true;
        } else if (getRole()  == roles.LG_Simple) {
            return true;
        } else if (getRole() == roles.Enfant_Sauvage && isTransfo) {
            return true;
        } else if (getCamp() == camps.LoupGarou) {
            return true;
        } else {
            return false;
        }
    }

    // "has" methods
    public boolean hasCouple() {
        return (couple != null);
    }
}
