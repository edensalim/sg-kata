package fr.bankaccountkata.domain;

public enum TransactionType {

    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal");

    String transaction;

    TransactionType(String transaction){
        this.transaction = transaction;
    }
}
