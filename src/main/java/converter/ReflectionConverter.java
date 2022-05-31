package converter;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ReflectionConverter implements Converter {
    Map<Class<?>, Class<?>> primitiveTypes = getPrimitiveTypes();

    @Override
    public void printFields(Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            System.out.println(field.getName());
            System.out.println(getFieldValue(object, field));
        }
    }

    @Override
    public Map<String, Object> toMap(Object object) throws IllegalAccessException {
        if (object != null) {
            Map<String, Object> map = new HashMap<>();
            for (Field field : object.getClass().getDeclaredFields()) {
                map.put(field.getName(), getFieldValue(object, field));
            }
            return map;
        } else {
            return null;
        }
    }

    @Override
    public Object toObject(Class<?> clazz, Map<String, Object> map) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (map == null) {
            return null;
        }
        Object object = instantiate(clazz);
        setFields(clazz, object, map);
        return object;
    }

    private void setFields(Class<?> clazz, Object object, Map<String, Object> map) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            Object value = map.get(fieldName);
            if (value != null) {
                Class<?> parameterType = field.getType().isPrimitive() ? toPrimitive(value) : value.getClass();
                if (parameterType != null && field.getType().isAssignableFrom(parameterType)) {
                    setFieldValue(object, field, value);
                } else {
                    log.trace(object.getClass().getName() + " object's " + fieldName + " field type is not supported!");
                }
            }
        }
    }

    private Object getFieldValue(Object object, Field field) throws IllegalAccessException {
        boolean accessModifier = field.canAccess(object);
        field.setAccessible(true);
        Object fieldValue = field.get(object);
        field.setAccessible(accessModifier);
        return fieldValue;
    }

    private void setFieldValue(Object object, Field field, Object value) throws IllegalAccessException {
        boolean accessModifier = field.canAccess(object);
        field.setAccessible(true);
        field.set(object, value);
        field.setAccessible(accessModifier);
    }

    private Class<?> toPrimitive(Object object) {
        return primitiveTypes.get(object.getClass());
    }

    private Map<Class<?>, Class<?>> getPrimitiveTypes() {
        Map<Class<?>, Class<?>> primitiveTypes = new HashMap<>();
        primitiveTypes.put(Integer.class, int.class);
        primitiveTypes.put(Short.class, short.class);
        primitiveTypes.put(Long.class, long.class);
        primitiveTypes.put(Float.class, float.class);
        primitiveTypes.put(Double.class, double.class);
        primitiveTypes.put(Byte.class, byte.class);
        primitiveTypes.put(Boolean.class, boolean.class);
        primitiveTypes.put(Character.class, char.class);
        return primitiveTypes;
    }

    private Object instantiate(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getConstructors()[0];
        List<Object> params = new ArrayList<>();

        for (Class<?> parameterType : constructor.getParameterTypes()) {
            if (parameterType.isPrimitive()) {
                params.add(getBasicPrimitive(parameterType));
            } else {
                params.add(null);
            }
        }
        return constructor.newInstance(params.toArray());
    }

    private Object getBasicPrimitive(Class<?> primitiveType) {
        if (primitiveType.equals(int.class)) {
            return 0;
        } else if (primitiveType.equals(long.class)) {
            return 0L;
        } else if (primitiveType.equals(short.class)) {
            return (short) 0;
        } else if (primitiveType.equals(float.class)) {
            return 0.f;
        } else if (primitiveType.equals(double.class)) {
            return 0.d;
        } else if (primitiveType.equals(byte.class)) {
            return (byte) 0;
        } else if (primitiveType.equals(boolean.class)) {
            return false;
        } else if (primitiveType.equals(char.class)) {
            return '\0';
        } else {
            throw new RuntimeException("Unknown primitive type");
        }
    }
}
