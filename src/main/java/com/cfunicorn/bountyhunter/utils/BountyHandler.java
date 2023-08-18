package com.cfunicorn.bountyhunter.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BountyHandler {

    private final Loader loader;

    public BountyHandler(Loader loader) {
        this.loader = loader;
    }

    public boolean hasBounty(UUID uuid) {
        boolean bool = false;

        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("SELECT * FROM BountyHunter WHERE target = ?");

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bool = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bool;
    }

    public void setBounty(UUID target, UUID issuer, int bounty) {

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = localDateTime.format(dtf);

        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("INSERT INTO BountyHunter (target,issuer,bounty,timestamp) VALUES(?,?,?,?)");
            ps.setString(1, target.toString());
            ps.setString(2, issuer.toString());
            ps.setInt(3, bounty);
            ps.setString(4, timestamp);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteBounty(UUID target) {
        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("DELETE FROM BountyHunter WHERE target = ?");
            ps.setString(1, target.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public @Nullable Bounty getBounty(UUID uuid) {

        Bounty bounty = null;

        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("SELECT * FROM BountyHunter WHERE target = ?");

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UUID target = UUID.fromString(rs.getString("target"));
                UUID issuer = UUID.fromString(rs.getString("issuer"));
                int id = rs.getInt("id");
                int amount = rs.getInt("bounty");
                String timeStamp = rs.getString("timeStamp");

                bounty = new Bounty(target, issuer, amount, id, timeStamp);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bounty;
    }

    public List<Bounty> getBounties() {
        List<Bounty> bounties = new ArrayList<>();
        try {
            PreparedStatement ps = loader.getMySQL().getConnection().prepareStatement("SELECT * FROM BountyHunter");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UUID target = UUID.fromString(rs.getString("target"));
                UUID issuer = UUID.fromString(rs.getString("issuer"));
                int id = rs.getInt("id");
                int bounty = rs.getInt("bounty");
                String timeStamp = rs.getString("timeStamp");

                if (!(Bukkit.getPlayer(target) == null))
                    bounties.add(new Bounty(target, issuer, bounty, id, timeStamp));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bounties;
    }

}
