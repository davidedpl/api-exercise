package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.security.UserType;
import usersmanagement.domain.utils.Users;

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
        UserType type = getMandatoryEnum(node, UserType.class, "type");
        switch (type) {
            case Subscriber:
                return Users.getSubscriber(
                        getMandatoryString(node, "title"),
                        getMandatoryString(node, "lastName"),
                        getMandatoryString(node, "firstName"),
                        getMandatoryLocalDate(node, "dateOfBirth"),
                        getMandatoryString(node, "email"),
                        getMandatoryString(node, "password").toCharArray(),
                        getMandatoryString(node, "username"),
                        addressAssembler.assemble(node.get("homeAddress")).orElseThrow(ValidationException::new),
                        addressAssembler.assemble(node.get("billingAddress")).orElseThrow(ValidationException::new)

                );
            case Administrator:
                return Users.getAdmin(
                        getMandatoryString(node, "title"),
                        getMandatoryString(node, "lastName"),
                        getMandatoryString(node, "firstName"),
                        getMandatoryLocalDate(node, "dateOfBirth"),
                        getMandatoryString(node, "email"),
                        getMandatoryString(node, "password").toCharArray(),
                        getMandatoryString(node, "username")
                );
            default:
                throw new ValidationException("Invalid user type: " + type);
        }
    }
}
