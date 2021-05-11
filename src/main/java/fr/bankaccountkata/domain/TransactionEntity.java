package fr.bankaccountkata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;


@Entity
@Table(name = "transaction")
public class TransactionEntity implements Serializable{

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        private Long id;

        private Instant date;

        @Enumerated(EnumType.STRING)
        private TransactionType type;

        private Long amount ;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "account_id")
        private BankAccountEntity account;

        public TransactionEntity() {
        }

        public TransactionEntity(Instant date, TransactionType type, long amount, BankAccountEntity account) {
                this.date = date;
                this.type = type;
                this.amount = amount;
                this.account = account;
        }

        public BankAccountEntity getAccount() {
                return account;
        }

        public void setAccount(BankAccountEntity account) {
                this.account = account;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Instant getDate() {
                return date;
        }

        public void setDate(Instant date) {
                this.date = date;
        }

        public TransactionType getType() {
                return type;
        }

        public void setType(TransactionType type) {
                this.type = type;
        }

        public Long getAmount() {
                return amount;
        }

        public void setAmount(Long amount) {
                this.amount = amount;
        }
}
