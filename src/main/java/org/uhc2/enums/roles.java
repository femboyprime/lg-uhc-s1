package org.uhc2.enums;

public enum roles {
    // https://minecraft.fandom.com/fr/wiki/Loup-Garou_UHC
    Petite_Fille("Petite File",             camps.Village, 0, "§a", 10),
    Sorciere("Sorciere",                    camps.Village, 0, "§a", 10),
    Voyante_Bavarde("Voyante Bavarde",      camps.Village, 0, "§a", 10),
    Chasseur("Chasseur",                    camps.Village, 0, "§a", 10),
    Salvateur("Salvateur",                  camps.Village, 0, "§a", 10),
    Ancien("Ancien",                        camps.Village, 1, "§a", 10),
    Pyromane("Pyromane",                    camps.Village, 0, "§a", 10),
    Chaman("Chaman",                        camps.Village, 0, "§a", 10),
    Simple_Villageois("Simple Villageois",  camps.Village, 0, "§a", 10),

    Enfant_Sauvage("Enfant Sauvage",        camps.Village, 0, "§5", 5),

    LG_Simple("Loup-Garou",                 camps.LoupGarou, 0, "§c", 1),
    LG_Blanc("Loup-Garou Blanc",            camps.Neutre, 0, "§6", 14);

    private final String nom;
    private camps camp;
    private int number;
    private String description;
    private final String strcolor;
    private final int intcolor;

    roles(String nom, camps camp, int number, String strcolor, int intcolor) {
        this.nom = nom;
        this.camp = camp;
        this.number = number;
        this.description = "§9Votre objectif est de .";
        this.strcolor = strcolor;
        this.intcolor = intcolor;
    }

    // set
    public void setDescription(String description) {
        this.description = description;
    }

    public void setCamp(camps camp) {
        this.camp = camp;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    // get
    public camps getCamp() {
        return camp;
    }

    public int getIntColor() { return intcolor; }

    public int getNumber() { return number; }

    public String getDescription() { return description; }

    public String getStrColor() {
        return strcolor;
    }

    public String getName() {
        return nom;
    }
}
