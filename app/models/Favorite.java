package models;

import models.gtfs.Route;
import models.gtfs.Stop;
import models.gtfs.StopTime;
import org.apache.commons.lang3.time.DateUtils;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;
import java.util.Map;

@Entity
public class Favorite extends Model {
    @Id
    @GeneratedValue
    private Long id;

    @Constraints.Required
    @ManyToOne(targetEntity = User.class)
    public User owner;

    @Constraints.Required
    @ManyToOne(targetEntity = Stop.class)
    public Stop stop;

    @Constraints.Required
    @ManyToOne(targetEntity = Route.class)
    public Route route;

    @Constraints.Required
    public int direction;



    public static Finder<String,Favorite> find = new Finder<String, Favorite>(
            String.class, Favorite.class
    );

    public StopTime next() {
        for(StopTime st : stop.getDaySchedule(new Date(), route.id, direction)) {
            if(DateUtils.getFragmentInMinutes(st.departure, java.util.Calendar.DAY_OF_MONTH) > DateUtils.getFragmentInMinutes(new Date(), java.util.Calendar.DAY_OF_MONTH))
                return st;
        }

        return null;

    }
}
