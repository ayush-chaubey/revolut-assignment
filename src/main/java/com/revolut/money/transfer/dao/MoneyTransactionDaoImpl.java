package com.revolut.money.transfer.dao;

import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.domain.Transaction;
import com.revolut.money.transfer.exception.InvalidAmountException;
import com.revolut.money.transfer.exception.NotEnoughBalanceException;
import com.revolut.money.transfer.exception.SameAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MoneyTransactionDaoImpl implements MoneyTransactionDao {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyTransactionDaoImpl.class);
    private final AccountDao accountDao = AccountDaoImpl.getAccountDaoInstance();
    private static final String INVALID_TRANSACTION_AMOUNT_EXCEPTION = "Invalid amount hence cannot be transferred";
    private static int WAIT_TIME_TO_ACQUIRE_LOCK = 1000;


    public List<Account> transferMoneyBetweenAccounts(Transaction transaction) {

        List<Account> accounts = new ArrayList<>();
        Account sourceAccount = accountDao.fetchByAccountNumber(transaction.getFromAccountNumber());
        Account targetAccount = accountDao.fetchByAccountNumber(transaction.getToAccountNumber());

        if(sourceAccount.getAccountNumber().equals(targetAccount.getAccountNumber())) {
            LOG.error("Transaction is from the same account to itself");
            throw new SameAccountException(sourceAccount.getAccountNumber());
        }

        BigDecimal amount = transaction.getAmount();
        try {
            if (sourceAccount.getLock().tryLock(WAIT_TIME_TO_ACQUIRE_LOCK, TimeUnit.MILLISECONDS)) {

                if (targetAccount.getLock().tryLock(WAIT_TIME_TO_ACQUIRE_LOCK, TimeUnit.MILLISECONDS)) {
                    try {
                        if (amount.compareTo(new BigDecimal(0)) <= 0) {
                            LOG.error(INVALID_TRANSACTION_AMOUNT_EXCEPTION);
                            throw new InvalidAmountException();
                        }

                        if (sourceAccount.getBalance().compareTo(amount) < 0) {
                            LOG.error("Not enough balance in account to be transferred");
                            throw new NotEnoughBalanceException();
                        }

                        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
                        targetAccount.setBalance(targetAccount.getBalance().add(amount));

                        accounts.add(sourceAccount);
                        accounts.add(targetAccount);

                    } finally {
                        targetAccount.getLock().unlock();
                    }
                }
            }
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        finally {
            sourceAccount.getLock().unlock();
        }
        return accounts;
    }


    public Account depositMoneyToAccount(Long accountNumber, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            LOG.warn(INVALID_TRANSACTION_AMOUNT_EXCEPTION);
            throw new InvalidAmountException();
        }

        final Account account = accountDao.fetchByAccountNumber(accountNumber);
        try {
            if (account.getLock().tryLock(WAIT_TIME_TO_ACQUIRE_LOCK, TimeUnit.MILLISECONDS)) {
                account.setBalance(account.getBalance().add(amount));
            }
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        finally {
            account.getLock().unlock();
        }
        return account;
    }


    public Account withdrawMoneyFromAccount(Long accountNumber, BigDecimal amount) {

        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            LOG.error(INVALID_TRANSACTION_AMOUNT_EXCEPTION);
            throw new InvalidAmountException();
        }

        final Account account = accountDao.fetchByAccountNumber(accountNumber);
        try {
            if (account.getLock().tryLock(WAIT_TIME_TO_ACQUIRE_LOCK, TimeUnit.MILLISECONDS)) {
                account.setBalance(account.getBalance().subtract(amount));
            }
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        finally {
            account.getLock().unlock();
        }
        return account;
    }

    public static MoneyTransactionDao getTransactionDaoInstance() {
        return MoneyTransactionDaoInstance.instance;
    }

    private static class MoneyTransactionDaoInstance {
        private static final MoneyTransactionDao instance = new MoneyTransactionDaoImpl();
    }
}
