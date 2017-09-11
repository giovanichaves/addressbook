package library;

import library.model.Book;
import library.model.Library;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class LibraryServiceTest {

    private List<Book> booksList;
    private LibraryService libraryService;

    @Before
    public void setUp() {
        booksList = Arrays.asList(
                new Book("Jungle Book", 2),
                new Book("Lord of the Rings", 1),
                new Book("50 Shades of Gay", null),
                new Book("The Hole Bible", 4),
                new Book("Guinness Book", 2)
        );

        libraryService = new LibraryService(new Library(booksList));
    }

    @Test
    public void testFindRentedBooks() {
        List<Book> expected = Arrays.asList(
                booksList.get(0),
                booksList.get(1),
                booksList.get(3),
                booksList.get(4)
        );
        Assert.assertEquals(expected, libraryService.findRentedBooks());
    }

    @Test
    public void testWhoRentedHowManyBooks() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 1);
        expected.put(2, 2);
        expected.put(4, 1);
        Assert.assertEquals(expected, libraryService.summarizeRentalsPerContact());
    }


    @Test
    public void testFindNotRentedBooks() {
        Assert.assertEquals(Collections.singletonList(booksList.get(2)), libraryService.findNotRentedBooks());
    }
}
