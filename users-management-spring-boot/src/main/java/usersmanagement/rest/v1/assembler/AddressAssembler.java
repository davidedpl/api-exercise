package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.Address;

import java.util.Optional;

import static usersmanagement.rest.v1.assembler.AssemblerUtils.getString;

@Component
public class AddressAssembler implements Assembler<Optional<Address>> {
    @Override
    public Optional<Address> assemble(JsonNode node) {
        if (node != null) {
            Address.AddressBuilder builder = new Address.AddressBuilder(
                    getString(node, "addressLine1").get(),
                    getString(node, "country").get()
            );
            getString(node, "addressLine2").ifPresent(s -> builder.withAddressLine2(s));
            getString(node, "postCode").ifPresent(s -> builder.withPostCode(s));
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }
}