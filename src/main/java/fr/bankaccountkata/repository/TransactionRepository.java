package fr.bankaccountkata.repository;

import fr.bankaccountkata.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
}
