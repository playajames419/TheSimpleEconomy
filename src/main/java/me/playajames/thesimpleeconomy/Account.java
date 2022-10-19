package me.playajames.thesimpleeconomy;

import java.util.UUID;

public class Account {

    private final UUID ownerUUID;
    private double balance;

    public Account(UUID ownerUUID, double startingBalance) {
        this.ownerUUID = ownerUUID;
        this.balance = startingBalance;
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
        //TODO implement
    }
}
