package models.gtfs;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class CalendarDate extends Model {
    public static final int ADDED = 1;
    public static final int REMOVED = 2;
    
    @Id
    public Long id;

    @Constraints.Required
    @ManyToOne
    public Calendar calendar;

    @Constraints.Required
    public Date date;

    @Constraints.Required
    public Integer exception_type;

    public static Calendar hasException(Date date) {
        CalendarDate exception = Ebean.find(CalendarDate.class)
            .where().eq("date", date)
            .where().eq("exception_type", ADDED)
            .findUnique();

        return exception != null ? exception.calendar : null;
    };
}
