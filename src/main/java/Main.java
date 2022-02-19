import converter.Converter;
import converter.ReflectionConverter;
import converter.monitoring.ConverterInvocationHandler;
import converter.monitoring.AnotherConverterInvocationHandler;
import data.MyClass;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.*;

public class Main {
    private static final Converter converter = (Converter) Proxy.newProxyInstance(
            ClassLoader.getSystemClassLoader(),
            new Class[]{Converter.class},
            new AnotherConverterInvocationHandler(
                    (Converter) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                    new Class[]{Converter.class},
                    new ConverterInvocationHandler(new ReflectionConverter()))
            )
    );

    public static void main(String[] args) throws Exception {
        MyClass myClass = MyClass.builder()
                .myInt(0)
                .myFloat(0.0f)
                .myDouble(0.0d)
                .myString("")
                .myList(null)
                .mySet(null)
                .myQueue(new PriorityQueue<>())
                .myBigDecimal(new BigDecimal("100.001"))
                .build();

        //converter.printFields(myClass);
        Map<String, Object> map = converter.toMap(myClass);
        System.out.println(map);
        System.out.println(myClass);
        System.out.println(converter.toObject(MyClass.class, map));
        System.out.println(myClass.equals(converter.toObject(MyClass.class, map)));
    }
}
