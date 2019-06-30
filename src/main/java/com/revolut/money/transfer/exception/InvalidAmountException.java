package com.revolut.money.transfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidAmountException extends RuntimeException implements ExceptionMapper<InvalidAmountException> {

    public InvalidAmountException() {
        super("Amount given is invalid. Cannot proceed with transaction");
    }

    @Override
    public Response toResponse(InvalidAmountException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
