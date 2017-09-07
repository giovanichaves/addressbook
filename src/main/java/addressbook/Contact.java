package addressbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.time.LocalDate;

public class Contact {

    private final int id;
    private final String name;
    private final Gender gender;
    private final LocalDate dob;

    public Contact(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("gender") Gender gender,
            @JsonProperty("dob") LocalDate dob) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(gender);
        Preconditions.checkNotNull(dob);

        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getDob() {
        return dob;
    }
}