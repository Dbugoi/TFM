package es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class FieldsToStringParser {
    private FieldsToStringParser() {}

    public static String parse(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        Class<?> clazz = o.getClass();
        List<String> fieldsValues = new LinkedList<>();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                fieldsValues.add(String.format("%s=%s", field.getName(), field.get(o)));
            } catch (Exception e) { /* do nothing */ }
        }

        return String.format("%s [%s]", clazz.getSimpleName(), String.join(", ", fieldsValues));
    }
}
