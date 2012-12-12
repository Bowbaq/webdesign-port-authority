package models.gtfs;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import play.Logger;
import play.cache.Cache;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

@Entity
public class Route extends Model {
    @Id
    @Constraints.MaxLength(7)
    public String id;

    @Constraints.Required
    @Constraints.MaxLength(3)
    public String short_name;

    @Constraints.Required
    @Constraints.MaxLength(64)
    public String long_name;

    @Constraints.Required
    public Integer type;

    private Directions directions;

    private List<Stop> inbound;
    private List<Stop> outbound;

    public Route() {
        directions = new Directions();
        inbound = new ArrayList<Stop>();
        outbound = new ArrayList<Stop>();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Route> getAllWithStops() {
        ArrayList<Route> routes = (ArrayList<Route>) Cache.get("route_stops");

        if(routes == null) {
            HashMap<String, Route> tmp_routes = new HashMap<String, Route>();

            String sql = "SELECT route_id, route_short_name, route_long_name, route_type, route_direction, "
                    + "stop_id, stop_code, stop_name, stop_lat, stop_lng, stop_zone "
                    + "FROM route_stops ORDER BY route_id ASC, route_direction ASC, stop_id ASC";

            SqlQuery query = Ebean.createSqlQuery(sql);

            List<SqlRow> result = query.findList();

            for(SqlRow row : result) {
                String route_id = row.getString("route_id");
                Route route = tmp_routes.get(route_id);
                if(route == null) {
                    route = new Route();
                    route.id = route_id;
                    route.short_name = row.getString("route_short_name");
                    route.long_name = row.getString("route_long_name");
                    route.type = row.getInteger("route_type");

                    tmp_routes.put(route_id, route);
                }

                Integer direction = row.getInteger("route_direction");
                route.directions.add(direction);

                Stop stop = new Stop();
                stop.id = row.getString("stop_id");
                stop.code = row.getString("stop_code");
                stop.name = row.getString("stop_name");
                stop.lat = row.getDouble("stop_lat");
                stop.lng = row.getDouble("stop_lng");
                stop.zone = row.getString("stop_zone");

                if(direction == Trip.INBOUND) {
                    route.inbound.add(stop);
                } else {
                    route.outbound.add(stop);
                }
            }

            routes = sortAll(new ArrayList<Route>(tmp_routes.values()));
            Cache.set("route_stops", routes);
        }

        return routes;
    }

    private static ArrayList<Route> sortAll(ArrayList<Route> shuffled) {
        Collections.sort(shuffled, new Comparator<Route>() {
            @Override
            public int compare(Route route1, Route route2) {
                return route1.id.compareTo(route2.id);
            }
        });
        
        Comparator<Stop> stopcomp = new Comparator<Stop>() {
            @Override
            public int compare(Stop stop, Stop stop1) {
                return stop.name.compareTo(stop1.name);
            }
        };

        for(Route route : shuffled) {
            Collections.sort(route.inbound, stopcomp);
            Collections.sort(route.outbound, stopcomp);
        }

        return shuffled;
    }

    public List<Stop> inboundStops() {
        return this.inbound;
    }

    public List<Stop> outboundStops() {
        return this.outbound;
    }
    
    public Directions directions() {
        if (null == directions) {
            directions = new Directions();

            String sql = "SELECT DISTINCT(direction) FROM trip WHERE trip.route_id = :id";

            SqlQuery query = Ebean.createSqlQuery(sql);
            query.setParameter("id", id);

            List<SqlRow> result = query.findList();

            for(SqlRow row : result) {
                directions.add(row.getInteger("direction"));
            }
        }

        return directions;
    }

    public static Finder<String,Route> find = new Finder<String, Route>(
            String.class, Route.class
    );

    @Override
    public String toString(){
        return short_name;
    }

    public class Directions {
        private boolean inbound = false;
        private boolean outbound = false;


        public void add(Integer direction) {
            if(direction == Trip.INBOUND) {
                inbound = true;
            }

            if(direction == Trip.OUTBOUND) {
                outbound = true;
            }
        }

        public boolean inbound() {
            return inbound;
        }

        public boolean outbound() {
            return outbound;
        }
    }
}