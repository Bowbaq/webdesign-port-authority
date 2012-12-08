package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.transit.schedule;

public class Application extends Controller {
    
    public static Result index() {
        return ok(schedule.render());
    }
}