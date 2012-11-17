package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class CalendarDate extends Model{
    @Id
    public Long id;

    @Constraints.Required
    @ManyToOne(targetEntity = Calendar.class)
    public Calendar Calendar;

    @Constraints.Required
    public Date date;

    @Constraints.Required
    public String exception_type;
}
