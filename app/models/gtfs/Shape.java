package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Shape extends Model {
    @Id
    @Constraints.MaxLength(8)
    public String id;

    @OneToMany
    public List<ShapeFragment> fragments;

    public static Finder<String,Shape> find = new Finder<String, Shape>(
        String.class, Shape.class
    );
}