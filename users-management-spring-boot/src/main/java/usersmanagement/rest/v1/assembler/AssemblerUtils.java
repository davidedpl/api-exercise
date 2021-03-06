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

    public static <T extends Enum<T>> T getMandatoryEnum(JsonNode node, Class<T> clazz, String field) {
        return Enum.valueOf(clazz, getMandatoryString(node, field));
    }

    public static String getString(JsonNode node, String field, String def) {
        return getString(node, field).orElse(def);
    }

    public static Optional<String> getString(JsonNode node, String field) {
        if (validNodeAndField(node, field)) {
            return Optional.of(extractString(node, field));
        } else {
            return Optional.empty();
        }
    }

    public static String getMandatoryString(JsonNode node, String field) {
        if (validNodeAndField(node, field)) {
            return extractString(node, field);
        }
        throw validationException(field);
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
            return getString(node, field).map(LocalDate::parse);
        } else {
            return Optional.empty();
        }
    }

    public static LocalDate getMandatoryLocalDate(JsonNode node, String field) {
        if (validNodeAndField(node, field)) {
            return LocalDate.parse(extractString(node, field));
        }
        throw validationException(field);
    }

    public static ValidationException validationException(String field) {
        return new ValidationException("Mandatory field missing: " + field);
    }

    private static boolean validNodeAndField(JsonNode node, String field) {
        return node != null && node.has(field);
    }

    private static String extractString(JsonNode node, String field) {
        return node.get(field).asText();
    }

}
