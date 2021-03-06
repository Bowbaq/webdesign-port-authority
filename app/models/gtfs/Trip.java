package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Trip extends Model {
    
    public static final int OUTBOUND = 0;
    public static final int INBOUND = 1;

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
    public Integer direction;

    public static Finder<String,Trip> find = new Finder<String, Trip>(
            String.class, Trip.class
    );
}