import addressbook.AddressbookService;
import addressbook.model.Addressbook;
import addressbook.model.Contact;
import datasource.Datasource;
import datasource.csv.FileWatch;
import datasource.csv.BooksFromCSV;
import datasource.csv.ContactsFromCSV;
import datasource.csv.NotMatchingCsvMappingException;
import library.LibraryService;
import library.model.Book;
import library.model.Library;
import view.RudimentaryHtml;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class EbayApplication {

    public static void main(String[] args) {
        EbayApplication app = new EbayApplication();
        app.outputHtml();

        try {
            FileWatch fileWatcher = new FileWatch("testdata");
            fileWatcher.watchChangedFiles(
                    Arrays.asList("address-book.csv", "library.csv"),
                    app::outputHtml
            );
        } catch (FileNotFoundException e) {
            System.out.println("FileWatcher service not up: " + e.getMessage());
        }

    }

    private void outputHtml() {
        try {
            Datasource<Contact> contactDatasource = ContactsFromCSV.getDatasource("testdata/address-book.csv");
            Addressbook addressbook = new Addressbook(contactDatasource.fetchAll());
            AddressbookService addressbookService = new AddressbookService(addressbook);

            Datasource<Book> bookDatasource = BooksFromCSV.getDatasource("testdata/library.csv");
            Library library = new Library(bookDatasource.fetchAll());
            LibraryService libraryService = new LibraryService(library);
            String hmtl = new RudimentaryHtml(addressbookService, libraryService).getHtml();

            System.out.println(hmtl);
        } catch (NotMatchingCsvMappingException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
