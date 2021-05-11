package fr.bankaccountkata.service;

import fr.bankaccountkata.domain.BankAccountEntity;
import fr.bankaccountkata.domain.TransactionEntity;
import fr.bankaccountkata.domain.TransactionType;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.repository.TransactionRepository;
import fr.bankaccountkata.exceptions.NoSuchAccountException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private AccountDtoMapper dtoMapper;

    public TransactionService(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, AccountDtoMapper dtoMapper) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.dtoMapper = dtoMapper;
    }

    public AccountDto doWithdrawal(long accountId, long amount) throws NoSuchAccountException {
        TransactionEntity transactionEntity = createTransaction(accountId, amount, TransactionType.WITHDRAWAL);
        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(accountId).get();
        bankAccountEntity.getOperations().add(transactionEntity);
        return dtoMapper.mapEntityToDto(bankAccountEntity);
    }

    public AccountDto doDeposit(long accountId, long amount) throws NoSuchAccountException {
        TransactionEntity transactionEntity = createTransaction(accountId, amount, TransactionType.DEPOSIT);
        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(accountId).get();
        bankAccountEntity.getOperations().add(transactionEntity);
        return dtoMapper.mapEntityToDto(bankAccountEntity);
    }

    public TransactionEntity createTransaction(long accountId, long amount, TransactionType transactionType) throws NoSuchAccountException {
        Optional<BankAccountEntity> optionalBankAccount = bankAccountRepository.findById(accountId);
        if (!optionalBankAccount.isPresent()) {
            throw new NoSuchAccountException(": " + accountId);
        }
        BankAccountEntity account = optionalBankAccount.get();
        int opType = transactionType.equals(TransactionType.WITHDRAWAL) ? -1 : 1;
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(opType * amount);
        transactionEntity.setDate(Instant.now());
        transactionEntity.setAccount(account);
        transactionEntity.setType(transactionType);
        account.balance += opType * amount;
        transactionRepository.save(transactionEntity);
        return transactionEntity;
    }
}
