package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.UserType;
import usersmanagement.domain.controller.UserController;
import usersmanagement.domain.security.UserAuthenticationAttributes;
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
import java.util.List;

@Component
@Path(UserRestResource.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces("application/hal+json")
public class UserRestResource {

    public final static String PATH = "/v1/users";
    public final static String PATH_WITH_ID = PATH + "/{username}";

    private final CreateUserAssembler createUserAssembler;
    private final UserUpdateHelperAssembler userUpdateHelperAssembler;
    private final UserController userController;

    @Inject
    public UserRestResource(
            CreateUserAssembler createUserAssembler, UserUpdateHelperAssembler userUpdateHelperAssembler,
            UserController userController) {
        this.createUserAssembler = createUserAssembler;
        this.userUpdateHelperAssembler = userUpdateHelperAssembler;
        this.userController = userController;
    }

    @GET
    public Response readAllUsers(@HeaderParam("type") UserType clientUserType) {
        return Response.ok(
                userController.readAll(new UserAuthenticationAttributes(clientUserType)))
                .build();
    }

    @GET
    @Path("{username}")
    public Response readUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username,
            @Context UriInfo uriInfo) {
        final User retrievedUser = userController
                .readUser(new UserAuthenticationAttributes(clientUserName, clientUserType), username);
        return Response.ok(
                getUserRepresentation(retrievedUser, uriInfo))
                .build();
    }

    @POST
    public Response registerUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("type") UserType clientUserType,
            @Context UriInfo uriInfo,
            JsonNode createUser) {
        User userToRegister = createUserAssembler.assemble(createUser);
        userController.registerUser(new UserAuthenticationAttributes(clientUserType), userToRegister);
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
        userController.updateUser(new UserAuthenticationAttributes(clientUserType), username, updateHelper);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(
            @HeaderParam("type") UserType clientUserType,
            @PathParam("username") String username) {
        userController.deleteUser(new UserAuthenticationAttributes(clientUserType), username);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    private Object getUserRepresentation(User user, UriInfo uriInfo) {
//        Representation representation = representationFactory.newRepresentation(
//                UriBuilder.fromUri(uriInfo.getBaseUri()).path(UserRestResource.PATH_WITH_ID)
//                        .build(String.valueOf(user.getUsername())).toString());
//
//        representation.withProperty("type", user.getType());
//        representation.withProperty("title", user.getTitle());
//        representation.withProperty("firstName", user.getFirstName());
//        representation.withProperty("lastName", user.getLastName());
//        representation.withProperty("dateOfBirth", user.getDateOfBirth().toString());
//        representation.withProperty("email", user.getEmail());
//
//        if (user instanceof Addressable) {
//            representation.withProperty("homeAddress", ((Addressable) user).getHomeAddress());
//            representation.withProperty("billingAddress", ((Addressable) user).getHomeAddress());
//        }

        return null;
    }

    private Object getUsersCollectionRepresentation(List<User> users, UriInfo uriInfo) {
        // pagination attributes
        // query self link

        // embedded
        for (User user : users) {
            getUserRepresentation(user, uriInfo);
        }

        return null;
    }

}
