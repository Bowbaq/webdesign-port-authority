package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;
import views.html.register;

public class Application extends Controller {
    public static final String FLASH_MESSAGE = "message";
    public static final String FLASH_ERROR = "error";

    public static Result login() {
        if(session().get("email") != null) {
            return redirect(
                    routes.Transit.favorites()
            );
        }

        return ok(
            login.render(form(Login.class))
        );
    }
    
    public static Result register() {
        if(session().get("email") != null) {
            return redirect(
                routes.Transit.favorites()
            );
        }

        return ok(
            register.render(form(Register.class))
        );
    }
    
    public static Result newuser() {
        User u;
        final Form<Register> form = form(Register.class).bindFromRequest();
        Ebean.beginTransaction();
        try {
            u = User.findByEmail(form.field("email").value());
            if(null != u) {
                flash(FLASH_ERROR, "A user with the same email address already exists");
                Ebean.rollbackTransaction();
                return badRequest(register.render(form));
            }

            u = new User();
            u.email = form.field("email").value();
            u.password = BCrypt.hashpw(form.field("password").value(), BCrypt.gensalt());
            u.save();
            Ebean.commitTransaction();
        } catch (Exception e) {
            flash(FLASH_ERROR, "An error occurred during the registration process, please try again");
            Ebean.rollbackTransaction();
            return badRequest(register.render(form));
        } finally {
            Ebean.endTransaction();
        }

        session("email", u.email);
        return redirect(
                routes.Transit.favorites()
        );
    }

    public static Result authenticate() {
        final Form<Login> form = form(Login.class).bindFromRequest();
        if(form.hasErrors()) {
            return badRequest(login.render(form));
        } else {
            session("email", form.field("email").value());
            return redirect(
                routes.Transit.favorites()
            );
        }
    }

    public static Result logout() {
        session().clear();
        flash(FLASH_MESSAGE, "You've been logged out");
        return redirect(
                routes.Application.login()
        );
    }


    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if(User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }

    }
    
    public static class Register {
        @Constraints.Required
        @Constraints.Email
        public String email;

        @Constraints.Required
        @Constraints.MinLength(8)
        public String password;
    }
}