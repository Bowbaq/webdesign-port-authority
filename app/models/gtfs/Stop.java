package models.gtfs;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import org.apache.commons.lang3.time.DateUtils;
import play.Logger;
import play.cache.Cache;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@Entity
public class Stop extends Model {
    @Id
    @Constraints.MaxLength(6)
    public String id;

    @Constraints.Required
    @Constraints.MaxLength(16)
    public String code;

    @Constraints.Required
    @Constraints.MaxLength(64)
    public String name;

    @Constraints.Required
    public Double lat;

    @Constraints.Required
    public Double lng;

    @Constraints.Required
    @Constraints.MaxLength(4)
    public String zone;


    public static Finder<String,Stop> find = new Finder<String, Stop>(
            String.class, Stop.class
    );

    public static List<Stop> getNearby(Double lat, Double lng) {
        return Ebean.find(Stop.class)
           .setParameter("lat", lat)
           .setParameter("lng", lng)
           .where().le("SQRT(4474.81 * (lat - " + lat + ") * (lat - " + lat + ") + 2809 * (lng - " + lng + ") * (lng - " + lng + "))", 0.5)
           .findList()
        ;
    }

    public List<Route> getRoutes() {
        String sql = "SELECT DISTINCT(route.id), route.short_name, route.long_name, route.type "
            + "FROM route JOIN trip ON trip.route_id = route.id JOIN stop_time ON stop_time.trip_id = trip.id "
            + "WHERE stop_time.stop_id = :stop"
        ;

        RawSql raw = RawSqlBuilder
                .parse(sql)
                .columnMapping("DISTINCT(route.id)", "id")
                .columnMapping("route.short_name", "short_name")
                .columnMapping("route.long_name", "long_name")
                .columnMapping("route.type", "type")
                .create()
        ;

        return Ebean.find(Route.class)
                .setRawSql(raw)
                .setParameter("stop", id)
                .findList()
        ;
    }

    public TreeMap<Date, List<StopTime>> getSchedule(String route_id, Integer direction) {
        Route route = Route.find.byId(route_id);
        TreeMap<Date, List<StopTime>> schedule = new TreeMap<Date, List<StopTime>>();
        if(route == null) {
            return schedule;
        }

        Date day = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);  // Today
        Boolean first = true;
        for (int i = 0; i < 7; i++) {
            Calendar cal = Calendar.getCurrent(day, route, direction);
            if(null != cal) {
                List<StopTime> times = StopTime.getTimes(this, cal, day, route, direction);
                if (first) {
                    List<StopTime> notdeparted = new ArrayList<StopTime>();
                    for(StopTime t : times) {
                        if(!t.remaining().equals("departed")) {
                            notdeparted.add(t);
                        }
                    }
                    times = notdeparted;
                    first = false;
                }
                schedule.put(day, times);
            }
            day = DateUtils.addDays(day, 1);
        }

        return schedule;
    }

    @SuppressWarnings("unchecked")
    public List<StopTime> getDaySchedule(Date day, String route_id, Integer direction) {
        day = DateUtils.truncate(day, java.util.Calendar.DAY_OF_MONTH);
        List<StopTime> schedule = (List<StopTime>) Cache.get(key(day, route_id, direction));
        if (schedule == null) {
            Logger.info("Cache miss : " + key(day, route_id, direction));
            Route route = Route.find.byId(route_id);
            schedule = new ArrayList<StopTime>();

            if(route == null) {
                return schedule;
            }

            Calendar cal = Calendar.getCurrent(day, route, direction);

            if(null != cal) {
                schedule = StopTime.getTimes(this, cal, day, route, direction);
                Cache.set(key(day, route_id, direction), schedule);
            }
        }

        return schedule;
    }

    private String key(Date day, String route_id, Integer direction) {
        return "stop_times_" + route_id + "_" + id + "_" + direction + "_" + day.getTime();
    }
}