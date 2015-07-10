package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.utils.UserUpdateHelper;

import javax.inject.Inject;

import static usersmanagement.rest.v1.assembler.AssemblerUtils.getLocalDate;
import static usersmanagement.rest.v1.assembler.AssemblerUtils.getString;

@Component
public class UserUpdateHelperAssembler implements Assembler<UserUpdateHelper> {

    private final AddressAssembler addressAssembler;

    @Inject
    public UserUpdateHelperAssembler(AddressAssembler addressAssembler) {
        this.addressAssembler = addressAssembler;
    }

    @Override
    public UserUpdateHelper assemble(JsonNode node) {
        return new UserUpdateHelper(
                getString(node, "title"),
                getString(node, "firstName"),
                getString(node, "lastName"),
                getLocalDate(node, "dateOfBirth"),
                getString(node, "email"),
                getString(node, "password").map(String::toCharArray),
                addressAssembler.assemble(node.get("homeAddress")),
                addressAssembler.assemble(node.get("billingAddress"))
        );
    }
}
