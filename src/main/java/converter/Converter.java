package converter;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface Converter {
    Map<String, Object> toMap(Object object) throws IllegalAccessException;
    Object toObject(Class<?> clazz, Map<String, Object> map) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
    void printFields(Object object) throws IllegalAccessException;
}
