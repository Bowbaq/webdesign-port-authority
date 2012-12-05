package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Poi extends Model {
    @Id
    public Long id;
   
    @Constraints.Required
    @Constraints.MaxLength(64)
    public String name;

    @Constraints.Required
    public Double lat;
    
    @Constraints.Required
    public Double lng;

    public static Finder<Long,Poi> find = new Finder<Long, Poi>(
        Long.class, Poi.class
    );
}