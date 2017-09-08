package datasource;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

abstract class CsvDatasource<T> implements Datasource<T> {

    private final File csvFile;
    private Class classType;
    private final CsvSchema schema;

    CsvDatasource(File csvFile, CsvSchema schema, Class classType) {
        this.csvFile = csvFile;
        this.schema = schema;
        this.classType = classType; //its bad, I know.. but we cant easily guess the Type during runtime for the reader
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

        try (Reader reader = new FileReader(csvFile)) {
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
