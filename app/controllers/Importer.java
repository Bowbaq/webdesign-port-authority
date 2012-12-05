package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.MySqlPlatform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import models.gtfs.*;
import models.gtfs.csv.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Importer extends Controller {

    static private CSVStrategy strategy;
    static private Map<String, Shape> shapes;
    static private Map<String, Route> routes = new HashMap<String, Route>();
    static private Map<String, Calendar> services = new HashMap<String, Calendar>();
    static private Map<String, Stop> stops = new HashMap<String, Stop>();
    static private Map<String, Trip> trips = new HashMap<String, Trip>();

    public static Result doImport() throws IOException {
        strategy = new CSVStrategy(',', '"', '#', true, true);


        clearDB();

        importAgency();

        importCalendar();
        importCalendarDates();

        importRoutes();
        importStops();
        importShapes();
        importTrips();
        importStopTimes();

        importPoi();

        return ok("Successfully imported GTFS data");
    }

    private static void clearDB() {
        EbeanServer server = Ebean.getServer("default");
        DdlGenerator ddl = new DdlGenerator((SpiEbeanServer) server, new MySqlPlatform(), new ServerConfig());
        ddl.runScript(false, ddl.generateDropDdl());
        ddl.runScript(false, ddl.generateCreateDdl());
    }

    private static void importAgency() throws IOException {
        Reader r = new FileReader("data/agency.txt");
        CSVReader<Agency> csvr = new CSVReaderBuilder<Agency>(r)
                .entryParser(new AgencyAdapter())
                .strategy(strategy)
                .build();
        List<Agency> agencies = csvr.readAll();

        for (Agency a : agencies) {
            a.save();
        }
    }

    private static void importCalendar() throws IOException {
        Reader r = new FileReader("data/calendar.txt");
        CSVReader<Calendar> csvr = new CSVReaderBuilder<Calendar>(r)
                .entryParser(new CalendarAdapter())
                .strategy(strategy)
                .build();
        List<Calendar> cals = csvr.readAll();

        for (Calendar cal : cals) {
            services.put(cal.id, cal);
            cal.save();
        }
    }

    private static void importCalendarDates() {};

    private static void importRoutes() throws IOException {
        Reader r = new FileReader("data/routes.txt");
        CSVReader<Route> csvr = new CSVReaderBuilder<Route>(r)
                .entryParser(new RouteAdapter())
                .strategy(strategy)
                .build();
        List<Route> routes = csvr.readAll();

        for (Route ro : routes) {
            Importer.routes.put(ro.id, ro);
            ro.save();
        }
    }

    private static void importStops() throws IOException {
        Reader r = new FileReader("data/stops.txt");
        CSVReader<Stop> csvr = new CSVReaderBuilder<Stop>(r)
                .entryParser(new StopAdapter())
                .strategy(strategy)
                .build();
        List<Stop> stops = csvr.readAll();

        for (Stop s : stops) {
            Importer.stops.put(s.id, s);
            s.save();
        }
    }

    private static void importShapes() throws IOException {
        Reader r = new FileReader("data/shapes.txt");
        ShapeAdapter adapter = new ShapeAdapter();
        CSVReader<Shape> csvr = new CSVReaderBuilder<Shape>(r)
                .entryParser(adapter)
                .strategy(strategy)
                .build();
        csvr.readAll();
        shapes = adapter.getShapes();
        for(Shape s: shapes.values()) {
            s.save();
            for(ShapeFragment f : s.fragments) {
                f.save();
            }
        }
    }

    private static void importTrips() throws IOException {
        Reader r = new FileReader("data/trips.txt");
        TripAdapter adapter = new TripAdapter();
        adapter.setRoutes(routes);
        adapter.setServices(services);
        adapter.setShapes(shapes);
        CSVReader<Trip> csvr = new CSVReaderBuilder<Trip>(r)
                .entryParser(adapter)
                .strategy(strategy)
                .build();
        List<Trip> trips = csvr.readAll();
        for(Trip t : trips) {
            Importer.trips.put(t.id, t);
            t.save();
        }
    }

    private static void importStopTimes() throws IOException {
        Reader r = new FileReader("data/stop_times.txt");
        StopTimeAdapter adapter = new StopTimeAdapter();
        adapter.setStops(stops);
        adapter.setTrips(trips);
        CSVReader<StopTime> csvr = new CSVReaderBuilder<StopTime>(r)
                .entryParser(adapter)
                .strategy(strategy)
                .build();
        List<StopTime> stop_times = csvr.readAll();
        for(StopTime st : stop_times) {
            st.save();
        }
    }

    private static void importPoi() throws IOException {
        Reader r = new FileReader("data/poi.txt");
        CSVReader<Poi> csvr = new CSVReaderBuilder<Poi>(r)
                .entryParser(new PoiAdapter())
                .strategy(strategy)
                .build();
        List<Poi> pois = csvr.readAll();

        for (Poi p : pois) {
            p.save();
        }
    }
}