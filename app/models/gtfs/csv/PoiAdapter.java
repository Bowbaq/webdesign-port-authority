package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Poi;

public class PoiAdapter extends BaseAdapter implements CSVEntryParser<Poi> {

    @Override
    public Poi parseEntry(String... data) {
        Poi poi = new Poi();

        poi.name = data[mapping.get("NAME")];
        poi.lat = Double.parseDouble(data[mapping.get("LAT")]);
        poi.lng = Double.parseDouble(data[mapping.get("LONG")]);

        return poi;
    }
}
