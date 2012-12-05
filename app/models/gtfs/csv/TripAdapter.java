package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Calendar;
import models.gtfs.Route;
import models.gtfs.Shape;
import models.gtfs.Trip;

import java.util.Map;

public class TripAdapter extends BaseAdapter implements CSVEntryParser<Trip> {

    private Map<String, Route> routes;
    private Map<String, Calendar> services;
    private Map<String, Shape> shapes;

    @Override
    public Trip parseEntry(String... data) {
        Trip trip = new Trip();

        trip.id = data[mapping.get("trip_id")];
        trip.route = routes.get(data[mapping.get("route_id")]);
        trip.service = services.get(data[mapping.get("service_id")]);
        trip.headsign = data[mapping.get("trip_headsign")];
        trip.direction = data[mapping.get("direction_id")].equals("1") ? Trip.INBOUND : Trip.OUTBOUND;
        trip.shape = shapes.get(data[mapping.get("shape_id")]);

        return trip;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    public void setServices(Map<String, Calendar> services) {
        this.services = services;
    }

    public void setShapes(Map<String, Shape> shapes) {
        this.shapes = shapes;
    }
}
