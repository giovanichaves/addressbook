package datasource;

import addressbook.Contact;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.FileNotFoundException;

public class ContactsFromCSV extends CsvDatasource {

    private ContactsFromCSV(String fileLocation, CsvSchema schema) throws FileNotFoundException {
        super(fileLocation, schema, Contact.class);
    }

    public static Datasource<Contact> getDatasource(String fileLocation) throws FileNotFoundException {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("id")
                .addColumn("name")
                .addColumn("gender")
                .addColumn("dob")
                .build();

        return new ContactsFromCSV(fileLocation, schema);
    }

}
