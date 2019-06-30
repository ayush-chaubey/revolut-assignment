package com.revolut.money.transfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountAlreadyExistsException extends RuntimeException implements ExceptionMapper<AccountAlreadyExistsException> {

    public AccountAlreadyExistsException(Long accountId) {
        super("Account with account Number:" + accountId + " already exists");
    }

    public AccountAlreadyExistsException() {
        super();
    }


    @Override
    public Response toResponse(AccountAlreadyExistsException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
