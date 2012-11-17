package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ShapeFragment extends Model {
    @Id
    public Long id;

    @ManyToOne
    public Shape shape;

    @Constraints.Required
    public Double lat;

    @Constraints.Required
    public Double lng;

    @Constraints.Required
    public Long seq;

    public static Finder<String,ShapeFragment> find = new Finder<String, ShapeFragment>(
            String.class, ShapeFragment.class
    );
}