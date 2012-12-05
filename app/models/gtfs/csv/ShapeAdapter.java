package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Shape;
import models.gtfs.ShapeFragment;

import java.util.HashMap;
import java.util.Map;

public class ShapeAdapter extends BaseAdapter implements CSVEntryParser<Shape> {

    private static Map<String, Shape> shapes = new HashMap<String, Shape>();

    @Override
    public Shape parseEntry(String... data) {
        String id = data[mapping.get("shape_id")];
        Shape s;
        if(!shapes.containsKey(id)) {
            s = new Shape();
            s.id = id;
            shapes.put(id, s);
        } else {
            s = shapes.get(id);
        }

        ShapeFragment f = new ShapeFragment();
        f.lat = Double.parseDouble(data[mapping.get("shape_pt_lat")]);
        f.lng = Double.parseDouble(data[mapping.get("shape_pt_lon")]);
        f.seq = Long.parseLong(data[mapping.get("shape_pt_sequence")]);
        f.shape = s;
        s.fragments.add(f);

        return s;
    }

    public Map<String, Shape> getShapes() {
        return shapes;
    }
}
