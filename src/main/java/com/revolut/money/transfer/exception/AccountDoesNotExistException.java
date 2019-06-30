package com.revolut.money.transfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountDoesNotExistException extends RuntimeException implements ExceptionMapper<AccountDoesNotExistException> {

    public AccountDoesNotExistException(Long account) {
        super("Account does not exist in the records | Account number = " + account);
    }

    public AccountDoesNotExistException() {
        super();
    }

    @Override
    public Response toResponse(AccountDoesNotExistException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
