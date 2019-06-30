package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.domain.Account;
import java.util.Collection;

/**
 * Provides all the account operations that the money transfer application
 */
public interface AccountDao {
    Account fetchByAccountNumber(Long accountNumber);
    Collection<Account> fetchAllAccounts();
    void createNewAccount(Account account);
    void clearAllAccounts();
    void deleteAccountByAccountNumber(Long accountNumber);
}
