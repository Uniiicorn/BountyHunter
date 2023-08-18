package com.cfunicorn.bountyhunter.listeners;

import com.cfunicorn.bountyhunter.main.Main;
import com.cfunicorn.bountyhunter.utils.Bounty;
import com.cfunicorn.bountyhunter.utils.Loader;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListeners implements Listener {

    private final Loader loader;
    private final String prefix;

    public ConnectionListeners(Main main) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.loader = main.getLoader();
        this.prefix = loader.getPrefix();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        if (loader.getBountyHandler().hasBounty(p.getUniqueId())) {

            Bounty bounty = loader.getBountyHandler().getBounty(p.getUniqueId());
            assert bounty != null;

            TextComponent message = new TextComponent(prefix + ChatColor.GRAY + p.getName() + " just joined with an " + ChatColor.UNDERLINE + "active" + ChatColor.RESET + " " + ChatColor.GRAY + "bounty on their head!");
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                    new TextComponent(ChatColor.GRAY + "Reward: " + ChatColor.GREEN + bounty.getBounty())
            }));

            for(Player all : Bukkit.getOnlinePlayers()) {
                all.spigot().sendMessage(message);
            }
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();

        if (loader.getBountyHandler().hasBounty(p.getUniqueId())) {

            Bukkit.broadcastMessage(prefix + ChatColor.GRAY + p.getName() + " just left, their bounty can be claimed once they appear again.");

        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        try {
            if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Bounties")) {
                e.setCancelled(true);
            }
        } catch (Exception ignored) {

        }
    }
}
