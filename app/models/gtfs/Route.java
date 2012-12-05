package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

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

    public static Finder<String,Route> find = new Finder<String, Route>(
            String.class, Route.class
    );
}