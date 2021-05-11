package fr.bankaccountkata.domain.dto;

import fr.bankaccountkata.domain.TransactionEntity;

import java.util.List;

public class AccountDto {

    private long accountId;

    private long balance;

    private List<TransactionEntity> transactions;

    public AccountDto() {
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }
}
