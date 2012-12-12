package controllers;

import models.gtfs.Route;
import models.gtfs.Stop;
import org.codehaus.jackson.map.ObjectMapper;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.transit.*;
import views.html.fares.*;
import views.html.service.*;
import views.html.info.*;

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

    public static Result fares(){
        return ok(fares.render());
    }

    public static Result zones(){
        return ok(zones.render());
    }
    /*public static Result map(){
        return ok(map.render());
    }*/
    public static Result buy(){
        return ok(buy.render());
    }
    public static Result info(){
        return ok(info.render());
    }
    public static Result changes(){
        return ok(changes.render());
    }
    public static Result service(){
        return ok(service.render());
    }
    public static Result special(){
        return ok(special.render());
    }
}