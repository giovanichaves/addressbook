package library;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class Book {
    private final String title;
    private final Integer renter;

    public Book(String title, Integer renter) {
        Preconditions.checkNotNull(title);

        this.title = title;
        this.renter = renter;
    }

    private Book(
            @JsonProperty("title") String title,
            @JsonProperty("renter") String renter) {
        //Jackson lib doesnt recognize uppercase NULL as literal
        this(title, renter.equals("NULL") ? null : Integer.valueOf(renter));
    }

    public String getTitle() {
        return title;
    }

    public Integer getRenter() {
        return renter;
    }
}
