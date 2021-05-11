package fr.bankaccountkata.controller;


import fr.bankaccountkata.domain.TransactionEntity;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.domain.dto.TransactionDto;
import fr.bankaccountkata.service.BankAccountService;
import fr.bankaccountkata.service.TransactionService;
import fr.bankaccountkata.exceptions.NoSuchAccountException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    public BankAccountController(BankAccountService bankAccountService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @GetMapping("{accountId}/viewAccount")
    public AccountDto viewAccountState(@PathVariable long accountId) throws NoSuchAccountException {
        return bankAccountService.viewAccount(accountId);
    }

    @GetMapping("{accountId}/history")
    public List<TransactionEntity> showTransactions(@PathVariable long accountId) throws NoSuchAccountException {
        return bankAccountService.listAllTransactions(accountId);
    }

    @PutMapping(value = "{accountId}/deposit")
    public AccountDto deposit(@PathVariable long accountId,
                              @RequestBody TransactionDto transactionDto) throws NoSuchAccountException {
        return transactionService.doDeposit(accountId, transactionDto.getAmount());
    }

    @PutMapping(value = "{accountId}/withdrawal")
    public AccountDto withdrawal(@PathVariable long accountId,
                                 @RequestBody TransactionDto transactionDto) throws NoSuchAccountException {
        return transactionService.doWithdrawal(accountId, transactionDto.getAmount());
    }

    @PutMapping(value = "/createAccount")
    public AccountDto createAccount() throws NoSuchAccountException {
        return bankAccountService.createAccount();
    }

}
