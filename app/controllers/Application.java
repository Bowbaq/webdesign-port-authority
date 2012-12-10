package controllers;

import models.gtfs.Route;
import models.gtfs.Stop;
import org.codehaus.jackson.map.ObjectMapper;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.transit.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Application extends Controller {
    
    public static Result index() {
        List<Route> routes = Route.find.all();
        return ok(schedule.render(routes));
    }
    
    public static Result getSchedule(String route_id, Integer direction, String stop_id) throws IOException {
        Stop stop = Stop.find.byId(stop_id);
        if(stop == null) {
            return notFound();
        }

        ObjectMapper mapper = new ObjectMapper();
        return ok(mapper.writeValueAsString(stop.getDaySchedule(
                new Date(),
                route_id,
                direction
        )));
    }
}