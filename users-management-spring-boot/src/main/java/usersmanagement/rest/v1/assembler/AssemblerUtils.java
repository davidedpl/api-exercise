package usersmanagement.rest.v1.assembler;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Optional;

public class AssemblerUtils {

    private AssemblerUtils() {}

    public static <T extends Enum> T getEnum(JsonNode node, Class<T> clazz, String field, T def) {
        return getEnum(node, clazz, field).orElse(def);
    }

    public static <T extends Enum> Optional<T> getEnum(JsonNode node, Class<T> clazz, String field) {
        return getString(node, field).map(s -> Enum.valueOf(clazz, s));
    }

    public static String getString(JsonNode node, String field, String def) {
        return getString(node, field).orElse(def);
    }

    public static Optional<String> getString(JsonNode node, String field) {
        if (validNodeAndField(node, field)) {
            return Optional.of(node.get(field).asText());
        } else {
            return Optional.empty();
        }
    }

    public static int getInt(JsonNode node, String field) {
        return node.get(field).asInt();
    }

    public static Optional<Boolean> getBoolean(JsonNode node, String field) {
        if (validNodeAndField(node, field)) {
            return Optional.empty();
        } else {
            JsonNode b = node.get(field);
            if (!b.isBoolean()) {
                throw new ValidationException("Invalid boolean value: " + b);
            }
            return Optional.of(b.asBoolean());
        }
    }

    public static Optional<LocalDate> getLocalDate(JsonNode node, String field) {
        if (validNodeAndField(node, field)) {
            return Optional.of(LocalDate.parse(node.get(field).asText()));
        } else {
            return Optional.empty();
        }
    }

    private static boolean validNodeAndField(JsonNode node, String field) {
        return node != null && node.has(field);
    }
}
