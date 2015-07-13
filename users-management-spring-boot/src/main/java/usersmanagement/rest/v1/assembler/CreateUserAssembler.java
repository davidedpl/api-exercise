package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import usersmanagement.domain.model.User;
import usersmanagement.domain.model.UserType;
import usersmanagement.domain.model.Users;

import javax.inject.Inject;
import javax.validation.ValidationException;

import static usersmanagement.rest.v1.assembler.AssemblerUtils.*;


@Component
public class CreateUserAssembler {

    private final AddressAssembler addressAssembler;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public CreateUserAssembler(AddressAssembler addressAssembler, PasswordEncoder passwordEncoder) {
        this.addressAssembler = addressAssembler;
        this.passwordEncoder = passwordEncoder;
    }

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
                        encodePassword(getMandatoryString(node, "password")),
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
                        encodePassword(getMandatoryString(node, "password")),
                        getMandatoryString(node, "username")
                );
            default:
                throw new ValidationException("Invalid user type: " + type);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
