package datasource;

import addressbook.Contact;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class ContactsFromCSV extends CsvDatasource<Contact> {

    private ContactsFromCSV(File csvFile, CsvSchema schema) throws FileNotFoundException {
        super(csvFile, schema, Contact.class);
    }

    public static Datasource<Contact> getDatasource(String fileLocation) throws FileNotFoundException {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("id")
                .addColumn("name")
                .addColumn("gender")
                .addColumn("dob")
                .build();

        URL fileUrl = ContactsFromCSV.class.getClassLoader().getResource(fileLocation);
        if (fileUrl == null) {
            throw new FileNotFoundException("The specified datasource file " + fileLocation+ " was not found");
        }

        return new ContactsFromCSV(new File(fileUrl.getFile()), schema);
    }

}
