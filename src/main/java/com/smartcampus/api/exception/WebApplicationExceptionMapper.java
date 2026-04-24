package com.smartcampus.api.exception;

import com.smartcampus.api.model.ErrorResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();
        String reason = exception.getResponse().getStatusInfo().getReasonPhrase();
        String message = exception.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Request could not be processed.";
        }
        ErrorResponse error = new ErrorResponse(status, reason, message);
        return Response.status(status).entity(error).build();
    }
}
