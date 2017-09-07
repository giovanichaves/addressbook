package addressbook;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("Male") MALE("Male"),
    @JsonProperty("Female") FEMALE("Female");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String value() { return value; }

}
