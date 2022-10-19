package me.playajames.thesimpleeconomy.listeners;

import me.playajames.thesimpleeconomy.Account;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

import static me.playajames.thesimpleeconomy.TheSimpleEconomy.STARTING_BALANCE;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (Account.fetch(playerUUID) == null ) // ? Check if account exists if not... create one.
            Account.create(playerUUID, STARTING_BALANCE);
    }
}
