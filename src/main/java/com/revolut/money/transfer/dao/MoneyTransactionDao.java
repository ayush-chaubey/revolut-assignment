package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.domain.Transaction;
import java.math.BigDecimal;
import java.util.List;

/**
 * Provides tbe transactions between accounts and deposit/withdraw features
 */
public interface MoneyTransactionDao {
    List<Account> transferMoneyBetweenAccounts(Transaction transaction);
    Account depositMoneyToAccount(Long accountNumber, BigDecimal amount);
    Account withdrawMoneyFromAccount(Long accountNumber, BigDecimal amount);
}
