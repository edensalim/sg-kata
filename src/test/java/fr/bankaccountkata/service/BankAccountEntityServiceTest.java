package fr.bankaccountkata.service;


import fr.bankaccountkata.domain.BankAccountEntity;
import fr.bankaccountkata.domain.TransactionEntity;
import fr.bankaccountkata.domain.TransactionType;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.exceptions.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BankAccountEntityServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private AccountDtoMapper accountDtoMapper;

    @InjectMocks
    private BankAccountService bankAccountService;

    private List<TransactionEntity> transactionEntities;
    private BankAccountEntity account ;
    @Before
    public void setUp(){
        account = new BankAccountEntity();
        account.setBalance(5000);
        account.setId(12L);
        transactionEntities = new ArrayList<>();
        transactionEntities.add(new TransactionEntity(Instant.now(), TransactionType.DEPOSIT,10000,account));
        account.setOperations(transactionEntities);
    }

    @Test(expected = NoSuchAccountException.class)
    public void listAllTransactions_should_throw_exception_for_no_such_account() throws Exception {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        bankAccountService.listAllTransactions(12L);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void listAllTransactions_should_successfully_return_all_account_transactions() throws NoSuchAccountException {
        when(bankAccountRepository.findById(12L)).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccountEntity.class))).thenCallRealMethod();
        List<TransactionEntity> transactionEntities = bankAccountService.listAllTransactions(12L);
        assertThat(transactionEntities).isNotEmpty();
        assertThat(transactionEntities).hasSize(1);
    }

    @Test(expected = NoSuchAccountException.class)
    public void viewAccount_should_throw_exception_for_no_such_account() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        bankAccountService.viewAccount(12L);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void viewAccount_should_successfully_return_current_account_balance() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccountEntity.class))).thenCallRealMethod();
        AccountDto accountDto = bankAccountService.viewAccount(12L);
        assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());
        assertThat(accountDto.getTransactions()).isNotEmpty();
        assertThat(accountDto.getTransactions()).hasSameSizeAs(account.getOperations());

        TransactionEntity transactionEntity = new TransactionEntity(Instant.now().minusSeconds(10000), TransactionType.DEPOSIT,10000,account);
        account.getOperations().add(transactionEntity);
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        accountDto = bankAccountService.viewAccount(12L);
        assertThat(accountDto.getTransactions()).hasSize(2);
        assertThat(accountDto.getTransactions()).isSortedAccordingTo(Comparator.comparing(TransactionEntity::getDate).reversed());

    }




}
