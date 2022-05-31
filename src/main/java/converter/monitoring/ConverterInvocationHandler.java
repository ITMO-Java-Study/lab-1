package converter.monitoring;

import converter.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class ConverterInvocationHandler implements InvocationHandler {
    private final Converter delegate;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Instant start = Instant.now();
        Object result = method.invoke(delegate, args);
        Instant stop = Instant.now();
        log.info("Execution of " + method.getName() + " finished in " + Duration.between(start, stop).toNanos() + " ns");

        return result;
    }
}
