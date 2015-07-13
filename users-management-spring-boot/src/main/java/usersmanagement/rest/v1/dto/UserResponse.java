package usersmanagement.rest.v1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import usersmanagement.domain.model.Address;
import usersmanagement.domain.model.Addressable;
import usersmanagement.domain.model.User;
import usersmanagement.rest.v1.UserRestResource;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY) // exclude optional fields from Json representation
public class UserResponse {

    private final Map<String, Link> _links = new HashMap<>();

    private final String title;
    private final String firstName;
    private final String lastName;
    private final String dateOfBirth;
    private final String email;
    private final String username;
    private final String type;

    private final Address homeAddress;
    private final Address billingAddress;

    private UserResponse(URI selfLink, String title, String firstName, String lastName, String dateOfBirth, String email,
                         String username, String type, Address homeAddress, Address billingAddress) {
        this.type = type;
        this._links.put("self", new Link(selfLink.toString()));
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.username = username;
        this.homeAddress = homeAddress;
        this.billingAddress = billingAddress;
    }

    public static UserResponse fromUser(User user, UriInfo uriInfo) {
        URI selfLink = UriBuilder.fromUri(uriInfo.getBaseUri()).path(UserRestResource.PATH_WITH_ID)
                .build(String.valueOf(user.getUsername()));
        Address homeAddress = null;
        Address billingAddress = null;
        if (user instanceof Addressable) {
            homeAddress = ((Addressable) user).getHomeAddress();
            billingAddress = ((Addressable) user).getBillingAddress();
        }
        return new UserResponse(selfLink, user.getTitle(), user.getFirstName(), user.getLastName(),
                user.getDateOfBirth().toString(), user.getEmail(), user.getUsername(), user.getType().toString(),
                homeAddress, billingAddress);
    }

    public Map<String, Link> get_links() {
        return _links;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public String getType() {
        return type;
    }

    private static class Link {
        private final String href;

        private Link(String href) {
            this.href = href;
        }

        public String getHref() {
            return href;
        }
    }
}
