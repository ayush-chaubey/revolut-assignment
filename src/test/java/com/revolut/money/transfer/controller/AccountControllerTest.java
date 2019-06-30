package com.revolut.money.transfer.controller;

import com.revolut.money.transfer.dao.AccountDao;
import com.revolut.money.transfer.dao.AccountDaoImpl;
import com.revolut.money.transfer.domain.Account;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.ws.rs.core.Application;
import java.math.BigDecimal;
import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class AccountControllerTest extends JerseyTest {

    private static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String BALANCE = "balance";
    private static final String USER_NAME = "userName";

    private AccountDao accountDao = null;

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9998;
    }

    @Before
    public void initializeData() {
        accountDao = AccountDaoImpl.getAccountDaoInstance();
        accountDao.clearAllAccounts();
        accountDao.createNewAccount(new Account(100l, "ayush", BigDecimal.valueOf(50.11)));
        accountDao.createNewAccount(new Account(200l, "prakhar", BigDecimal.valueOf(100.21)));
    }

    @Test
    public void accessExistingAccount() {
        expect().statusCode(200).contentType(ContentType.JSON)
                .when().get("/accounts/100");
    }

    @Test
    public void accessInvalidAccount() {
        expect().statusCode(500)
                .when().get("/accounts/101");
    }

    @Test
    public void accessAccountBasedOnDetails() {
        given().when()
                .get("/accounts/100")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(100))
                .body(USER_NAME, equalTo("ayush"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(50.11F)));
    }

    @Test
    public void createAccount() {
        Account newAccount = new Account(300L, "Hari", BigDecimal.valueOf(21.21));

        given().contentType("application/json").body(newAccount)
                .when()
                .post("/accounts")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(300))
                .body(USER_NAME, equalTo("Hari"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(21.21F)));
    }

    @Test
    public void withdrawFromAccount() {
        given().contentType("application/json")
                .when()
                .put("/accounts/200/withdraw/100")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(200))
                .body(USER_NAME, equalTo("prakhar"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(0.21F)));
    }


    @Test
    public void depositToAccount() {
        given().contentType("application/json")
                .when()
                .put("/accounts/200/deposit/1")
                .then()
                .statusCode(200)
                .body(ACCOUNT_NUMBER, equalTo(200))
                .body(USER_NAME, equalTo("prakhar"))
                .body(BALANCE, Matchers.comparesEqualTo(Float.valueOf(101.21F)));
    }

    @Override
    public Application configure() {
        return new ResourceConfig(AccountController.class);
    }
}