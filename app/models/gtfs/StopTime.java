package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

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


    public static Finder<String,StopTime> find = new Finder<String, StopTime>(
            String.class, StopTime.class
    );
}