package models.gtfs.csv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class BaseAdapter {
    protected boolean is_first_line = true;
    protected Map<String, Integer> mapping = new HashMap<String, Integer>();

    public void setMapping(List<String> first_line) {
        for(int i = 0; i < first_line.size(); i++) {
            mapping.put(first_line.get(i), i);
        }
    }
}
