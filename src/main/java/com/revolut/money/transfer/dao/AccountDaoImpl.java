package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.exception.AccountAlreadyExistsException;
import com.revolut.money.transfer.exception.AccountDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class AccountDaoImpl implements AccountDao {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDaoImpl.class);

    private final ConcurrentMap<Long, Account> accounts;

    public AccountDaoImpl() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public Account fetchByAccountNumber(Long accountNumber) {
        final Account account = accounts.get(accountNumber);
        if (account == null) {
            LOG.error("Account {} does not exist in the records", accountNumber);
            throw new AccountDoesNotExistException(accountNumber);
        }
        return account;
    }

    public Collection<Account> fetchAllAccounts() {
        return accounts.values();
    }

    public void createNewAccount(Account account) {
        Account existingAccount = accounts.putIfAbsent(account.getAccountNumber(), account);

        if (existingAccount != null) {
            LOG.error("Account with account number {} is already Present", account.getAccountNumber());
            throw new AccountAlreadyExistsException(existingAccount.getAccountNumber());
        }
    }

    public void clearAllAccounts() {
        accounts.clear();
    }

    public void deleteAccountByAccountNumber(Long accountNumber) {
        final Account account = accounts.get(accountNumber);

        if (account == null) {
            LOG.error("Account with account number {} is does not exist", accountNumber);
            throw new AccountDoesNotExistException(accountNumber);
        }
        accounts.remove(accountNumber);
    }

    public static AccountDao getAccountDaoInstance() {
        return AccountDaoInstance.instance;
    }

    private static class AccountDaoInstance {
        private static final AccountDao instance = new AccountDaoImpl();
    }
}