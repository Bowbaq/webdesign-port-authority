package models.gtfs;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import play.cache.Cache;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

            routes = new ArrayList<Route>(tmp_routes.values());
            Cache.set("route_stops", routes);
        }

        return routes;
    }

    public List<Stop> inboundStops() {
        return this.inbound;
    }

    public List<Stop> outboundStops() {
        return this.outbound;
    }

/*    public List<Stop> stops(Integer direction) {
        List<Stop> stops = (List<Stop>) Cache.get("route_stops_" + id + "_" + direction);
        if(stops == null ) {
            Logger.info("Cache miss : " + "route_stops_" + id + "_" + direction);

            String sql = "SELECT DISTINCT(stop.id), stop.code, stop.name, stop.lat, stop.lng, stop.zone "
                + "FROM stop JOIN stop_time ON stop.id = stop_time.stop_id JOIN trip ON trip.id = stop_time.trip_id"
            ;

            RawSql raw = RawSqlBuilder
                .parse(sql)
                .columnMapping("DISTINCT(stop.id)", "id")
                .columnMapping("stop.code", "code")
                .columnMapping("stop.name", "name")
                .columnMapping("stop.lat", "lat")
                .columnMapping("stop.lng", "lng")
                .columnMapping("stop.zone", "zone")
                .create()
            ;

            stops =  Ebean.find(Stop.class)
                .setRawSql(raw)
                .where().eq("trip.route_id", id)
                .where().eq("trip.direction", direction)
                .order().asc("stop.name")
                .findList()
            ;

            Cache.set("route_stops_" + id + "_" + direction, stops);
        }

        return stops;
    }*/
    
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