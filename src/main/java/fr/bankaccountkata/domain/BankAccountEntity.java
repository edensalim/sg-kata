package fr.bankaccountkata.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "account")
public class BankAccountEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    public long balance;

    @OneToMany(mappedBy = "account")
    public List<TransactionEntity> transactionEntities = new ArrayList<>();

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<TransactionEntity> getOperations() {
        return transactionEntities;
    }

    public void setOperations(List<TransactionEntity> transactionEntities) {
        this.transactionEntities = transactionEntities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
