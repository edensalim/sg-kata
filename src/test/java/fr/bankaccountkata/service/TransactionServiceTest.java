package fr.bankaccountkata.service;


import fr.bankaccountkata.domain.BankAccountEntity;
import fr.bankaccountkata.domain.TransactionEntity;
import fr.bankaccountkata.domain.TransactionType;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.repository.TransactionRepository;
import fr.bankaccountkata.exceptions.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {


    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountDtoMapper accountDtoMapper;

    @InjectMocks
    private TransactionService transactionService;

    private BankAccountEntity account ;
    private TransactionEntity transactionEntity;
    @Before
    public void setUp(){
        account = new BankAccountEntity();
        account.setBalance(5000);
        account.setId(12L);
        transactionEntity = new TransactionEntity(Instant.now(), TransactionType.DEPOSIT,10000, null);
    }

    @Test(expected = NoSuchAccountException.class)
    public void createAndPerformOperation_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        transactionService.createTransaction(12L,0, TransactionType.WITHDRAWAL);
        Assert.fail("should have thrown NoSuchAccountException ");

    }

    @Test
    public void createAndPerformOperation_should_perform_deposit() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        long currentAccountBalance = account.getBalance();
        TransactionEntity transactionEntity = transactionService.createTransaction(12L,1000, TransactionType.DEPOSIT);
        assertThat(transactionEntity.getAmount()).isEqualTo(1000);
        assertThat(transactionEntity.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transactionEntity.getAccount()).isNotNull();
        assertThat(transactionEntity.getAccount().getBalance()).isEqualTo(currentAccountBalance+1000);
    }

    @Test
    public void createAndPerformOperation_should_perform_withdrawal() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        long currentAccountBalance = account.getBalance();
        TransactionEntity transactionEntity = transactionService.createTransaction(12L,5000, TransactionType.WITHDRAWAL);
        assertThat(transactionEntity.getAmount()).isEqualTo(-5000);
        assertThat(transactionEntity.getType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(transactionEntity.getAccount()).isNotNull();
        assertThat(transactionEntity.getAccount().getBalance()).isEqualTo(currentAccountBalance-5000);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doDeposit_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        transactionService.doDeposit(12L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void doDeposit_should_perform_deposit_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccountEntity.class))).thenCallRealMethod();
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);
        long currentAccountBalance = account.getBalance();
        AccountDto dto = transactionService.doDeposit(12L,1200);
        assertThat(dto.getTransactions().size()).isEqualTo(1);
        assertThat(dto.getTransactions().get(0).getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(dto.getTransactions().get(0).getAmount()).isEqualTo(1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance+1200);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doWithdrawal_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        transactionService.doWithdrawal(12L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void doWithdrawal_should_perform_withdrawal_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccountEntity.class))).thenCallRealMethod();
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);
        long currentAccountBalance = account.getBalance();
        AccountDto dto = transactionService.doWithdrawal(12L,1200);
        assertThat(dto.getTransactions().size()).isEqualTo(1);
        assertThat(dto.getTransactions().get(0).getType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(dto.getTransactions().get(0).getAmount()).isEqualTo(-1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance-1200);
    }

}
