package me.playajames.thesimpleeconomy;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DbRow;

import java.sql.SQLException;
import java.util.UUID;

public class Transaction {

    private final int id;
    private final Account sender;
    private final Account receiver;
    private final double amount;
    private TransactionStatus status;

    private enum TransactionStatus {
        Pending, // ? pending process function
        Complete, // ? completed success, account balances affected accordingly
        InsufficientFunds; // ? meaning no account balances were affected
    }

    private Transaction(int id, Account sender, Account receiver, double amount, String status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.status = TransactionStatus.valueOf(status);

        if (this.status == TransactionStatus.Pending)
            process();
    }

    public static Transaction create(Account sender, Account receiver, double amount) {
        Database db = DB.getGlobalDatabase();
        long id = -1;

        try {
            id = db.executeInsert("INSERT INTO `transactions` (`sender_uuid`, `receiver_uuid`, `amount`, `status`) VALUES (?, ?, ?, ?)", sender.getOwnerUUID().toString(), receiver.getOwnerUUID().toString(), amount, TransactionStatus.Pending.name());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id == -1)
            return null;

        return fetch((int) id);
    }

    public static Transaction fetch(UUID ownerUUID, int resultCountLimit) {
        Database db = DB.getGlobalDatabase();

        DbRow row = null;

        try {
            row = db.getFirstRow("SELECT * FROM `transactions` WHERE `sender_uuid` = ? OR `receiver_uuid` = ? LIMIT ?", ownerUUID.toString(), ownerUUID.toString(), resultCountLimit);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (row == null)
            return null;

        return new Transaction(
                row.getInt("id"),
                Account.fetch(UUID.fromString(row.getString("sender_uuid"))),
                Account.fetch(UUID.fromString(row.getString("receiver_uuid"))),
                row.getDbl("amount"),
                row.getString("status")
        );

    }

    public static Transaction fetch(int id) {
        Database db = DB.getGlobalDatabase();

        DbRow row = null;

        try {
            row = db.getFirstRow("SELECT * FROM `transactions` WHERE `id` = ? LIMIT 1", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (row == null)
            return null;

        return new Transaction(
                row.getInt("id"),
                Account.fetch(UUID.fromString(row.getString("sender_uuid"))),
                Account.fetch(UUID.fromString(row.getString("receiver_uuid"))),
                row.getDbl("amount"),
                row.getString("status")
        );

    }

    private void updateData() {
        int rowsModified = 0;
        Database db = DB.getGlobalDatabase();
        try {
            rowsModified = db.executeUpdate("UPDATE `transactions` SET `status` = ? WHERE `id` = ?", this.status.name(), this.id);
            // ^ This should only ever be one if successful update.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getSender() {
        return sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    private boolean validate() {
        //todo if valid transaction create db entry
        return false;
    }

    private void process() {
        if (sender.getBalance() < amount) {
            this.status = TransactionStatus.InsufficientFunds;
        } else {
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);
            this.status = TransactionStatus.Complete;
        }
        updateData();
    }

}
