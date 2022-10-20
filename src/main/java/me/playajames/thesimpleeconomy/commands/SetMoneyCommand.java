package me.playajames.thesimpleeconomy.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.ADoubleArgument;
import dev.jorel.commandapi.annotations.arguments.AOfflinePlayerArgument;
import me.playajames.thesimpleeconomy.Account;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static me.playajames.thesimpleeconomy.TheSimpleEconomy.PREFIX;

@Command("setmoney")
@Permission("thesimpleeconomy.setmoney")
public class SetMoneyCommand {

    private static final String usage = "Usage: /setmoney <targetPlayer> <balance>";

    @Default
    public static void setMoney(Player player) {
        player.sendMessage(PREFIX + usage);
    }

    @Default
    public static void setMoney(Player player, @AOfflinePlayerArgument OfflinePlayer targetPlayer) {
        player.sendMessage(PREFIX + usage);
    }

    @Default
    public static void setMoney(Player player, @ADoubleArgument double amount) {
        player.sendMessage(PREFIX + usage);
    }

    @Default
    public static void setMoney(Player player, @AOfflinePlayerArgument OfflinePlayer targetPlayer, @ADoubleArgument double amount) {

        // TODO Fix Player not found response when a player argument is entered. CommandAPI responds with "That player does not exist" without our prefix.
        if (!targetPlayer.hasPlayedBefore()) {
            player.sendMessage(PREFIX + "There is no bank account with that player name, this usually happens when that player has not played on this server yet.");
            return;
        }

        if (amount < 1) {
            player.sendMessage(PREFIX + "Oops... argument <amount> must be a positive number.");
            return;
        }

        Account targetAccount = Account.fetch(targetPlayer.getUniqueId());
        targetAccount.setBalance(amount);

        player.sendMessage(PREFIX + "You set " + targetPlayer.getName() + "'s balance to $" + amount + ".");
    }

}
