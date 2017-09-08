import addressbook.Addressbook;
import addressbook.Contact;
import datasource.BooksFromCSV;
import datasource.ContactsFromCSV;
import datasource.Datasource;
import datasource.FileWatch;
import library.Book;
import library.Library;
import view.RudimentaryHtml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Application {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        outputQuestions();

        FileWatch fileWatcher = new FileWatch("testdata");
        fileWatcher.isFilesChanged(
                Arrays.asList("address-book.csv", "library.csv"),
                (Callable<Void>) Application::outputQuestions
        );

    }

    private static Void outputQuestions() throws IOException {
        Datasource<Contact> contactDatasource = ContactsFromCSV.getDatasource("testdata/address-book.csv");
        Addressbook addressbook = new Addressbook(contactDatasource.fetchAll());
        Datasource<Book> bookDatasource = BooksFromCSV.getDatasource("testdata/library.csv");
        Library library = new Library(bookDatasource.fetchAll());

        String html = new RudimentaryHtml(addressbook, library).getHtml();

        System.out.println(html);
        return null;
    }
}
