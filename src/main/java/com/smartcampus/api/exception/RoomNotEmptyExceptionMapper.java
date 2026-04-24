package com.smartcampus.api.exception;

import com.smartcampus.api.model.ErrorResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorResponse error = new ErrorResponse(409, "Conflict", exception.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(error).build();
    }
}
