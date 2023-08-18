package com.cfunicorn.bountyhunter.main;

import com.cfunicorn.bountyhunter.utils.Loader;
import com.cfunicorn.bountyhunter.utils.Metrics;
import com.cfunicorn.bountyhunter.utils.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main main;
    private Loader loader;
    private NamespacedKey namespacedKey;
    private Economy economy;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        setMain(this);

        new Metrics(this, 19553);

        getLogger().info("checking for updates...");
        new UpdateChecker(this, 112098).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("you're running the latest version, yay!");
            } else {
                getLogger().info("looks like there is a new version available, you can download it here: https://cf-unicorn.com/go/bountyhunter");
            }
        });

        if (!setupEconomy()) {
            getLogger().severe("Vault not found or no economy plugin!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setNamespacedKey(new NamespacedKey(this, "BountyHunter"));

        setLoader(new Loader(this));
        getLoader().getMySQL().connect();

        getLoader().registerClasses(this);
    }

    @Override
    public void onDisable() {
        getLoader().getMySQL().disconnect();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        setEconomy(rsp.getProvider());
        return getEconomy() != null;
    }

    public static Main getMain() {
        return main;
    }

    public static void setMain(Main main) {
        Main.main = main;
    }

    public Loader getLoader() {
        return loader;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public void setNamespacedKey(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void setEconomy(Economy economy) {
        this.economy = economy;
    }
}
