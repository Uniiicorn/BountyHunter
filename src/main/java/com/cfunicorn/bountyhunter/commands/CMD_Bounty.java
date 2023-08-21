package com.cfunicorn.bountyhunter.commands;

import com.cfunicorn.bountyhunter.main.Main;
import com.cfunicorn.bountyhunter.utils.Bounty;
import com.cfunicorn.bountyhunter.utils.Loader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMD_Bounty implements CommandExecutor {

    private String prefix;
    private String noPerm;
    private Loader loader;

    public CMD_Bounty(Main main) {
        Objects.requireNonNull(main.getCommand("bounty")).setExecutor(this);
        this.loader = main.getLoader();
        this.prefix = loader.getPrefix();
        this.noPerm = loader.getNoPerm();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage(noPerm);
            return true;
        }

        //bounty <add> <player> <amount>

        if (args.length == 0) {
            loader.getGuiHandler().listBounties(p);
            return true;
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("check")) {

                if (!(loader.getBountyHandler().hasBounty(p.getUniqueId()))) {
                    p.sendMessage(prefix + ChatColor.GRAY + "There is currently no bounty on your head.");
                    return true;
                }

                Bounty bounty = loader.getBountyHandler().getBounty(p.getUniqueId());

                p.sendMessage(prefix + ChatColor.RED + "There is a bounty on your head!");
                p.sendMessage(ChatColor.GRAY + "Issuer: " + ChatColor.RED + Bukkit.getOfflinePlayer(bounty.getIssuer()).getName());
                p.sendMessage(ChatColor.GRAY + "Bounty: " + ChatColor.RED + bounty.getBounty());
                p.sendMessage(ChatColor.GRAY + "Amount to pay off: " + ChatColor.RED + (bounty.getBounty() * 2));

                return true;
            } else if (args[0].equalsIgnoreCase("payoff")) {

                if (!(loader.getBountyHandler().hasBounty(p.getUniqueId()))) {
                    p.sendMessage(prefix + ChatColor.GRAY + "There is currently no bounty on your head.");
                    return true;
                }

                Bounty bounty = loader.getBountyHandler().getBounty(p.getUniqueId());

                int amount = bounty.getBounty() * 2;

                if (Main.getMain().getEconomy().getBalance(p) < amount) {
                    p.sendMessage(prefix + ChatColor.GRAY + "It looks like you don't have enough money for that.");
                    return true;
                }

                p.sendMessage(prefix + ChatColor.GRAY + "You " + ChatColor.GREEN + "successfully" + ChatColor.GRAY +
                        " paid off your bounty. (Money spent: " + ChatColor.RED + amount + ChatColor.GRAY + ")");

                Bukkit.broadcastMessage(prefix + ChatColor.GRAY + p.getName() + " paid off their bounty, it is no longer available.");

                Main.getMain().getEconomy().withdrawPlayer(p, amount);
                loader.getBountyHandler().deleteBounty(p.getUniqueId());

                return true;
            } else if (args[0].equalsIgnoreCase("profile")) {

                loader.getGuiHandler().bountyProfile(p);
                return true;
            } else {
                helpMessage(p);
                return true;
            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    p.sendMessage(prefix + ChatColor.GRAY + "Couldn't find that player. Are they online?");
                    return true;
                }

                if (p == target) {
                    p.sendMessage(prefix + ChatColor.GRAY + "You can't put a bounty on yourself.");
                    return true;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    p.sendMessage(prefix + ChatColor.GRAY + "That doesn't seem to be a valid number.");
                    return true;
                }

                if (loader.getBountyHandler().hasBounty(target.getUniqueId())) {
                    p.sendMessage(prefix + ChatColor.GRAY + "Seems like there already is a bounty on them.");
                    return true;
                }

                if (Main.getMain().getEconomy().getBalance(p) < amount) {
                    p.sendMessage(prefix + ChatColor.GRAY + "It looks like you don't have enough money for that.");
                    return true;
                }

                loader.getBountyHandler().setBounty(target.getUniqueId(), p.getUniqueId(), amount);
                p.sendMessage(prefix + ChatColor.GRAY + "You " + ChatColor.GREEN + "successfully" + ChatColor.GRAY +
                        " placed a bounty on " + ChatColor.RED + target.getName() + ChatColor.GRAY + ". (Reward: " + ChatColor.RED + amount + ChatColor.GRAY + ")");
                Bukkit.broadcastMessage(prefix + ChatColor.GRAY + "A bounty has been placed on " + ChatColor.RED + target.getName() + ChatColor.GRAY + ". (Reward: " + ChatColor.RED + amount + ChatColor.GRAY + ")");

                Main.getMain().getEconomy().withdrawPlayer(p, amount);

            } else {
                helpMessage(p);
            }
            return true;
        } else {
            helpMessage(p);
            return true;
        }

    }

    private void helpMessage(Player p) {
        p.sendMessage(ChatColor.GOLD + "BountyHunter Help");
        p.sendMessage(ChatColor.GRAY + "If you wish to get an overview of currently available bounties you may use " + ChatColor.RED + "/bounty.");
        p.sendMessage(ChatColor.GRAY + "however if you want to place a bounty on a player, simply use " + ChatColor.RED + "/bounty <add> <Player> <amount>.");
        p.sendMessage(ChatColor.GRAY + "By the way, by using " + ChatColor.RED + "/bouny check" + ChatColor.GRAY + " you can check if there is a bounty on you, as well as see who issued it, how much it is and how much it will cost to pay off.");
        p.sendMessage(ChatColor.GRAY + "Speaking of paying off your bounty, by using " + ChatColor.RED + "/bounty payoff" + ChatColor.GRAY + " you will be able to pay off your bounty for an increased price.");
        p.sendMessage(ChatColor.GRAY + "That's about it, as soon as you kill a player with an active bounty on them you will get it paid out to you.");

    }
}
