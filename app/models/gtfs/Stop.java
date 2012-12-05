package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

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
}