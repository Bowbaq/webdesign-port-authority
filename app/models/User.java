package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import org.mindrot.jbcrypt.BCrypt;
    import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class User extends Model {

    @Id
    @Constraints.Required
    @Formats.NonEmpty
    public String email;

    @Constraints.Required
    public String password;

    public static Finder<String, User> find = new Finder<String, User>(
            String.class, User.class
    );

    public static List<User> findAll() {
        return find.all();
    }

    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    /**
     * Authenticate a User.
     */
    public static User authenticate(String email, String password) {
        User u = findByEmail(email);
        return u != null && BCrypt.checkpw(password, u.password) ? u : null;
    }

    public List<Favorite> getAllFavorites() {

        String sql = "SELECT favorite.route_id, favorite.stop_id, favorite.owner_email, favorite.direction, favorite.id "
                + "FROM favorite "
                + "WHERE favorite.owner_email = :user_id"
                ;

        RawSql raw = RawSqlBuilder
                .parse(sql)
                .columnMapping("favorite.route_id", "route.id")
                .columnMapping("favorite.stop_id", "stop.id")
                .columnMapping("favorite.owner_email", "owner.email")
                .columnMapping("favorite.direction", "direction")
                .columnMapping("favorite.id", "id")                
                .create()
                ;

        return Ebean.find(Favorite.class)
                .setRawSql(raw)
                .setParameter("user_id", this.email)
                .findList()
                ;
    }
}