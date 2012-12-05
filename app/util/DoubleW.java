package util;

import play.mvc.PathBindable;

public class DoubleW implements PathBindable<DoubleW> {

    public Double value = null;

    @Override
    public DoubleW bind(String key, String value) {
        DoubleW res =  new DoubleW();
        res.value = Double.parseDouble(value);
        return res;
    }

    @Override
    public String unbind(String key) {
        return key + "=" + value;
    }

    @Override
    public String javascriptUnbind() {
        return value.toString();
    }

}