package models.gtfs;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Agency extends Model {
    @Id
    public String id;

    @Constraints.Required
    public String name;

    public String url;
    public String timezone;
    public String lang;
    public String phone;

    public static Finder<String, Agency> find = new Finder<String, Agency>(
        String.class, Agency.class
    );
}