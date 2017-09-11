import addressbook.AddressbookService;
import addressbook.model.Addressbook;
import addressbook.model.Contact;
import datasource.csv.BooksFromCSV;
import datasource.csv.ContactsFromCSV;
import datasource.Datasource;
import datasource.FileWatch;
import datasource.csv.NotMatchingCsvMappingException;
import library.LibraryService;
import library.model.Book;
import library.model.Library;
import view.RudimentaryHtml;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Application {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        outputQuestions();

        FileWatch fileWatcher = null;
        try {
            fileWatcher = new FileWatch("testdata");
            fileWatcher.watchFilesChanged(
                    Arrays.asList("address-book.csv", "library.csv"),
                    (Callable<Void>) Application::outputQuestions
            );
        } catch (FileNotFoundException e) {
            System.out.println("FileWatcher service not up: " + e.getMessage());
        }

    }

    private static Void outputQuestions() {
        try {
            Datasource<Contact> contactDatasource = ContactsFromCSV.getDatasource("testdata/address-book.csv");
            Addressbook addressbook = new Addressbook(contactDatasource.fetchAll());
            AddressbookService addressbookService = new AddressbookService(addressbook);

            Datasource<Book> bookDatasource = BooksFromCSV.getDatasource("testdata/library.csv");
            Library library = new Library(bookDatasource.fetchAll());
            LibraryService libraryService = new LibraryService(library);
            String html = new RudimentaryHtml(addressbookService, libraryService).getHtml();

            System.out.println(html);
        } catch (NotMatchingCsvMappingException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            return null;
        }
    }
}
