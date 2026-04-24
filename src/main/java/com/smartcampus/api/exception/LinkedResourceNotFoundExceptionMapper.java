package com.smartcampus.api.exception;

import com.smartcampus.api.model.ErrorResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(422, "Unprocessable Entity", exception.getMessage());
        return Response.status(422).entity(error).build();
    }
}
