package datasource;

import library.Book;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class BooksFromCSV extends CsvDatasource<Book> {

    private BooksFromCSV(File csvFile, CsvSchema schema) throws FileNotFoundException {
        super(csvFile, schema, Book.class);
    }

    public static Datasource<Book> getDatasource(String fileLocation) throws FileNotFoundException {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("title")
                .addColumn("renter")
                .build();

        URL fileUrl = ContactsFromCSV.class.getClassLoader().getResource(fileLocation);
        if (fileUrl == null) {
            throw new FileNotFoundException("The specified datasource file " + fileLocation+ " was not found");
        }

        return new BooksFromCSV(new File(fileUrl.getFile()), schema);
    }

}
