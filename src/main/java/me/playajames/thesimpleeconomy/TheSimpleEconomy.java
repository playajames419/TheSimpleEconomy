package me.playajames.thesimpleeconomy;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import me.playajames.thesimpleeconomy.commands.BalanceCommand;
import me.playajames.thesimpleeconomy.listeners.PlayerJoinListener;
import me.playajames.thesimpleeconomy.utilities.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheSimpleEconomy extends JavaPlugin {

    public static double STARTING_BALANCE;
    public static String PREFIX;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig().verboseOutput(false)); //Load with verbose output
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setConfigValues();
        DatabaseUtil.init("database.db");
        CommandAPI.onEnable(this);
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        DatabaseUtil.close();
    }

    private void setConfigValues() {
        STARTING_BALANCE = getConfig().getDouble("starting-balance");
        PREFIX = getConfig().getString("prefix");
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    private void registerCommands() {
        CommandAPI.registerCommand(BalanceCommand.class);
    }

}
