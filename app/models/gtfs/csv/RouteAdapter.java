package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Route;

public class RouteAdapter implements CSVEntryParser<Route> {

    @Override
    public Route parseEntry(String... data) {
        Route r = new Route();

        r.id = data[0];
        r.short_name = data[1];
        r.long_name = data[2];
        r.type = Integer.parseInt(data[3]);

        return r;
    }
}
