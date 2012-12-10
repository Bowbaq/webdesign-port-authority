package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import models.gtfs.Route;

import views.html.transit.*;

public class Application extends Controller {
    
    public static Result index() {
        List<Route> routes = Route.find.all();
        return ok(schedule.render(routes));
    }
}