package me.mystichqra.flyplugin;

import me.mystichqra.flyplugin.commands.FlyCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlyPlugin extends JavaPlugin {

    @Override
    public void onEnable() { //Plugin startup logic
        System.out.println("Plugin by MysticHqra <3");

        getServer().getPluginManager().registerEvents(new FlyCommand(),this);
        getCommand("fly").setExecutor(new FlyCommand());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    public static String Colors(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
