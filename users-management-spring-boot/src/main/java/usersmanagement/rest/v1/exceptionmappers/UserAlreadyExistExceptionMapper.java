package usersmanagement.rest.v1.exceptionmappers;

import usersmanagement.domain.exceptions.UserAlreadyExistException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserAlreadyExistExceptionMapper implements ExceptionMapper<UserAlreadyExistException> {
    @Override
    public Response toResponse(UserAlreadyExistException e) {
        return Response.status(Response.Status.CONFLICT).build(); // TODO complete
    }
}
