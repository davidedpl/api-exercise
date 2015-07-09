package usersmanagement.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.StringUtils;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public final class Address {

    private final String addressLine1;
    private final String addressLine2;
    private final String postCode;
    private final String country;

    private Address(AddressBuilder builder) {
        this.addressLine1 = builder.addressLine1;
        this.addressLine2 = builder.addressLine2;
        this.postCode = builder.postCode;
        this.country = builder.country;
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

    public static class AddressBuilder {
        // required params
        private final String addressLine1;
        private final String country;

        // optional params - initialized to empty string
        private String addressLine2 = "";
        private String postCode = "";

        public AddressBuilder(String addressLine1, String country) {
            if (StringUtils.isEmpty(addressLine1)) {
                throw new IllegalArgumentException("addressLine1 is empty");
            }
            if (StringUtils.isEmpty(country)) {
                throw new IllegalArgumentException("country is empty");
            }

            this.addressLine1 = addressLine1;
            this.country = country;
        }

        public AddressBuilder withAddressLine2(String addressLine2) {
            if (addressLine2 != null) {
                this.addressLine2 = addressLine2;
            }
            return this;
        }

        public AddressBuilder withPostCode(String postCode) {
            if (postCode != null) {
                this.postCode = postCode;
            }
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
