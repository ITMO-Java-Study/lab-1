package converter;

import java.lang.reflect.*;
import java.util.*;

public class ReflectionConverter implements Converter {
    @Override
    public void printFields(Object object) throws IllegalAccessException {
        for(Field field : object.getClass().getDeclaredFields()) {
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
        if (map != null) {
            Constructor<?>[] allConstructors = clazz.getDeclaredConstructors();
            Object object = instantiate(clazz);

            for (Constructor<?> ctor : allConstructors) {
                paramLoop:
                for (Parameter param : ctor.getParameters()) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        Object value = entry.getValue();
                        if (value != null) {
                            Class<?> parameterType = param.getType().isPrimitive() ? toPrimitive(value) : value.getClass();
                            if (parameterType != null && param.getType().isAssignableFrom(parameterType)) {
                                for (Field field : clazz.getDeclaredFields()) {
                                    if (field.getName().equals(entry.getKey())) {
                                        setFieldValue(object, field, value);
                                        continue paramLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return object;
        } else {
            return null;
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
        if (object instanceof Integer) {
            return int.class;
        } else if (object instanceof Long) {
            return long.class;
        } else if (object instanceof Short) {
            return short.class;
        } else if (object instanceof Float) {
            return float.class;
        } else if (object instanceof Double) {
            return double.class;
        } else if (object instanceof Byte) {
            return byte.class;
        } else if (object instanceof Boolean) {
            return boolean.class;
        } else if (object instanceof Character) {
            return char.class;
        } else {
            return null;
        }
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
