package com.artemzin.uncheckexceptions.codegen;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class JavaTest {

    private static final class TestCheckedException extends Exception {

    }

    @Test
    public void thr0wThrowsCheckedExceptionAsIs() throws NoSuchMethodException, IllegalAccessException {
        Class<?> UncheckExceptions = new TestClassLoader()
                .loadClass("com.artemzin.uncheckexceptions.UncheckExceptions");

        Method thr0w = UncheckExceptions.getDeclaredMethod("thr0w", Throwable.class);

        TestCheckedException testException = new TestCheckedException();

        try {
            thr0w.invoke(null, testException);
            fail();
        } catch (InvocationTargetException expected) {
            assertSame(testException, expected.getCause());
        }
    }
}
