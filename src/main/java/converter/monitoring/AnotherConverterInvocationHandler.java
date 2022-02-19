package converter.monitoring;

import converter.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class AnotherConverterInvocationHandler implements InvocationHandler {
    private final Converter delegate;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        log.info("Execution of " + method.getName() + " with parameters " + Arrays.toString(args));

        Object result = method.invoke(delegate, args);
        log.info("Object field values after execution of " + method.getName() + ": ");
        log.info(result.toString());

        return result;
    }
}
