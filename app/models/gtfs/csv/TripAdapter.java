package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Calendar;
import models.gtfs.Route;
import models.gtfs.Shape;
import models.gtfs.Trip;

import java.util.Map;

public class TripAdapter implements CSVEntryParser<Trip> {

    private Map<String, Route> routes;
    private Map<String, Calendar> services;
    private Map<String, Shape> shapes;

    @Override
    public Trip parseEntry(String... data) {
        Trip trip = new Trip();

        trip.id = data[2];
        trip.route = routes.get(data[0]);
        trip.service = services.get(data[1]);
        trip.headsign = data[3];
        trip.direction = data[4];
        trip.shape = shapes.get(data[6]);

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
