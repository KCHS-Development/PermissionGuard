package dev.dinofeng.permissionguard;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class PermissionGuard extends JavaPlugin {
    public FileConfiguration dataBase = new YamlConfiguration();
    public File dataFile;

    @Override
    public void onEnable() {
        createDataFile();
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        new VerifyCommand(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public FileConfiguration getData() {
        return this.dataBase;
    }

    private void createDataFile() {
        this.dataFile = new File(getDataFolder(), "Data.yml");
        saveRes(this.dataFile, "Data.yml");
        this.dataBase = new YamlConfiguration();
        try {
            this.dataBase.load(this.dataFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    private void saveRes(File file, String name) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource(name, false);
        }
    }
    public void saveDataFile() {
        try {
            this.dataBase.save(this.dataFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[PlayerCountReward] Failed to save Data.yml");
        }
    }

}
