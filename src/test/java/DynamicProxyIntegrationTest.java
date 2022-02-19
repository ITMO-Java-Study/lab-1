import converter.Converter;
import converter.monitoring.AnotherConverterInvocationHandler;
import converter.monitoring.ConverterInvocationHandler;
import data.MyClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;

public class DynamicProxyIntegrationTest {
    @Test
    public void whenConverterInvocationHandlerClassMocked_thenToMapWithMockedMyClassWorks() throws IllegalAccessException {
        ConverterInvocationHandler mockedConverterInvocationHandler = Mockito.mock(ConverterInvocationHandler.class);

        Converter converter = (Converter) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{Converter.class},
                mockedConverterInvocationHandler
        );

        converter.toMap(Mockito.mock(MyClass.class));
    }

    @Test
    public void whenAnotherConverterInvocationHandlerClassMocked_thenToMapWithMockedMyClassWorks() throws IllegalAccessException {
        AnotherConverterInvocationHandler mockedConverterInvocationHandler = Mockito.mock(AnotherConverterInvocationHandler.class);

        Converter converter = (Converter) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{Converter.class},
                mockedConverterInvocationHandler
        );

        converter.toMap(Mockito.mock(MyClass.class));
    }
}
