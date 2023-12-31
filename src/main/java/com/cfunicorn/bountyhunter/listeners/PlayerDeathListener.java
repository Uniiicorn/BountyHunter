package com.cfunicorn.bountyhunter.listeners;

import com.cfunicorn.bountyhunter.main.Main;
import com.cfunicorn.bountyhunter.utils.Bounty;
import com.cfunicorn.bountyhunter.utils.Loader;
import com.cfunicorn.bountyhunter.utils.PlayerRanks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerDeathListener implements Listener {

    private final Loader loader;
    private final String prefix;

    public PlayerDeathListener(Main main) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.loader = main.getLoader();
        this.prefix = loader.getPrefix();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

        if (e.getEntity() instanceof Player p) {

            if (e.getEntity().getKiller() != null) {

                Player killer = e.getEntity().getKiller();

                if (loader.getBountyHandler().hasBounty(p.getUniqueId())) {

                    Bounty bounty = loader.getBountyHandler().getBounty(p.getUniqueId());

                    loader.getPlayerHandler().addBounty(killer.getUniqueId());

                    if (loader.getPlayerHandler().checkRankUp(killer.getUniqueId())) {
                        PlayerRanks rank = loader.getPlayerHandler().getRank(killer.getUniqueId());
                        killer.sendMessage(prefix + ChatColor.GRAY + "You ranked up! You're now classified as a "
                                + ChatColor.DARK_GREEN + rank.getIcon() + " " + ChatColor.GREEN + rank.getName()
                                + ChatColor.GRAY + ". This means you will now get a " + ChatColor.GREEN + rank.getBonus()
                                + ChatColor.GRAY + "% bonus on every bounty you complete.");

                        Bukkit.broadcastMessage(prefix + ChatColor.GRAY + killer.getName() + " ranked up! They're now classified as a "
                                + ChatColor.DARK_GREEN + rank.getIcon() + " " + ChatColor.GREEN + rank.getName() + ChatColor.GRAY + "!");

                    }

                    assert bounty != null;
                    int bonus = loader.getPlayerHandler().calculateBonus(bounty.getBounty(), loader.getPlayerHandler().getRank(killer.getUniqueId()).getBonus());

                    Bukkit.broadcastMessage(prefix + ChatColor.GRAY + p.getName() + "'s bounty of " + ChatColor.GREEN + bounty.getBounty() + ChatColor.GRAY + " has been claimed by " + killer.getName());
                    killer.sendMessage(prefix + ChatColor.GRAY + "You successfully claimed this bounty of " + ChatColor.GREEN + bounty.getBounty() + ChatColor.GRAY + "! (Bonus: " + ChatColor.GREEN + bonus + ChatColor.GRAY + ")");

                    Main.getMain().getEconomy().depositPlayer(killer, bounty.getBounty() + bonus);

                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String timestamp = localDateTime.format(dtf);

                    p.getWorld().dropItemNaturally(p.getLocation(), loader.getGuiHandler().createSkull(p.getUniqueId(),
                            ChatColor.RED + "The skull of " + p.getName(), 0, " ",
                            ChatColor.GRAY + "This head was claimed by " + ChatColor.GOLD + killer.getName(),
                            ChatColor.GRAY + "Bounty: " + ChatColor.GREEN + bounty.getBounty(), ChatColor.GRAY + "----------------", ChatColor.GRAY + "Date claimed: " + timestamp));

                    loader.getBountyHandler().deleteBounty(p.getUniqueId());

                }

            }

        }

    }

}