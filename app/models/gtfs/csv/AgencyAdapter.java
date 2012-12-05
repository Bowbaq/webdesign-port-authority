package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Agency;

public class AgencyAdapter implements CSVEntryParser<Agency> {

    @Override
    public Agency parseEntry(String... data) {
        Agency agency = new Agency();

        agency.id = data[0];
        agency.name = data[1];
        agency.url = data[2];
        agency.timezone = data[3];
        agency.lang = data[4];
        agency.phone = data[5];

        return agency;
    }
}
