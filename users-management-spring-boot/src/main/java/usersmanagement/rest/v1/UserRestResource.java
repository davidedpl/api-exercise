package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.UserType;
import usersmanagement.domain.controller.UserController;
import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.user.UserUpdateHelper;
import usersmanagement.rest.v1.assembler.CreateUserAssembler;
import usersmanagement.rest.v1.assembler.UserUpdateHelperAssembler;
import usersmanagement.rest.v1.dto.UserResponse;
import usersmanagement.rest.v1.dto.UsersCollectionResponse;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

@Component
@Path(UserRestResource.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces("application/hal+json")
public class UserRestResource {

    public final static String PATH = "/v1/users";
    public final static String PATH_WITH_ID = PATH + "/{username}";

    public static final String HAL_JSON = "application/hal+json";

    private final CreateUserAssembler createUserAssembler;
    private final UserUpdateHelperAssembler userUpdateHelperAssembler;
    private final UserController userController;

    private static final Log LOG = LogFactory.getLog(UserRestResource.class);

    @Inject
    public UserRestResource(
            CreateUserAssembler createUserAssembler, UserUpdateHelperAssembler userUpdateHelperAssembler,
            UserController userController) {
        this.createUserAssembler = createUserAssembler;
        this.userUpdateHelperAssembler = userUpdateHelperAssembler;
        this.userController = userController;
    }

    @GET
    public Response readAllUsers(
            @HeaderParam("type") UserType clientUserType,
            @HeaderParam("username") String clientUserName,
            @Context UriInfo uriInfo) {
        try {
            Collection<User> users = userController
                    .readAll(new UserAuthenticationAttributes(clientUserName, clientUserType));
            return Response.ok(new UsersCollectionResponse(users, uriInfo)).build();
        } catch (Throwable t) {
            return mapException(t);
        }
    }

    @GET
    @Path("{username}")
    public Response readUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username,
            @Context UriInfo uriInfo) {
        try {
            final User retrievedUser = userController
                    .readUser(new UserAuthenticationAttributes(clientUserName, clientUserType), username);
            return Response.ok(UserResponse.fromUser(retrievedUser, uriInfo)).build();
        } catch (Throwable t) {
            return mapException(t);
        }
    }

    @POST
    public Response registerUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("type") UserType clientUserType,
            @Context UriInfo uriInfo,
            JsonNode createUser) {
        try {
            User userToRegister = createUserAssembler.assemble(createUser);
            userController.registerUser(new UserAuthenticationAttributes(clientUserName, clientUserType), userToRegister);
            URI userUri = uriInfo.getBaseUriBuilder().path(PATH + "/" + userToRegister.getUsername()).build();
            return Response.created(userUri).build();
        } catch (Throwable t) {
            return mapException(t);
        }
    }

    @PUT
    @Path("{username}")
    public Response updateUser(
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username,
            JsonNode updateUser) {
        try {
            UserUpdateHelper updateHelper = userUpdateHelperAssembler.assemble(updateUser);
            userController.updateUser(new UserAuthenticationAttributes(clientUserType), username, updateHelper);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Throwable t) {
            return mapException(t);
        }
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username) {
        try {
            userController.deleteUser(new UserAuthenticationAttributes(clientUserType), username);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Throwable t) {
            return mapException(t);
        }
    }

    // mapping exception in this way since Jersey exception-mappers are not working properly
    private Response mapException(Throwable t) {
        LOG.error(t.getMessage(), t);
        if (t instanceof SecurityException) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (t instanceof UserAlreadyExistException) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        if (t instanceof UserNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (t instanceof ValidationException) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if(t instanceof WebApplicationException) {
            return Response.status(((WebApplicationException)t).getResponse().getStatus()).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
