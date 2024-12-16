package DAO;

import connection.attribute.Attribute;
import connection.callback.ParsingCallback;

import java.util.HashSet;

public class Parser<T> {

    ParsingCallback<T> callback;

    public Parser(ParsingCallback<T> callback) {
        this.callback = callback;
    }

    public void parseObject(T object, HashSet<Attribute> values) {
        for (Attribute attribute: values) {
            if (attribute.getValue() instanceof String) callback.onCallbackString(object, attribute);
            else if (attribute.getValue() instanceof Integer) callback.onCallbackInteger(object, attribute);
            else if (attribute.getValue() instanceof Double) callback.onCallbackDouble(object, attribute);
            else if (attribute.getValue() instanceof Boolean) callback.onCallbackBoolean(object, attribute);

        }
    }
}
