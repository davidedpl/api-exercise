package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;

public interface Assembler<T> {

    T assemble(JsonNode node);
}
