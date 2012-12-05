package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Calendar;
import models.gtfs.CalendarDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class CalendarDateAdapter extends BaseAdapter implements CSVEntryParser<CalendarDate> {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private Map<String, Calendar> calendars;

    @Override
    public CalendarDate parseEntry(String... data) {
        CalendarDate date = new CalendarDate();

        date.calendar = calendars.get(data[mapping.get("service_id")]);
        try {
            date.date = formatter.parse(data[mapping.get("date")]);
        } catch (ParseException e) {}
        date.exception_type = data[mapping.get("exception_type")].equals("1") ? CalendarDate.ADDED : CalendarDate.REMOVED;

        return date;
    }
    
    public void setCalendars(Map<String, Calendar> calendars) {
        this.calendars = calendars;
    }
}
