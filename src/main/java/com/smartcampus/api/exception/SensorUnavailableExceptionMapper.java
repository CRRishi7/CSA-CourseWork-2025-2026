package com.smartcampus.api.exception;

import com.smartcampus.api.model.ErrorResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorResponse error = new ErrorResponse(403, "Forbidden", exception.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(error).build();
    }
}
