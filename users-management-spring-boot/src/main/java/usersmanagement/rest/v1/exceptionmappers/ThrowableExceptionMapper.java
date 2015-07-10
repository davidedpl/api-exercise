package usersmanagement.rest.v1.exceptionmappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    public static final int GENERIC_ERROR_CODE = 0;

    private static final Log LOG = LogFactory.getLog(ThrowableExceptionMapper.class);

    @Override
    public Response toResponse(Throwable ex) {

        LOG.error(ex.getMessage(), ex);

        ErrorMessage errorMessage = new ErrorMessage(GENERIC_ERROR_CODE, getHttpStatus(ex), ex.getMessage());
        StringWriter errorStackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(errorStackTrace));

        return Response.status(errorMessage.getStatus())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private int getHttpStatus(Throwable ex) {
        if(ex instanceof WebApplicationException) {
            return ((WebApplicationException)ex).getResponse().getStatus();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
    }

    public static class ErrorMessage {
        private final int code;
        private final int status;
        private final String message;

        private ErrorMessage(int code, int status, String message) {
            this.code = code;
            this.status = status;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonIgnore
        public int getStatus() {
            return status;
        }
    }
}
