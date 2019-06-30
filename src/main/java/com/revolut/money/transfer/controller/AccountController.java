package com.revolut.money.transfer.controller;

import com.revolut.money.transfer.dao.AccountDao;
import com.revolut.money.transfer.dao.AccountDaoImpl;
import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.dao.MoneyTransactionDao;
import com.revolut.money.transfer.dao.MoneyTransactionDaoImpl;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Collections;


@Path("/accounts")
@RequestScoped
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);
    private final AccountDao accountDao = AccountDaoImpl.getAccountDaoInstance();
    private final MoneyTransactionDao transactionDao = MoneyTransactionDaoImpl.getTransactionDaoInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        LOG.info("Total Number of accounts fetched = {}", accountDao.fetchAllAccounts().size());
        return Response.ok(Collections.unmodifiableCollection(accountDao.fetchAllAccounts())).build();
    }

    @GET
    @Path("{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountByAccountNumber(@PathParam("accountNumber") Long accountNumber) {
        Account account = accountDao.fetchByAccountNumber(accountNumber);
        LOG.info("Fethed account details : {}", account.toString());
        return Response.ok(account).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewAccount(Account account) {
        accountDao.createNewAccount(account);
        LOG.info("New account created with account number = {}", account.getAccountNumber());
        return Response.ok(account).build();
    }

    @GET
    @Path("/{accountNumber}/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(@PathParam("accountNumber") Long accountId) {
        final Account account = accountDao.fetchByAccountNumber(accountId);
        LOG.debug("Balance account details {}", account.toString());
        return Response.ok(account).build();
    }

    @PUT
    @Path("/{accountNumber}/deposit/{amount}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(@PathParam("accountNumber") Long accountId, @PathParam("amount") BigDecimal amount) {
        Account account = transactionDao.depositMoneyToAccount(accountId, amount);
        return Response.ok(account).build();
    }

    @PUT
    @Path("/{accountNumber}/withdraw/{amount}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(@PathParam("accountNumber") long accountId, @PathParam("amount") BigDecimal amount) {
        Account account = transactionDao.withdrawMoneyFromAccount(accountId, amount);
        LOG.debug("Amount of {} debited from the account", amount);
        return Response.ok(account).build();
    }

    @DELETE
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeAccount(@PathParam("accountId") long accountId) {

        accountDao.deleteAccountByAccountNumber(accountId);
        LOG.info("Account number {} successfully removed", accountId);
        return Response.ok("Account Deleted with Account number " + accountId).build();
    }

}
