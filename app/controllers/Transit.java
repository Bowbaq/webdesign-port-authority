package controllers;

import models.Favorite;
import models.User;
import models.gtfs.Route;
import models.gtfs.Stop;
import models.gtfs.StopTime;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.DoubleW;
import views.html.transit.*;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@Security.Authenticated(Secured.class)
public class Transit extends Controller {

    public static Result routes() {
        List<Route> routes = Route.find.all();
        return ok(route.render(routes));
    }

    public static Result doFavorite(String stop_id, String route_id, Integer direction, Integer on) {
        User user = User.find.byId(session().get("email"));
        List<Favorite> userFavorites = user.getAllFavorites();
        for(Favorite f : userFavorites) {
            if(f.route.id.equals(route_id) && f.stop.id.equals(stop_id) && f.direction == direction.intValue()) {
                if(on == 0) {
                    f.delete();
                }
                return schedule(route_id, stop_id, direction);
            }
        }
        if(on == 1) {
            Favorite f = new Favorite();
            f.direction = direction;
            f.stop = Stop.find.byId(stop_id);
            f.route = Route.find.byId(route_id);
            f.owner = user;
            f.save();
        }
        return schedule(route_id, stop_id, direction);
    }

    public static Result favorites() {
        User current = User.findByEmail(session().get("email"));
        List<Favorite> myFavorites = current.getAllFavorites();
        for(Favorite f : myFavorites) {
            f.route = Route.find.byId(f.route.id);
            f.stop = Stop.find.byId(f.stop.id);
        }
        return ok(favorite.render(myFavorites));
    }

    public static Result directions(String route_id) {
        Route route = Route.find.byId(route_id);
        return ok(directions.render(route));
    }

    public static Result stops(String route_id, Integer direction) {
        Route route = Route.find.byId(route_id);
        List<Stop> stops = route.stops(direction);
        return ok(stop.render(route, direction, stops));
    }

    public static Result schedule(String route_id, String stop_id, Integer direction) {
        Stop stop = Stop.find.byId(stop_id);
        Route route = Route.find.byId(route_id);
        TreeMap<Date, List<StopTime>> times = stop.getSchedule(route_id, direction);
        Integer isFavorite = Favorite.find.where()
                .eq("route_id", route_id)
                .eq("stop_id", stop_id)
                .eq("owner_email", session().get("email"))
                .eq("direction", direction).findRowCount() >= 1 ? 1 : 0;
        return ok(schedule.render(route, direction, stop, times, isFavorite));
    }

    public static Result preNearby() {
        return ok(nearby.render());
    }

    public static Result nearby(DoubleW lat, DoubleW lng) {
        List<Stop> stops = Stop.getNearby(lat.value, lng.value);
        return ok(nearbystop.render(stops));
    }

    public static Result nearbyRoutes(String stop_id) {
        Stop stop = Stop.find.byId(stop_id);
        return ok(nearbyroutes.render(stop, stop.getRoutes()));
    }

    public static Result directedNearbyRoute(String stop_id , String route_id) {
        Stop stop = Stop.find.byId(stop_id);
        Route route = Route.find.byId(route_id);

        return ok(nearbydirectedroute.render(stop, route));
    }

    public static Result nearbyTimes(String stop_id, String route_id, Integer direction) {
        return ok("");
    }
}