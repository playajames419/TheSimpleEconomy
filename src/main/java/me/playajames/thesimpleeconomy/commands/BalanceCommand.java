package me.playajames.thesimpleeconomy.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import me.playajames.thesimpleeconomy.Account;
import org.bukkit.entity.Player;

import static me.playajames.thesimpleeconomy.TheSimpleEconomy.PREFIX;

@Command("balance")
@Permission("thesimpleeconomy.balance")
public class BalanceCommand {

    @Default
    public static void balance(Player player) {
        Account account = Account.fetch(player.getUniqueId());
        player.sendMessage(PREFIX + "Your balance is $" + account.getBalance() + ".");
    }

}
