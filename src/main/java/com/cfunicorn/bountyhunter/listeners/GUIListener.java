package com.cfunicorn.bountyhunter.listeners;

import com.cfunicorn.bountyhunter.main.Main;
import com.cfunicorn.bountyhunter.utils.Loader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class GUIListener implements Listener {

    private final Loader loader;

    public GUIListener(Main main) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.loader = main.getLoader();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        try {

            Player p = (Player) e.getWhoClicked();

            if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Bounties")) {
                e.setCancelled(true);
            } else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Bounty Profile")) {
                e.setCancelled(true);

                if(Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.BARRIER)) {
                    p.closeInventory();
                } else if(e.getCurrentItem().getType().equals(Material.DARK_OAK_SIGN)) {
                    p.performCommand("bounty");
                } else if(e.getCurrentItem().getType().equals(Material.RED_DYE)) {
                    p.performCommand("bounty payoff");
                    p.closeInventory();
                } else if(e.getCurrentItem().getType().equals(Material.COMMAND_BLOCK)) {
                    loader.getGuiHandler().profileSettings(p);
                }

            } else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Bounty Profile Settings")) {
                e.setCancelled(true);

                String color = Objects.requireNonNull(e.getCurrentItem()).getType().toString().replace("_STAINED_GLASS_PANE", "");

                loader.getPlayerHandler().setProfileColor(p.getUniqueId(), color);
                loader.getGuiHandler().bountyProfile(p);
            }

            if(Objects.equals(e.getCurrentItem(), loader.getGuiHandler().getTracker()) && e.getInventory() != p.getInventory()) {
                e.setCancelled(true);
            }

        } catch (Exception ignored) {

        }
    }
}
