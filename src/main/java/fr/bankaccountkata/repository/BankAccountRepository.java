package fr.bankaccountkata.repository;

import fr.bankaccountkata.domain.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository  extends JpaRepository<BankAccountEntity,Long>{
}
