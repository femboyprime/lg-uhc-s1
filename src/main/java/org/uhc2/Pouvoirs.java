package org.uhc2;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.uhc2.enums.roles;

import java.util.Arrays;
import java.util.Collection;

public class Pouvoirs {
    private final Uhc2 main;
    public Pouvoirs(Uhc2 main) {this.main = main;}

    // repeated
    public final PotionEffect nightvision = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
    public final PotionEffect force = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false);
    public final PotionEffect lg_speed = new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false);
    public final PotionEffect lg_abso = new PotionEffect(PotionEffectType.ABSORPTION, 4800, 1, false, false);

    public final Collection<PotionEffect> lg_kill_effects = Arrays.asList(lg_speed, lg_abso);


    public void giveEffect(Joueur joueur, PotionEffect potionEffect) {
        Bukkit.getScheduler().runTask(this.main, () -> joueur.getPlayer().addPotionEffect(potionEffect));
    }

    public void giveEffects(Joueur joueur, Collection<PotionEffect> collection) {
        Bukkit.getScheduler().runTask(this.main, () -> joueur.getPlayer().addPotionEffects(collection));
    }

    public void givePermanent(Joueur joueur) {
        Player player = joueur.getPlayer();

        if (joueur.role == roles.Petite_Fille || joueur.role == roles.Voyante_Bavarde) {
            giveEffect(joueur, nightvision);
        } else if (joueur.role == roles.Pyromane) {
            PotionEffect fireresistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false);

            giveEffect(joueur, fireresistance);
        } else if (joueur.role == roles.Ancien && !joueur.hasRespawned) {
            PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false);

            giveEffect(joueur, resistance);
        } else if (joueur.isLoup_Effect()) {
            Collection<PotionEffect> effects = Arrays.asList(force, nightvision);
            giveEffects(joueur, effects);

            if (joueur.role == roles.LG_Blanc) {
                player.setMaxHealth(40);
            }
        }
    }

    public void giveOneTime(Joueur joueur) {
        Player player = joueur.getPlayer();

        if (joueur.role == roles.Petite_Fille) {
            //-// default minecraft potion
            ItemStack speed_1_8mn = new ItemStack(Material.POTION, 2, (short)8258);
            player.getInventory().addItem(speed_1_8mn);

        } else if (joueur.role == roles.Sorciere) {
            //-// default minecraft potion
            ItemStack healPotion = new ItemStack(Material.POTION, 3, (short)16453);
            player.getInventory().addItem(healPotion);

            ItemStack damagePotion = new ItemStack(Material.POTION, 3, (short)16460);
            player.getInventory().addItem(damagePotion);

            //-// custom minecraft potion
            // setup the item and the new effect
            PotionEffect effect_regen_45s = new PotionEffect(PotionEffectType.REGENERATION, 900, 0, false, true);
            ItemStack regenPotion = new ItemStack(Material.POTION, 1, (short)16385);

            // rewrite potion duration
            PotionMeta potionMeta = (PotionMeta) regenPotion.getItemMeta();
            potionMeta.addCustomEffect(effect_regen_45s, true);

            regenPotion.setItemMeta(potionMeta);

            // give item
            player.getInventory().addItem(regenPotion);
        } else if (joueur.role == roles.Voyante_Bavarde) {
            //-// default minecraft blocks
            ItemStack obsi = new ItemStack(Material.OBSIDIAN, 4);
            ItemStack biblio = new ItemStack(Material.BOOKSHELF, 4);

            player.getInventory().addItem(obsi);
            player.getInventory().addItem(biblio);
        } else if (joueur.role == roles.Chasseur) {
            //-// default minecraft stuff
            // power 4
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
            bookMeta.addStoredEnchant(Enchantment.ARROW_DAMAGE, 4, true);
            book.setItemMeta(bookMeta);

            player.getInventory().addItem(book);

            // 128 arrow
            ItemStack arrows = new ItemStack(Material.ARROW, 128);

            player.getInventory().addItem(arrows);

            // 3 loup
            ItemStack spawneggs = new ItemStack(Material.MONSTER_EGG, 3, (short)95);

            player.getInventory().addItem(spawneggs);

            // 15 os
            ItemStack bones = new ItemStack(Material.BONE, 15);

            player.getInventory().addItem(bones);
        } else if (joueur.role == roles.Salvateur) {
            //-// default minecraft potion
            ItemStack healPotion = new ItemStack(Material.POTION, 2, (short)16453);
            player.getInventory().addItem(healPotion);
        } else if (joueur.role == roles.Ancien) {
            ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
            ItemMeta rodMeta = rod.getItemMeta();
            rodMeta.addEnchant(Enchantment.LUCK, 5, true);
            rod.setItemMeta(rodMeta);

            player.getInventory().addItem(rod);
        } else if (joueur.role == roles.Pyromane) {
            // fire aspect book
            ItemStack firebook = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantmentStorageMeta firebookMeta = (EnchantmentStorageMeta) firebook.getItemMeta();
            firebookMeta.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
            firebook.setItemMeta(firebookMeta);

            player.getInventory().addItem(firebook);

            // flame book
            ItemStack flamebook = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantmentStorageMeta flamebookMeta = (EnchantmentStorageMeta) flamebook.getItemMeta();
            flamebookMeta.addStoredEnchant(Enchantment.ARROW_FIRE, 1, true);
            flamebook.setItemMeta(flamebookMeta);

            player.getInventory().addItem(flamebook);

            // 2 lava buckets
            for (int index = 0; index < 2; index++) {
                ItemStack lavabuckets = new ItemStack(Material.LAVA_BUCKET, 2);

                player.getInventory().addItem(lavabuckets);
            }


            // flint and steel
            ItemStack flintandsteel = new ItemStack(Material.FLINT_AND_STEEL, 1);

            player.getInventory().addItem(flintandsteel);
        }
    }

    public void giveNight(Joueur joueur) {
        Player player = joueur.getPlayer();
        if (joueur.role == roles.Petite_Fille) {
            PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 12000, 0, false, false);
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, 12000, 0, false, false);

            Collection<PotionEffect> effects = Arrays.asList(weakness, invisibility);
            giveEffects(joueur, effects);
        }
    }

    public void giveDay(Joueur joueur) {
        // aucun role n'a de pouvoir seulement le jour... (
    }
}
