package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
public class Calendar extends Model {
    @Id
    @Constraints.Required
    @Constraints.MaxLength(32)
    public String id;
    
    public Boolean monday = false;
    public Boolean tuesday = false;
    public Boolean wednesday = false;
    public Boolean thursday = false;
    public Boolean friday = false;
    public Boolean saturday = false;
    public Boolean sunday = false;

    @Constraints.Required
    public Date start_date;

    @Constraints.Required
    public Date end_date;

    public static Calendar getCurrent(Date now, Route route, Integer direction) {
        Calendar calendar = CalendarDate.hasException(now);
        if(null == calendar) {
            Trip trip = null;
            List<Trip> trips = Trip.find
                    .fetch("service")
                    .where().eq("route", route)
                    .where().eq("direction", direction)
                    .where().eq("service." + new SimpleDateFormat("EEEE", Locale.ENGLISH).format(now).toLowerCase(), 1)
                    .where().betweenProperties("service.start_date", "service.end_date", now)
                    .findList()
                    ;
            if(trips.size() > 0) {
                trip = trips.get(0);
            }

            calendar = trip != null ? trip.service : null;
        }

        return calendar;
    }

    public static Finder<String,Calendar> find = new Finder<String, Calendar>(
        String.class, Calendar.class
    );
}