package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.model.Address;

import java.util.Optional;

import static usersmanagement.rest.v1.assembler.AssemblerUtils.getMandatoryString;
import static usersmanagement.rest.v1.assembler.AssemblerUtils.getString;

@Component
public class AddressAssembler {
    public Optional<Address> assemble(JsonNode node) {
        if (node != null) {
            Address.AddressBuilder builder = new Address.AddressBuilder(
                    getMandatoryString(node, "addressLine1"),
                    getMandatoryString(node, "country")
            );
            getString(node, "addressLine2").ifPresent(builder::withAddressLine2);
            getString(node, "postCode").ifPresent(builder::withPostCode);
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }
}
