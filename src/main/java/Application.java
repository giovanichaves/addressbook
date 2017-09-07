import addressbook.Addressbook;
import addressbook.ContactNotOlderException;
import library.Book;
import addressbook.Contact;
import library.Library;
import datasource.BooksFromCSV;
import datasource.ContactsFromCSV;
import datasource.Datasource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Application {

    public static void main(String[] args) throws ContactNotOlderException {

        Datasource<Contact> contactDatasource = ContactsFromCSV.getDatasource();
        Addressbook addressbook = new Addressbook(contactDatasource.fetchAll());

        int females = addressbook.calculateFemaleCount();

        Optional<Contact> oldest = addressbook.determineOldestContact();

        int daysOlder = addressbook.calculateContactDaysOlderThan(
            addressbook.findContactByName("Jon").get(),
            addressbook.findContactByName("Paul").get()
        );



        Datasource<Book> bookDatasource = BooksFromCSV.getDatasource();
        Library library = new Library(bookDatasource.fetchAll());

        Map<Integer,Integer> summary = library.summarizeRentalsPerPerson();

        List<Book> notRented = library.determineNotRentedBooks();

    }
}
