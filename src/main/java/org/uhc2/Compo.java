package org.uhc2;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.uhc2.enums.roles;

import java.util.Arrays;

public class Compo implements Listener {
    private final Uhc2 main;

    public Compo(Uhc2 main) {
        this.main = main;
    }

    Inventory roleInventory;

    public void roleGUI() {
        roleInventory = Bukkit.createInventory(null, 27, "Rôles disponibles.");

        initItems();
    }

    private ItemStack createDyeGui(final int dyeInt, final int roleAmount, final String itemName, final String... itemLore) {
        final ItemStack dyeItem = new ItemStack(Material.INK_SACK, roleAmount, (byte)dyeInt);
        final ItemMeta dyeMeta = dyeItem.getItemMeta();

        dyeMeta.setDisplayName(itemName);
        dyeMeta.setLore(Arrays.asList(itemLore));

        dyeItem.setItemMeta(dyeMeta);

        return dyeItem;
    }

    public void initItems() {
        if (roleInventory == null) return;
        roleInventory.clear();

        int roleInt = 0;
        for (roles role : roles.values()) {
            main.roleListInt.put(roleInt, role);
            roleInt = roleInt + 1;

            if (role.getNumber() >= 1) {
                roleInventory.addItem(createDyeGui(role.getIntColor(), role.getNumber(), (role.getStrColor() + role.getName()), "Le role est activé :)"));
            } else {
                roleInventory.addItem(createDyeGui(8, 1, (role.getStrColor() + role.getName()), "le role n'est pas activé :("));
            }
        }
    }

    public void entityOpenInventory(final HumanEntity entity) {
        roleGUI();
        entity.openInventory(roleInventory);
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(roleInventory)) {
            event.setCancelled(true);

            final ItemStack clickedItem = event.getCurrentItem();
            final Player player = (Player) event.getWhoClicked();
            final boolean isRightClick = event.isRightClick();
            final boolean isLeftClick = event.isLeftClick();
            final int slotClicked = event.getRawSlot();
            final roles roleFromSlot = main.roleListInt.get(slotClicked);

            if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

            if (isRightClick) {
                if (roleFromSlot.getNumber() >= 1) {
                    roleFromSlot.setNumber(roleFromSlot.getNumber() - 1);
                    if (main.debugMessages) {player.sendMessage(main.gameTag_Prive + "Le rôle '§b" + roleFromSlot.getName() + "§9' a bien été enlevé.");}
                } else {
                    if (main.debugMessages) {player.sendMessage(main.gameTag_Prive + "Le rôle '§b" + roleFromSlot.getName() + "§9' n'est pas dans la composition.");}
                }
            } else if (isLeftClick) {
                if (roleFromSlot.getNumber()  <= 63) {
                    roleFromSlot.setNumber(roleFromSlot.getNumber() + 1);
                    if (main.debugMessages) {player.sendMessage(main.gameTag_Prive + "Le rôle '§b" + roleFromSlot.getName() + "§9' a bien été ajouté.");}
                } else {
                    if (main.debugMessages) {player.sendMessage(main.gameTag_Prive + "Le rôle '§b" + roleFromSlot.getName() + "§9' n'a pas pu être ajouté.");}
                }
            }

            initItems();
        }
    }

    @EventHandler
    public void OnInventoryClick(InventoryDragEvent event) {
        if (event.getInventory().equals(roleInventory)) {
            event.setCancelled(true);
        }
    }

}
