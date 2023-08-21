package com.cfunicorn.bountyhunter.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerHandler {

    private final Loader loader;

    public PlayerHandler(Loader loader) {
        this.loader = loader;
    }

    public int getBounties(UUID uuid) {
        int bounties = -1;

        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("SELECT bounties FROM BountyHunterPlayers WHERE uuid = ?");

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bounties = rs.getInt("bounties");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bounties;
    }

    public String getProfileColor(UUID uuid) {
        String color = "RED";

        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("SELECT color FROM BountyHunterPlayers WHERE uuid = ?");

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                color = rs.getString("color");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return color;
    }

    public void addBounty(UUID uuid) {
        int bounties = getBounties(uuid) + 1;

        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("UPDATE BountyHunterPlayers SET bounties = ? WHERE uuid = ?");
            ps.setInt(1, bounties);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void setProfileColor(UUID uuid, String color) {
        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("UPDATE BountyHunterPlayers SET color = ? WHERE uuid = ?");
            ps.setString(1, color);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void setupPlayer(UUID uuid) {

        if (getBounties(uuid) == -1) {

            try {
                PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("INSERT INTO BountyHunterPlayers (uuid,bounties,color) VALUES(?,?,?)");
                ps.setString(1, uuid.toString());
                ps.setInt(2, 0);
                ps.setString(3, "RED");
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkRankUp(UUID uuid) {
        int bounties = getBounties(uuid);

        return bounties % 10 == 0 && bounties >= 10 && bounties <= 100;
    }

    public PlayerRanks getRank(UUID uuid) {
        int bounties = getBounties(uuid);

        if (bounties < 10) {
            return PlayerRanks.RANK1;
        }

        int rankIndex = (bounties - 10) / 10;
        rankIndex = Math.min(rankIndex, 9);

        return PlayerRanks.values()[rankIndex];
    }

    public int calculateBonus(int bounty, int percentage) {
        double d = Double.parseDouble("0." + percentage);
        double bonusPayout = bounty * d;
        return (int) Math.round(bonusPayout);
    }

}
