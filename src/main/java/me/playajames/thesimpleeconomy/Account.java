package me.playajames.thesimpleeconomy;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DbRow;

import java.sql.SQLException;
import java.util.UUID;

public class Account {

    private final UUID ownerUUID;
    private double balance;

    private Account(UUID ownerUUID, double startingBalance) {
        this.ownerUUID = ownerUUID;
        this.balance = startingBalance;
    }

    public static Account create(UUID ownerUUID, double startingBalance) {
        Database db = DB.getGlobalDatabase();
        long id = -1;

        try {
            id = db.executeInsert("INSERT INTO `accounts` (`owner_uuid`, `balance`) VALUES (?, ?)", ownerUUID.toString(), startingBalance);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id == -1)
            return null;

        return fetch(ownerUUID);
    }

    public static Account fetch(UUID ownerUUID) {
        Database db = DB.getGlobalDatabase();

        DbRow row = null;

        try {
            row = db.getFirstRow("SELECT * FROM `accounts` WHERE `owner_uuid` = ? LIMIT 1", ownerUUID.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (row == null)
            return null;

        return new Account(
                UUID.fromString(row.getString("owner_uuid")),
                row.getDbl("balance"));

    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        updateData();
    }

    private void updateData() {
        int rowsModified = 0;
        Database db = DB.getGlobalDatabase();
        try {
            rowsModified = db.executeUpdate("UPDATE `accounts` SET `balance` = ? WHERE `owner_uuid` = ?", this.balance, this.ownerUUID.toString());
            // ^ This should only ever be one if successful update.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Transaction transfer(Account toAccount, double amount) {
        return Transaction.create(toAccount, this, amount);
    }

}
