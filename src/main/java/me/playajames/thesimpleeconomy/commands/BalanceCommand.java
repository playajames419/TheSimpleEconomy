package me.playajames.thesimpleeconomy.commands;

import dev.jorel.commandapi.annotations.*;
import dev.jorel.commandapi.annotations.arguments.AOfflinePlayerArgument;
import me.playajames.thesimpleeconomy.Account;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static me.playajames.thesimpleeconomy.TheSimpleEconomy.PREFIX;

@Command("balance")
@Permission("thesimpleeconomy.balance")
@Alias("bal")
@Help("Shows your balance.")
public class BalanceCommand {

    @Default
    public static void balance(Player player) {
        Account account = Account.fetch(player.getUniqueId());
        player.sendMessage(PREFIX + "Your balance is $" + account.getBalance() + ".");
    }

    @Default
    @Permission("thesimpleeconomy.balance.lookup")
    public static void balance(Player player, @AOfflinePlayerArgument OfflinePlayer targetPlayer) {

        // TODO Fix Player not found response when a player argument is entered. CommandAPI responds with "That player does not exist" without our prefix.
        if (!targetPlayer.hasPlayedBefore()) {
            player.sendMessage(PREFIX + "There is no bank account with that player name, this usually happens when that player has not played on this server yet.");
            return;
        }

        Account targetAccount = Account.fetch(targetPlayer.getUniqueId());
        player.sendMessage(PREFIX + targetPlayer.getName() + " has a balance of " + targetAccount.getBalance() + ".");
    }

}
