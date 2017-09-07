package datasource;

import library.Book;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class BooksFromCSV extends CsvDatasource {

    public static final String FILE_LOCATION = "src/main/resources/testdata/library.csv";

    private BooksFromCSV(CsvSchema schema) {
        super(FILE_LOCATION, schema, Book.class);
    }

    public static Datasource<Book> getDatasource() {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("title")
                .addColumn("renter")
                .build();

        return new BooksFromCSV(schema);
    }

}
