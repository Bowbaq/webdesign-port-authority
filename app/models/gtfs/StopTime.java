package models.gtfs;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import org.apache.commons.lang3.time.DateUtils;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

@Entity
public class StopTime extends Model {
    @Id
    public Long id;

    @Constraints.Required
    @ManyToOne(targetEntity = Trip.class)
    public Trip trip;

    @Constraints.Required
    @ManyToOne(targetEntity = Stop.class)
    public Stop stop;

    @Constraints.Required
    public Integer stop_seq;

    @Constraints.Required
    public Date arrival;

    @Constraints.Required
    public Date departure;

    public Boolean pickup_type = false;

    public Boolean dropoff_type = false;

    public static List<StopTime> getTimes(Stop stop, Calendar calendar, Date now, Route route, Integer direction) {
        String sql = "SELECT stop_time.id, stop_time.stop_seq, stop_time.arrival, stop_time.departure, stop_time.pickup_type, stop_time.dropoff_type "
                + "FROM stop_time JOIN trip ON trip.id = stop_time.trip_id "
                + "WHERE trip.route_id = :route_id AND trip.service_id = :service_id AND trip.direction = :direction"
                ;

        RawSql raw = RawSqlBuilder
                .parse(sql)
                .columnMapping("stop_time.id", "id")
                .columnMapping("stop_time.stop_seq", "stop_seq")
                .columnMapping("stop_time.arrival", "arrival")
                .columnMapping("stop_time.departure", "departure")
                .columnMapping("stop_time.pickup_type", "pickup_type")
                .columnMapping("stop_time.dropoff_type", "dropoff_type")
                .create()
                ;

        return Ebean.find(StopTime.class)
                .setRawSql(raw)
                .setParameter("route_id", route.id)
                .setParameter("service_id", calendar.id)
                .setParameter("direction", direction)
                .where().eq("stop_time.stop_id", stop.id)
                .order().asc("stop_time.departure")
                .findList()
                ;
    }

    public String remaining() {
        Long now = DateUtils.getFragmentInMinutes(new Date(), java.util.Calendar.DAY_OF_MONTH);
        Long stop = DateUtils.getFragmentInMinutes(departure, java.util.Calendar.DAY_OF_MONTH);

        Long remaining = stop - now;
        if(remaining < 0) {
            return "departed";
        }
        if(remaining == 0) {
            return "now";
        }
        if(remaining > 60) {
            return "> 1h";
        }

        return remaining + " min";
    }

    public static Finder<String,StopTime> find = new Finder<String, StopTime>(
            String.class, StopTime.class
    );
}