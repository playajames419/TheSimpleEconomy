package me.playajames.thesimpleeconomy;

public class Transaction {

    private final Account sender;
    private final Account receiver;
    private final double amount;

    public Transaction(Account sender, Account receiver, double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
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

    private void validate() {
        //todo if valid transaction create db entry
    }
}
