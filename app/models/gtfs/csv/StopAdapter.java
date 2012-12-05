package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Stop;

public class StopAdapter implements CSVEntryParser<Stop> {

    @Override
    public Stop parseEntry(String... data) {
        Stop s = new Stop();

        s.id = data[0];
        s.code = data[1];
        s.name = data[2];
        s.lat = Double.parseDouble(data[4]);
        s.lng = Double.parseDouble(data[5]);
        s.zone = data[6];

        return s;
    }
}
