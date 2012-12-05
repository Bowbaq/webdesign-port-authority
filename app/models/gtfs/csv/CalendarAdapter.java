package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CalendarAdapter implements CSVEntryParser<Calendar> {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    @Override
    public Calendar parseEntry(String... data) {
        Calendar cal = new Calendar();

        cal.id = data[0];

        cal.monday = (data[1].equals("1"));
        cal.tuesday = (data[2].equals("1"));
        cal.wednesday = (data[3].equals("1"));
        cal.thursday = (data[4].equals("1"));
        cal.friday = (data[5].equals("1"));
        cal.saturday = (data[6].equals("1"));
        cal.sunday = (data[7].equals("1"));

        try {
            cal.start_date = formatter.parse(data[8]);
            cal.end_date = formatter.parse(data[9]);
        } catch (ParseException e) {}

        return cal;
    }
}
