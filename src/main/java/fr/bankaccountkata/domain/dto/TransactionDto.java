package fr.bankaccountkata.domain.dto;

public class TransactionDto {

    private long amount ;

    public TransactionDto() {
    }

    public TransactionDto(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
