package fr.bankaccountkata.service;

import fr.bankaccountkata.domain.BankAccountEntity;
import fr.bankaccountkata.domain.TransactionEntity;
import fr.bankaccountkata.domain.dto.AccountDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountDtoMapper {

    public AccountDto mapEntityToDto(BankAccountEntity account) {
        AccountDto dto = new AccountDto();
        dto.setAccountId(account.getId());
        dto.setBalance(account.getBalance());
        List<TransactionEntity> recentOps = account.getOperations()
                .stream()
                .sorted(Comparator.comparing(TransactionEntity::getDate).reversed())
                .collect(Collectors.toList());
        dto.setTransactions(recentOps);
        return dto;
    }
}
