package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Poi;

public class PoiAdapter implements CSVEntryParser<Poi> {

    @Override
    public Poi parseEntry(String... data) {
        Poi poi = new Poi();

        poi.name = data[0];
        poi.lat = Double.parseDouble(data[1]);
        poi.lng = Double.parseDouble(data[2]);

        return poi;
    }
}
