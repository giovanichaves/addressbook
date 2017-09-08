package addressbook;

import library.Book;
import library.Library;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class LibraryTest {

    private List<Book> booksList;
    private Library library;

    @Before
    public void setUp() {
        booksList = Arrays.asList(
                new Book("Jungle Book", 2),
                new Book("Lord of the Rings", 1),
                new Book("50 Shades of Gay", null),
                new Book("The Hole Bible", 4),
                new Book("Guinness Book", 2)
        );

        library = new Library(booksList);
    }

    @Test
    public void testWhoRentedHowManyBooks() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 1);
        expected.put(2, 2);
        expected.put(4, 1);
        Assert.assertEquals(expected, library.summarizeRentalsPerContact());
    }


    @Test
    public void testDetermineNotRentedBooks() {
        Assert.assertEquals(Collections.singletonList(booksList.get(2)), library.determineNotRentedBooks());
    }
}
