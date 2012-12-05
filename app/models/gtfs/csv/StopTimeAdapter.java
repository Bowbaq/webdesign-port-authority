package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Stop;
import models.gtfs.StopTime;
import models.gtfs.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class StopTimeAdapter implements CSVEntryParser<StopTime> {

    private Map<String, Stop> stops;
    private Map<String, Trip> trips;

    private static SimpleDateFormat formatter = new SimpleDateFormat("k:m:s");

    @Override
    public StopTime parseEntry(String... data) {
        StopTime time = new StopTime();

        time.trip = trips.get(data[0]);
        try {
            time.arrival = formatter .parse(data[1]);
            time.departure = formatter.parse(data[2]);
        } catch (ParseException e) {}
        time.stop = stops.get(data[3]);
        time.stop_seq = Integer.parseInt(data[4]);
        time.pickup_type = data[5].equals("1");
        time.dropoff_type = data[6].equals("1");

        return time;
    }

    public void setTrips(Map<String, Trip> trips) {
        this.trips = trips;
    }

    public void setStops(Map<String, Stop> stops) {
        this.stops = stops;
    }
}
