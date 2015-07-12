package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.UserType;
import usersmanagement.domain.service.UserService;
import usersmanagement.domain.utils.UserUpdateHelper;
import usersmanagement.rest.v1.assembler.CreateUserAssembler;
import usersmanagement.rest.v1.assembler.UserUpdateHelperAssembler;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Component
@Path(UserRestController.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
//@Produces(RepresentationFactory.HAL_JSON)
public class UserRestController {

    public final static String PATH = "/v1/users";

    private final CreateUserAssembler createUserAssembler;
    private final UserUpdateHelperAssembler userUpdateHelperAssembler;
    private final UserService userService;

    @Inject
    public UserRestController(
//            CreateUserAssembler createUserAssembler,
//                              UserUpdateHelperAssembler userUpdateHelperAssembler,
            UserService userService) {
        this.createUserAssembler = null;
        this.userUpdateHelperAssembler = null;
        this.userService = userService;
    }

    @GET
    @Path("{username}")
    public Response readUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username) {
        return Response.ok(
                userService.readUser(new UserAuthenticationAttributes(clientUserName, clientUserType), username))
                .build();
    }

    @POST
    public Response registerUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("type") UserType clientUserType,
            @Context UriInfo uriInfo,
            JsonNode createUser) {
        User userToRegister = createUserAssembler.assemble(createUser);
        userService.registerUser(new UserAuthenticationAttributes(clientUserType), userToRegister);
        URI userUri = uriInfo.getBaseUriBuilder().path(PATH + "/" + userToRegister.getUsername()).build();
        return Response.created(userUri).build();
    }

    @PUT
    @Path("{username}")
    public Response updateUser(
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username,
            JsonNode updateUser) {
        UserUpdateHelper updateHelper = userUpdateHelperAssembler.assemble(updateUser);
        userService.updateUser(new UserAuthenticationAttributes(clientUserType), username, updateHelper);
        return Response.ok(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username) {
        userService.deleteUser(new UserAuthenticationAttributes(clientUserType), username);
        return Response.ok(Response.Status.NO_CONTENT).build();
    }

}
