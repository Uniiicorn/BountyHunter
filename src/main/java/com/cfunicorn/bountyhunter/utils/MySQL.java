package com.cfunicorn.bountyhunter.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {

    private Connection connection;
    private final String host, user, pass, db, port;

    public MySQL(Loader loader) {
        this.host = loader.getConfig().getString("Settings.MySQL.Host");
        this.user = loader.getConfig().getString("Settings.MySQL.User");
        this.pass = loader.getConfig().getString("Settings.MySQL.Password");
        this.db = loader.getConfig().getString("Settings.MySQL.Database");
        this.port = loader.getConfig().getString("Settings.MySQL.Port");
    }

    public boolean isConnected() {
        return (getConnection() != null);
    }

    public void connect() {
        if (!(isConnected())) {
            try {
                setConnection(DriverManager.getConnection("jdbc:mysql://" + user + ":" + pass + "@" + host + ":" + port
                        + "/" + db + "?autoReconnect=true"));

                PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS BountyHunter ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"
                        + "target VARCHAR(36),"
                        + "issuer VARCHAR(36),"
                        + "bounty INT,"
                        + "timestamp VARCHAR(36))");
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                getConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }
}
