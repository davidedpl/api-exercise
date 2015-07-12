package usersmanagement.rest.v1.exceptionmappers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Log LOG = LogFactory.getLog(ThrowableExceptionMapper.class);

    @Override
    public Response toResponse(Throwable e) {
        LOG.error(e.getMessage(), e);
        return Response.status(getHttpStatus(e)).type(MediaType.APPLICATION_JSON).build();
    }

    private int getHttpStatus(Throwable ex) {
        if(ex instanceof WebApplicationException) {
            return ((WebApplicationException)ex).getResponse().getStatus();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
    }

}
