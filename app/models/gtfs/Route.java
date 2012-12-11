package models.gtfs;

import com.avaje.ebean.*;
import play.Logger;
import play.cache.Cache;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
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

    public List<Stop> stops(Integer direction) {
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
    }
    
    public Directions directions() {
        if (null == directions) {
            directions = new Directions();

            String sql = "SELECT DISTINCT(direction) FROM trip WHERE trip.route_id = :id";

            SqlQuery query = Ebean.createSqlQuery(sql);
            query.setParameter("id", id);

            // execute the query returning a List of MapBean objects
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