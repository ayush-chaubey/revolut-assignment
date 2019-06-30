package com.revolut.money.transfer.controller;

import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.domain.Transaction;
import com.revolut.money.transfer.dao.MoneyTransactionDao;
import com.revolut.money.transfer.dao.MoneyTransactionDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transaction")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final MoneyTransactionDao moneyTransactionDao = MoneyTransactionDaoImpl.getTransactionDaoInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferMoney(Transaction transaction) {
        List<Account> updatedAccount = moneyTransactionDao.transferMoneyBetweenAccounts(transaction);
        LOG.info("Amount of {} transferred from account {} to the account {}", transaction.getAmount(), transaction.getFromAccountNumber(),
                transaction.getToAccountNumber());
        return Response.ok(updatedAccount).build();
    }
}
