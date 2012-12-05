package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CalendarAdapter extends BaseAdapter implements CSVEntryParser<Calendar> {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    @Override
    public Calendar parseEntry(String... data) {
        Calendar cal = new Calendar();

        cal.id = data[mapping.get("service_id")];

        cal.monday = (data[mapping.get("monday")].equals("1"));
        cal.tuesday = (data[mapping.get("tuesday")].equals("1"));
        cal.wednesday = (data[mapping.get("wednesday")].equals("1"));
        cal.thursday = (data[mapping.get("thursday")].equals("1"));
        cal.friday = (data[mapping.get("friday")].equals("1"));
        cal.saturday = (data[mapping.get("saturday")].equals("1"));
        cal.sunday = (data[mapping.get("sunday")].equals("1"));

        try {
            cal.start_date = formatter.parse(data[mapping.get("start_date")]);
            cal.end_date = formatter.parse(data[mapping.get("end_date")]);
        } catch (ParseException e) {}

        return cal;
    }
}
