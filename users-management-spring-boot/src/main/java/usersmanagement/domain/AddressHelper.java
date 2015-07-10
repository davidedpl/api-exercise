package usersmanagement.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressHelper {

    private final String addressLine1;
    private final String addressLine2;
    private final String postCode;
    private final String country;

    @JsonCreator
    public AddressHelper(
            @JsonProperty("addressLine1") String addressLine1,
            @JsonProperty(value = "addressLine2", required = false) String addressLine2,
            @JsonProperty(value = "postCode", required = false) String postCode,
            @JsonProperty("country") String country) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postCode = postCode;
        this.country = country;
    }

    public Address toAddress() {
        return new Address.AddressBuilder(addressLine1, country).build();
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCountry() {
        return country;
    }
}
