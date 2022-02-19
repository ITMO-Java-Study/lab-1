import converter.Converter;
import converter.ReflectionConverter;
import data.MyClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class ReflectionConverterUnitTest {
    @ParameterizedTest
    @NullSource
    public void givenNull_whenConvertToMap_thenReturnNull(MyClass myClass) throws IllegalAccessException {
        Converter converter = new ReflectionConverter();

        logTestingValues(myClass);

        Map<String, Object> actualMyClassConvertedToMap = converter.toMap(myClass);

        assertNull(actualMyClassConvertedToMap);
    }

    @ParameterizedTest
    @MethodSource("provideMyClassFilledWithDefaultFieldValuesForToMapTests")
    public void givenMyClassObjectFilledWithDefaultValues_whenConvertToMap_thenReturnMap(MyClass myClass) throws IllegalAccessException {
        Converter converter = new ReflectionConverter();

        logTestingValues(myClass);

        Map<String, Object> expectedMyClassConvertedToMap = fillMapWithMyClassFieldValues(myClass);
        Map<String, Object> actualMyClassConvertedToMap = converter.toMap(myClass);

        assertEquals(expectedMyClassConvertedToMap, actualMyClassConvertedToMap);
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void givenNullAndEmptyMap_whenConvertToMyClassObject_thenReturnMyClassObject(Map<String, Object> map) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Converter converter = new ReflectionConverter();

        logTestingValues(map);

        MyClass expectedMapConvertedToMyClass = getMyClassFilledWihMapEntries(map);
        MyClass actualMapConvertedToMyClass = (MyClass) converter.toObject(MyClass.class, map);

        assertEquals(expectedMapConvertedToMyClass, actualMapConvertedToMyClass);
    }

    @ParameterizedTest
    @MethodSource("provideMapFilledWithDefaultValuesForToObjectTests")
    public void givenMapFilledWithDefaultValues_whenConvertToMyClassObject_thenReturnMyClassObject(Map<String, Object> map) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Converter converter = new ReflectionConverter();

        logTestingValues(map);

        MyClass expectedMapConvertedToMyClass = getMyClassFilledWihMapEntries(map);
        MyClass actualMapConvertedToMyClass = (MyClass) converter.toObject(MyClass.class, map);

        assertEquals(expectedMapConvertedToMyClass, actualMapConvertedToMyClass);
    }

    @ParameterizedTest
    @MethodSource("provideMapFilledWithDefaultValuesForToObjectTests")
    public void givenMapFilledWithDefaultValuesPlusExtraValue_whenConvertToMyClassObject_thenReturnMyClassObject(Map<String, Object> map) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Converter converter = new ReflectionConverter();
        map.put("myNonameValue", 100500);

        logTestingValues(map);

        MyClass expectedMapConvertedToMyClass = getMyClassFilledWihMapEntries(map);
        MyClass actualMapConvertedToMyClass = (MyClass) converter.toObject(MyClass.class, map);

        assertEquals(expectedMapConvertedToMyClass, actualMapConvertedToMyClass);
    }

    @ParameterizedTest
    @MethodSource("provideMapFilledWithDefaultValuesForToObjectTests")
    public void givenMapFilledWithDefaultValuesMyIntIsMissed_whenConvertToMyClassObject_thenReturnMyClassObject(Map<String, Object> map) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Converter converter = new ReflectionConverter();
        map.remove("myInt");

        logTestingValues(map);

        MyClass expectedMapConvertedToMyClass = getMyClassFilledWihMapEntries(map);
        MyClass actualMapConvertedToMyClass = (MyClass) converter.toObject(MyClass.class, map);

        assertEquals(expectedMapConvertedToMyClass, actualMapConvertedToMyClass);
    }

    private void logTestingValues(Object valuesContainer) {
        log.info("Testing with: ");
        log.info(valuesContainer == null ? null : valuesContainer.toString());
    }

    private Map<String, Object> fillMapWithMyClassFieldValues(MyClass myClass) {
        if (myClass != null) {
            Map<String, Object> map = new HashMap<>();

            map.put("myInt", myClass.getMyInt());
            map.put("myFloat", myClass.getMyFloat());
            map.put("myDouble", myClass.getMyDouble());
            map.put("myString", myClass.getMyString());
            map.put("myList", myClass.getMyList());
            map.put("mySet", myClass.getMySet());
            map.put("myQueue", myClass.getMyQueue());
            map.put("myBigDecimal", myClass.getMyBigDecimal());

            return map;
        } else {
            return null;
        }
    }

    private static Stream<Map<String, Object>> provideMapFilledWithDefaultValuesForToObjectTests() {
        Map<String, Object> map = new HashMap<>();

        map.put("myInt", 0);
        map.put("myFloat", 0.f);
        map.put("myDouble", 0.d);
        map.put("myString", "");
        map.put("myList", new ArrayList<>());
        map.put("mySet", new HashSet<>());
        map.put("myQueue", new PriorityQueue<>());
        map.put("myBigDecimal", new BigDecimal("100.001"));

        return Stream.of(map);
    }

    private static Stream<MyClass> provideMyClassFilledWithDefaultFieldValuesForToMapTests() {
        return Stream.of(MyClass.builder()
                .myInt(0)
                .myFloat(0.f)
                .myDouble(0.d)
                .myString("")
                .myList(new ArrayList<>())
                .mySet(null)
                .myQueue(new PriorityQueue<>())
                .myBigDecimal(new BigDecimal("100.001"))
                .build());
    }

    @SuppressWarnings("unchecked")
    private MyClass getMyClassFilledWihMapEntries(Map<String, Object> map) {
        if (map != null) {
            return MyClass.builder()
                    .myInt((int) map.getOrDefault("myInt", 0))
                    .myFloat((float) map.getOrDefault("myFloat", 0.f))
                    .myDouble((double) map.getOrDefault("myDouble", 0.d))
                    .myString((String) map.get("myString"))
                    .myList((List<Integer>) map.get("myList"))
                    .mySet((Set<Integer>) map.get("mySet"))
                    .myQueue((Queue<Integer>) map.get("myQueue"))
                    .myBigDecimal((BigDecimal) map.get("myBigDecimal"))
                    .build();
        } else {
            return null;
        }
    }
}
