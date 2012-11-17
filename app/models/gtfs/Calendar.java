package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

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

    public static Finder<String,Calendar> find = new Finder<String, Calendar>(
        String.class, Calendar.class
    );
}