package com.cfunicorn.bountyhunter.listeners;

import com.cfunicorn.bountyhunter.main.Main;
import com.cfunicorn.bountyhunter.utils.Bounty;
import com.cfunicorn.bountyhunter.utils.Loader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrackerListener implements Listener {

    private final String prefix;
    private final Loader loader;
    private final List<Player> delay;

    public TrackerListener(Main main) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.loader = main.getLoader();
        this.prefix = loader.getPrefix();
        this.delay = new ArrayList<>();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        try {
            Player p = (Player) e.getWhoClicked();

            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Bounty Profile")) {
                e.setCancelled(true);

                if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.COMPASS))

                    if (!(delay.contains(p))) {

                        Player closestPlayerWithBounty = findClosestPlayerWithBounty(p);
                        if (closestPlayerWithBounty != null) {

                            int distance = (int) Math.round(calculateDistanceXZ(p, closestPlayerWithBounty));
                            Location targetLocation = closestPlayerWithBounty.getLocation();
                            Bounty bounty = loader.getBountyHandler().getBounty(closestPlayerWithBounty.getUniqueId());

                            p.sendMessage(prefix + ChatColor.GRAY + "Player tracked successfully!");
                            assert bounty != null;
                            p.sendMessage(ChatColor.RED + closestPlayerWithBounty.getName() + ChatColor.GRAY + " (Reward: " + ChatColor.RED + bounty.getBounty() + ChatColor.GRAY + ")");
                            p.sendMessage(ChatColor.GRAY + "They are " + ChatColor.RED + distance + ChatColor.GRAY + " blocks away.");
                            p.sendMessage(ChatColor.GRAY + "Their exact location is X: " + ChatColor.RED + targetLocation.getBlockX() + ChatColor.GRAY + ", Y: " + ChatColor.RED + targetLocation.getBlockY() + ChatColor.GRAY + ", Z: " + ChatColor.RED + targetLocation.getBlockZ());

                            p.closeInventory();
                            delay.add(p);

                            Bukkit.getScheduler().runTaskLater(Main.getMain(), () -> delay.remove(p), 60 * 20);
                        } else {
                            p.sendMessage(prefix + ChatColor.GRAY + "Couldn't track any players. Perhaps they are too far away or there is no active bounty at the moment.");
                        }

                    } else {
                        p.sendMessage(prefix + ChatColor.GRAY + "You may use this only once every 60 seconds.");
                    }
            }

        } catch (Exception ignored) {

        }

    }

    private double calculateDistanceXZ(Player player1, Player player2) {
        double dx = player1.getLocation().getX() - player2.getLocation().getX();
        double dz = player1.getLocation().getZ() - player2.getLocation().getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    private Player findClosestPlayerWithBounty(Player origin) {
        Player closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (loader.getBountyHandler().hasBounty(player.getUniqueId())) {
                double distance = player.getLocation().distanceSquared(origin.getLocation());
                if (origin != player) {
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPlayer = player;
                    }
                }
            }
        }

        return closestPlayer;
    }

}
