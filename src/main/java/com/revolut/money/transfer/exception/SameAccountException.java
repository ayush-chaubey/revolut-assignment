package com.revolut.money.transfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SameAccountException extends RuntimeException implements ExceptionMapper<SameAccountException> {

    public SameAccountException(Long accountId) {
        super("Source and Target accounts are same : " + accountId);
    }

    public SameAccountException() {
        super();
    }

    @Override
    public Response toResponse(SameAccountException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
