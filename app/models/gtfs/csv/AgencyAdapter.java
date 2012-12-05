package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Agency;

public class AgencyAdapter extends BaseAdapter implements CSVEntryParser<Agency> {
    @Override
    public Agency parseEntry(String... data) {
        Agency agency = new Agency();

        agency.id = data[mapping.get("agency_id")];
        agency.name = data[mapping.get("agency_name")];
        agency.url = data[mapping.get("agency_url")];
        agency.timezone = data[mapping.get("agency_timezone")];
        agency.lang = data[mapping.get("agency_lang")];
        agency.phone = data[mapping.get("agency_phone")];

        return agency;
    }
}
