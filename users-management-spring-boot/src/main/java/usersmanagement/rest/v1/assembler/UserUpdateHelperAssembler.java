package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.model.UserUpdateHelper;

import javax.inject.Inject;

import static usersmanagement.rest.v1.assembler.AssemblerUtils.getLocalDate;
import static usersmanagement.rest.v1.assembler.AssemblerUtils.getString;

@Component
public class UserUpdateHelperAssembler {

    private final AddressAssembler addressAssembler;

    @Inject
    public UserUpdateHelperAssembler(AddressAssembler addressAssembler) {
        this.addressAssembler = addressAssembler;
    }

    public UserUpdateHelper assemble(JsonNode node) {
        return new UserUpdateHelper(
                getString(node, "title"),
                getString(node, "firstName"),
                getString(node, "lastName"),
                getLocalDate(node, "dateOfBirth"),
                getString(node, "email"),
                getString(node, "password"),
                addressAssembler.assemble(node.get("homeAddress")),
                addressAssembler.assemble(node.get("billingAddress"))
        );
    }
}
