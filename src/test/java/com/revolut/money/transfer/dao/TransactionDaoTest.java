package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.domain.Transaction;
import com.revolut.money.transfer.exception.AccountDoesNotExistException;
import com.revolut.money.transfer.exception.InvalidAmountException;
import com.revolut.money.transfer.exception.NotEnoughBalanceException;
import com.revolut.money.transfer.exception.SameAccountException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TransactionDaoTest {

    private AccountDao accountDao;
    private MoneyTransactionDao transactionDao;

    @Before
    public void setUp() {
        accountDao = AccountDaoImpl.getAccountDaoInstance();
        transactionDao = MoneyTransactionDaoImpl.getTransactionDaoInstance();
    }

    @Test
    public void successfulTransaction() {
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(500.55)));
        accountDao.createNewAccount(new Account(200l, "prakhar", BigDecimal.valueOf(220.20)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(100), 100l, 200l);

        List<Account> account = transactionDao.transferMoneyBetweenAccounts(transaction);
        assertEquals(320.20, account.get(1).getBalance().doubleValue(), 0);

    }

    @Test(expected = AccountDoesNotExistException.class)
    public void accountNotPresentTransaction() {
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(500.55)));
        Transaction transaction = new Transaction(BigDecimal.valueOf(100), 100l, 200l);
        transactionDao.transferMoneyBetweenAccounts(transaction);
    }

    @Test(expected = NotEnoughBalanceException.class)
    public void InSufficientBalanceInAccountForTransaction() {
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(99.99)));
        accountDao.createNewAccount(new Account(200l, "prakhar", BigDecimal.valueOf(220.20)));
        Transaction transaction = new Transaction(BigDecimal.valueOf(200), 100l, 200l);
        transactionDao.transferMoneyBetweenAccounts(transaction);
    }

    @Test(expected =  SameAccountException.class)
    public void sameAccountTransaction() {
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(500.55)));
        accountDao.createNewAccount(new Account(200l, "prakhar", BigDecimal.valueOf(220.20)));
        Transaction transaction = new Transaction(BigDecimal.valueOf(500), 100l, 100l);
        transactionDao.transferMoneyBetweenAccounts(transaction);
    }

    @Test(expected =  InvalidAmountException.class)
    public void invalidAmountTransaction() {
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(500.55)));
        accountDao.createNewAccount(new Account(200l, "prakhar", BigDecimal.valueOf(220.20)));
        Transaction transaction = new Transaction(BigDecimal.valueOf(-500), 100l, 200l);
        transactionDao.transferMoneyBetweenAccounts(transaction);
    }


    @Test
    public void depositToAccount() {
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(99l, "ayush", BigDecimal.valueOf(102.10)));
        Account account = transactionDao.depositMoneyToAccount(99l, BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(202.10), account.getBalance());
    }

    @Test
    public void withDrawFromAccount() {
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(99l, "ayush", BigDecimal.valueOf(302.10)));
        Account account = transactionDao.withdrawMoneyFromAccount(99l, BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(202.10), account.getBalance());
    }
}