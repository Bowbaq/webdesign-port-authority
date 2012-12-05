package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Stop;
import models.gtfs.StopTime;
import models.gtfs.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class StopTimeAdapter extends BaseAdapter implements CSVEntryParser<StopTime> {

    private Map<String, Stop> stops;
    private Map<String, Trip> trips;

    private static SimpleDateFormat formatter = new SimpleDateFormat("k:m:s");

    @Override
    public StopTime parseEntry(String... data) {
            StopTime time = new StopTime();

        time.trip = trips.get(data[mapping.get("trip_id")]);
        try {
            time.arrival = formatter .parse(data[mapping.get("arrival_time")]);
            time.departure = formatter.parse(data[mapping.get("departure_time")]);
        } catch (ParseException e) {}
        time.stop = stops.get(data[mapping.get("stop_id")]);
        time.stop_seq = Integer.parseInt(data[mapping.get("stop_sequence")]);
        time.pickup_type = data[mapping.get("pickup_type")].equals("1");
        time.dropoff_type = data[mapping.get("drop_off_type")].equals("1");

        return time;
    }

    public void setTrips(Map<String, Trip> trips) {
        this.trips = trips;
    }

    public void setStops(Map<String, Stop> stops) {
        this.stops = stops;
    }
}
