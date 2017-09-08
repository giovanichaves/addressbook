import addressbook.Addressbook;
import addressbook.Contact;
import addressbook.ContactNotOlderException;
import datasource.BooksFromCSV;
import datasource.ContactsFromCSV;
import datasource.Datasource;
import library.Book;
import library.Library;
import view.RudimentaryHtml;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws ContactNotOlderException, IOException {

        Datasource<Contact> contactDatasource = ContactsFromCSV.getDatasource("testdata/address-book.csv");
        Addressbook addressbook = new Addressbook(contactDatasource.fetchAll());
        Datasource<Book> bookDatasource = BooksFromCSV.getDatasource("testdata/library.csv");
        Library library = new Library(bookDatasource.fetchAll());

        String html = new RudimentaryHtml().getHtml(addressbook, library);

        System.out.println(html);
    }
}
