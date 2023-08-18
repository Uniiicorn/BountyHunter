package com.cfunicorn.bountyhunter.utils;

import com.cfunicorn.bountyhunter.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GuiHandler {

    private final Loader loader;
    private final NamespacedKey namespacedKey;

    public GuiHandler(Loader loader) {
        this.loader = loader;
        namespacedKey = Main.getMain().getNamespacedKey();
    }

    /**
     * @param p the player the gui of all available bounties should be displayed to
     */
    public void listBounties(Player p) {

        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED + "Bounties");

        List<Bounty> bounties = loader.getBountyHandler().getBounties();

        if (bounties.isEmpty()) {
            inv.setItem(22, createItem(Material.BARRIER, ChatColor.DARK_RED + "no Bounties available.",
                    ChatColor.RED + "As soon as a bounty is placed on someone", ChatColor.RED + "their head will be up for grabs in here."));
            p.openInventory(inv);
            return;
        }

        for (int i = 0; i < Math.min(53, bounties.size()); i++) {
            Bounty bounty = bounties.get(i);
            if (bounty == null) {
                continue;
            }

            UUID target = bounty.getTarget();
            UUID issuer = bounty.getIssuer();
            int amount = bounty.getBounty();
            int id = bounty.getId();
            String timeStamp = bounty.getTimeStamp();

                inv.addItem(createSkull(target, ChatColor.GOLD + Bukkit.getPlayer(target).getName(), id, " ", ChatColor.RED + "Issuer: " + ChatColor.GRAY + Bukkit.getPlayer(issuer).getName(),
                        ChatColor.RED + "Date issued: " + ChatColor.GRAY + timeStamp, ChatColor.GRAY + "Reward: " + ChatColor.GREEN + amount));
        }

        p.openInventory(inv);

    }

    /**
     * just a simple helper method
     */
    public ItemStack createItem(Material material, String displayName, String... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * just a simple helper method
     */
    public ItemStack createSkull(UUID owner, String displayName, int persistentData, String... lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, persistentData);
        itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
