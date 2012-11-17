package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Trip extends Model {

    @Id
    @Constraints.Required
    public String id;

    @Constraints.Required
    @ManyToOne
    public Route route;

    @Constraints.Required
    @ManyToOne
    public Calendar service;

    @Constraints.Required
    @ManyToOne
    public Shape shape;

    @Constraints.Required
    @Constraints.MaxLength(128)
    public String headsign;

    @Constraints.Required
    @Constraints.MaxLength(2)
    public String direction;

    public static Finder<String,Trip> find = new Finder<String, Trip>(
            String.class, Trip.class
    );
}