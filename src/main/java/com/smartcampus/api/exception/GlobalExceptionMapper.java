package com.smartcampus.api.exception;

import com.smartcampus.api.model.ErrorResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        ErrorResponse error = new ErrorResponse(
                500,
                "Internal Server Error",
                "An unexpected error occurred."
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}
