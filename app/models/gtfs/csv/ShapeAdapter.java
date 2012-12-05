package models.gtfs.csv;

import com.googlecode.jcsv.reader.CSVEntryParser;
import models.gtfs.Shape;
import models.gtfs.ShapeFragment;

import java.util.HashMap;
import java.util.Map;

public class ShapeAdapter implements CSVEntryParser<Shape> {

    private static Map<String, Shape> shapes = new HashMap<String, Shape>();

    @Override
    public Shape parseEntry(String... data) {
        String id = data[0];
        Shape s;
        if(!shapes.containsKey(id)) {
            s = new Shape();
            s.id = id;
            shapes.put(id, s);
        } else {
            s = shapes.get(id);
        }

        ShapeFragment f = new ShapeFragment();
        f.lat = Double.parseDouble(data[1]);
        f.lng = Double.parseDouble(data[2]);
        f.seq = Long.parseLong(data[3]);
        f.shape = s;
        s.fragments.add(f);

        return s;
    }

    public Map<String, Shape> getShapes() {
        return shapes;
    }
}
