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
    private ItemStack tracker;

    public GuiHandler(Loader loader) {
        this.loader = loader;
        namespacedKey = Main.getMain().getNamespacedKey();
        setTracker(createItem(Material.COMPASS, ChatColor.RED + "Tracker", " ", ChatColor.GOLD + "Rightclick" + ChatColor.GRAY + "to track the closest bounty"));
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

    public void bountyProfile(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED + "Bounty Profile");

        for (int i = 0; i < 54; i++) {
            if (i < 9 || i % 9 == 0 || (i + 1) % 9 == 0 || i > 44) {
                inv.setItem(i, createItem(Material.valueOf(loader.getPlayerHandler().getProfileColor(p.getUniqueId()) + "_STAINED_GLASS_PANE"), " "));
            }
        }

        PlayerRanks rank = loader.getPlayerHandler().getRank(p.getUniqueId());

        inv.setItem(10, createItem(Material.COMMAND_BLOCK, ChatColor.GRAY + "Settings"));

        inv.setItem(16, createItem(Material.COMPASS, ChatColor.RED + "Tracker", " ", ChatColor.GRAY + "Use this item to track the closest", ChatColor.GRAY + "player with an active bounty on them."));

        inv.setItem(22, createSkull(p.getUniqueId(), ChatColor.GRAY + p.getName(), 0, " ",
                ChatColor.GRAY + "Your license:", ChatColor.DARK_GREEN + rank.getIcon() + " " + ChatColor.GREEN + rank.getName(),
                ChatColor.GRAY + "Bonus: " + ChatColor.GREEN + rank.getBonus() + ChatColor.GRAY + "%", ChatColor.GRAY +
                        "Bounties Completed: " + ChatColor.GREEN + loader.getPlayerHandler().getBounties(p.getUniqueId())));

        if (loader.getBountyHandler().hasBounty(p.getUniqueId())) {

            Bounty bounty = loader.getBountyHandler().getBounty(p.getUniqueId());

            inv.setItem(31, createItem(Material.RED_DYE, ChatColor.RED + "There is a bounty on your head!",
                    " ", ChatColor.GRAY + "Issuer: " + ChatColor.RED + Bukkit.getOfflinePlayer(bounty.getIssuer()).getName(),
                    ChatColor.GRAY + "Bounty: " + ChatColor.RED + bounty.getBounty(), ChatColor.GRAY + "Amount to pay off: " +
                            ChatColor.RED + (bounty.getBounty() * 2), " ",
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "<Click me to pay off your bounty>"));


        } else {
            inv.setItem(31, createItem(Material.LIME_DYE, ChatColor.GREEN + "There is currently no bounty on your head."));
        }

        inv.setItem(37, createItem(Material.DARK_OAK_SIGN, ChatColor.GRAY + "Currently available bounties"));

        inv.setItem(43, createItem(Material.BARRIER, ChatColor.RED + "Close"));

        p.openInventory(inv);
    }

    public void profileSettings(Player p) {
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.RED + "Bounty Profile Settings");

        inv.setItem(0, createItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.WHITE + "Select color"));
        inv.setItem(1, createItem(Material.ORANGE_STAINED_GLASS_PANE, ChatColor.GOLD + "Select color"));
        inv.setItem(2, createItem(Material.MAGENTA_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE + "Select color"));
        inv.setItem(3, createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ChatColor.AQUA + "Select color"));
        inv.setItem(4, createItem(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.YELLOW + "Select color"));
        inv.setItem(5, createItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Select color"));
        inv.setItem(6, createItem(Material.PINK_STAINED_GLASS_PANE, ChatColor.LIGHT_PURPLE + "Select color"));
        inv.setItem(7, createItem(Material.GRAY_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Select color"));
        inv.setItem(8, createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "Select color"));
        inv.setItem(9, createItem(Material.CYAN_STAINED_GLASS_PANE, ChatColor.DARK_AQUA + "Select color"));
        inv.setItem(10, createItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE + "Select color"));
        inv.setItem(11, createItem(Material.BLUE_STAINED_GLASS_PANE, ChatColor.DARK_BLUE + "Select color"));
        inv.setItem(12, createItem(Material.BROWN_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Select color"));
        inv.setItem(13, createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.DARK_GREEN + "Select color"));
        inv.setItem(14, createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Select color"));
        inv.setItem(15, createItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + "Select color"));

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

    public ItemStack getTracker() {
        return tracker;
    }

    public void setTracker(ItemStack tracker) {
        this.tracker = tracker;
    }
}
