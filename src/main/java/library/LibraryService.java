package library;

import library.model.Book;
import library.model.Library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryService {

    private final Library library;

    public LibraryService(Library library) {
        this.library = library;
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
        return library.getBookList()
                .stream()
                .filter(rental -> rental.getRenter() != null)
                .collect(Collectors.toList());
    }

    public List<Book> findNotRentedBooks() {
        return library.getBookList()
                .stream()
                .filter(rental -> rental.getRenter() == null)
                .collect(Collectors.toList());
    }
}
