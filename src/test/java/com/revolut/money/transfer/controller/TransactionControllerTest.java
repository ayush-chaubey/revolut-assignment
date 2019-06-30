package com.revolut.money.transfer.controller;

import com.revolut.money.transfer.dao.AccountDao;
import com.revolut.money.transfer.dao.AccountDaoImpl;
import com.revolut.money.transfer.domain.Account;
import com.revolut.money.transfer.domain.Transaction;
import io.restassured.RestAssured;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.ws.rs.core.Application;
import java.math.BigDecimal;
import java.util.Arrays;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class TransactionControllerTest extends JerseyTest {

    private static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String USER_NAME = "userName";

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9998;
    }

    @Test
    public void correctTransactionShouldSucceed() {
        AccountDao accountDao = AccountDaoImpl.getAccountDaoInstance();
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(50.11)));
        accountDao.createNewAccount(new Account(200l, "prakhar", BigDecimal.valueOf(100.21)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(10), 100l, 200l);
        given().contentType("application/json").body(transaction)
                .when()
                .post("/transaction")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(Arrays.asList(100,200)))
                .body(USER_NAME, equalTo(Arrays.asList("ayush", "prakhar")));
    }

    @Test
    public void incorrectTransactionShouldFail() {
        AccountDao accountDao = AccountDaoImpl.getAccountDaoInstance();
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(50.11)));
        accountDao.createNewAccount(new Account(200l, "prakhar", BigDecimal.valueOf(100.21)));

        Transaction transaction = new Transaction(BigDecimal.valueOf(10), 900l, 200l);

        given().contentType("application/json").body(transaction)
                .when()
                .post("/transaction")
                .then()
                .statusCode(500);
    }

    @Override
    public Application configure() {
        return new ResourceConfig(TransactionController.class);
    }

}
