package com.cfunicorn.bountyhunter.utils;

import com.cfunicorn.bountyhunter.commands.CMD_Bounty;
import com.cfunicorn.bountyhunter.listeners.ConnectionListeners;
import com.cfunicorn.bountyhunter.listeners.GUIListener;
import com.cfunicorn.bountyhunter.listeners.PlayerDeathListener;
import com.cfunicorn.bountyhunter.listeners.TrackerListener;
import com.cfunicorn.bountyhunter.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class Loader {

    private String prefix, noPerm;
    private File file;
    private FileConfiguration config;
    private MySQL mySQL;
    private BountyHandler bountyHandler;
    private GuiHandler guiHandler;
    private PlayerHandler playerHandler;

    public Loader(Main main) {
        setFile(new File(main.getDataFolder(), "config.yml"));
        setConfig(YamlConfiguration.loadConfiguration(getFile()));

        setPrefix(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("Settings.Prefix"))) + ChatColor.RESET + " ");
        setNoPerm(getPrefix() + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("Settings.NoPerm"))));

        setMySQL(new MySQL(this));
        setBountyHandler(new BountyHandler(this));
        setGuiHandler(new GuiHandler(this));
        setPlayerHandler(new PlayerHandler(this));

    }

    public void registerClasses(Main main) {
        new CMD_Bounty(main);

        new ConnectionListeners(main);
        new PlayerDeathListener(main);
        new GUIListener(main);
        new TrackerListener(main);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNoPerm() {
        return noPerm;
    }

    public void setNoPerm(String noPerm) {
        this.noPerm = noPerm;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public void setMySQL(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    public BountyHandler getBountyHandler() {
        return bountyHandler;
    }

    public void setBountyHandler(BountyHandler bountyHandler) {
        this.bountyHandler = bountyHandler;
    }

    public GuiHandler getGuiHandler() {
        return guiHandler;
    }

    public void setGuiHandler(GuiHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public void setPlayerHandler(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }
}
