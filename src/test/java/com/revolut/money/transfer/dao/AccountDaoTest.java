package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.exception.AccountAlreadyExistsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.stream.LongStream;
import static org.junit.Assert.assertEquals;


public class AccountDaoTest {

    private AccountDaoImpl accountDao;

    @Before
    public void setUp() {
        accountDao = new AccountDaoImpl();
    }

    @Test
    public void addAccount() {
        accountDao.clearAllAccounts();
        Account anyAccount = new Account(100l, "ayush", BigDecimal.valueOf(500.55));

        accountDao.createNewAccount(anyAccount);

        assertEquals(1, accountDao.fetchAllAccounts().size());
        Assert.assertEquals(anyAccount, accountDao.fetchByAccountNumber(anyAccount.getAccountNumber()));
    }

    @Test(expected = AccountAlreadyExistsException.class)
    public void createDuplicateAccounts() {
        accountDao.clearAllAccounts();
        Account account = new Account(100l, "ayush", BigDecimal.valueOf(500.55));
        accountDao.createNewAccount(account);
        Account accountWithExistingId = new Account(100l, "prakhar", BigDecimal.valueOf(999.99));
        accountDao.createNewAccount(accountWithExistingId);
        assertEquals(1, accountDao.fetchAllAccounts().size());
    }

    @Test
    public void clearAllAccounts() {
        accountDao.clearAllAccounts();
        LongStream.range(0, 3).forEach(a -> accountDao.createNewAccount(new Account(a, "1" + a, BigDecimal.TEN)));

        assertEquals(3, accountDao.fetchAllAccounts().size());

        accountDao.clearAllAccounts();

        assertEquals(0, accountDao.fetchAllAccounts().size());
    }

    @Test
    public void fetchAccountDetails() {
        Account account = new Account(100l, "ayush", BigDecimal.valueOf(500.55));

        accountDao.createNewAccount(account);

        Account retrievedAccount = accountDao.fetchByAccountNumber(100l);

        assertEquals(account, retrievedAccount);
    }


}