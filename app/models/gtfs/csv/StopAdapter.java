package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Stop;

public class StopAdapter extends BaseAdapter implements CSVEntryParser<Stop> {

    @Override
    public Stop parseEntry(String... data) {
        Stop s = new Stop();

        s.id = data[mapping.get("stop_id")];
        s.code = data[mapping.get("stop_code")];
        s.name = data[mapping.get("stop_name")];
        s.lat = Double.parseDouble(data[mapping.get("stop_lat")]);
        s.lng = Double.parseDouble(data[mapping.get("stop_lon")]);
        s.zone = data[mapping.get("zone_id")];

        return s;
    }
}
