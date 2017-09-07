package datasource;

import library.Book;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.FileNotFoundException;

public class BooksFromCSV extends CsvDatasource {

    private BooksFromCSV(String fileLocation, CsvSchema schema) throws FileNotFoundException {
        super(fileLocation, schema, Book.class);
    }

    public static Datasource<Book> getDatasource(String fileLocation) throws FileNotFoundException {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("title")
                .addColumn("renter")
                .build();

        return new BooksFromCSV(fileLocation, schema);
    }

}
