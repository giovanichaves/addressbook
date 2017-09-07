package datasource;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

abstract class CsvDatasource<T> implements Datasource {

    private final URL fileUrl;
    private Class classType;
    private final CsvSchema schema;

    CsvDatasource(String fileLocation, CsvSchema schema, Class classType) throws FileNotFoundException {
        this.schema = schema;
        this.classType = classType; //its bad, I know.. but we cant easily guess the Type on runtime for the reader

        this.fileUrl = getClass().getClassLoader().getResource(fileLocation);
        if (fileUrl == null) {
            throw new FileNotFoundException("The specified datasource file " + fileLocation + " was not found");
        }
    }

    @Override
    public List<T> fetchAll() {
        List<T> recordList = new ArrayList<>();
        CsvMapper mapper = new CsvMapper();

        ObjectReader objectReader = mapper
                .enable(CsvParser.Feature.TRIM_SPACES)
                .registerModule(new JavaTimeModule())
                .readerFor(classType)
                .with(schema);

        try (Reader reader = new FileReader(fileUrl.getFile())) {
            MappingIterator<T> mi = objectReader.readValues(reader);
            while (mi.hasNext()) {
                recordList.add(mi.next());
            }
        } catch (IOException e) {
            System.out.println("There was an error reading the datasource file");
        }

        return recordList;
    }
}
