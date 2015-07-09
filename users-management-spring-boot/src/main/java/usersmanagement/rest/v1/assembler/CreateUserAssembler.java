package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.UserType;
import usersmanagement.domain.users.Users;

import javax.inject.Inject;
import javax.validation.ValidationException;

import static usersmanagement.rest.v1.assembler.AssemblerUtils.*;


@Component
public class CreateUserAssembler implements Assembler<User> {

    private final AddressAssembler addressAssembler;

    @Inject
    public CreateUserAssembler(AddressAssembler addressAssembler) {
        this.addressAssembler = addressAssembler;
    }

    @Override
    public User assemble(JsonNode node) {
        UserType type = getEnum(node, UserType.class, "type").get();
        switch (type) {
            case Subscriber:
                return Users.getSubscriber(
                        getString(node, "title").get(),
                        getString(node, "lastName").get(),
                        getString(node, "firstName").get(),
                        getLocalDate(node, "dateOfBirth").get(),
                        getString(node, "email").get(),
                        getString(node, "password").get().toCharArray(),
                        getString(node, "username").get(),
                        addressAssembler.assemble(node.get("homeAddress")).get(),
                        addressAssembler.assemble(node.get("billingAddress")).get()

                );
            case Administrator:
                return Users.getAdmin(
                        getString(node, "title").get(),
                        getString(node, "lastName").get(),
                        getString(node, "firstName").get(),
                        getLocalDate(node, "dateOfBirth").get(),
                        getString(node, "email").get(),
                        getString(node, "password").get().toCharArray(),
                        getString(node, "username").get()
                );
            default:
                throw new ValidationException("Invalid user type: " + type);
        }
    }
}
