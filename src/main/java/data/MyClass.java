package data;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class MyClass {
    private final int myInt;
    private final float myFloat;
    private final double myDouble;
    private final String myString;
    private final List<Integer> myList;
    private final Set<Integer> mySet;
    private final Queue<Integer> myQueue;
    private final BigDecimal myBigDecimal;
}
