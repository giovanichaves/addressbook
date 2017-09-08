package library;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {

    private ImmutableList<Book> bookList;

    public Library(List<Book> bookList) {
        this.bookList = ImmutableList.copyOf(bookList);
    }

    public Map<Integer, Integer> summarizeRentalsPerContact() {
        Map<Integer, Integer> summary = new HashMap<>();

        findRentedBooks()
            .forEach(rental -> {
                Integer renter = rental.getRenter();

                if (summary.containsKey(renter)) {
                    summary.put(renter, summary.get(renter) + 1);
                } else {
                    summary.put(renter, 1);
                }
            });

        return summary;
    }

    public List<Book> findRentedBooks() {
        return bookList
                .stream()
                .filter(rental -> rental.getRenter() != null)
                .collect(Collectors.toList());
    }

    public List<Book> determineNotRentedBooks() {
        return bookList
                .stream()
                .filter(rental -> rental.getRenter() == null)
                .collect(Collectors.toList());
    }
}
