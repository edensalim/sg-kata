package fr.bankaccountkata.service;


import fr.bankaccountkata.domain.BankAccountEntity;
import fr.bankaccountkata.domain.TransactionEntity;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.exceptions.NoSuchAccountException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final AccountDtoMapper accountDtoMapper;

    public BankAccountService(BankAccountRepository bankAccountRepository, AccountDtoMapper accountDtoMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountDtoMapper = accountDtoMapper;
    }

    public List<TransactionEntity> listAllTransactions(long accountId) throws NoSuchAccountException {
        Optional<BankAccountEntity> optionalBankAccount = bankAccountRepository.findById(accountId);
        if (!optionalBankAccount.isPresent()) {
            throw new NoSuchAccountException(": " + accountId);
        }
        return optionalBankAccount.get().transactionEntities;
    }

    public AccountDto viewAccount(long accountId) throws NoSuchAccountException {
        Optional<BankAccountEntity> optionalBankAccount = bankAccountRepository.findById(accountId);
        if (!optionalBankAccount.isPresent()) {
            throw new NoSuchAccountException(": " + accountId);
        }
        return accountDtoMapper.mapEntityToDto(optionalBankAccount.get());
    }

    public AccountDto createAccount() throws NoSuchAccountException {

        return accountDtoMapper.mapEntityToDto(bankAccountRepository.save(new BankAccountEntity()));
    }
}
