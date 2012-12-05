package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Route;

public class RouteAdapter extends BaseAdapter implements CSVEntryParser<Route> {

    @Override
    public Route parseEntry(String... data) {
        Route r = new Route();
        r.id = data[mapping.get("route_id")];
        r.short_name = data[mapping.get("route_short_name")];
        r.long_name = data[mapping.get("route_long_name")];
        r.type = Integer.parseInt(data[mapping.get("route_type")]);

        return r;
    }
}
