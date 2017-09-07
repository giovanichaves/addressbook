package datasource;

import addressbook.Contact;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class ContactsFromCSV extends CsvDatasource {

    public static final String FILE_LOCATION = "testdata/address-book.csv";

    private ContactsFromCSV(CsvSchema schema) {
        super(FILE_LOCATION, schema, Contact.class);
    }

    public static Datasource<Contact> getDatasource() {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("id")
                .addColumn("name")
                .addColumn("gender")
                .addColumn("dob")
                .build();

        return new ContactsFromCSV(schema);
    }

}
