package datasource;

import java.util.List;

public interface Datasource<T> {

    List<T> fetchAll();
}
