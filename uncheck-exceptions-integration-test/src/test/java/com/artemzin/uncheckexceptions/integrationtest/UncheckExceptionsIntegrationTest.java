package com.artemzin.uncheckexceptions.integrationtest;

import org.junit.Test;

import static com.artemzin.uncheckexceptions.UncheckExceptions.thr0w;
import static org.junit.Assert.assertSame;

public class UncheckExceptionsIntegrationTest {

    @Test(expected = Exception.class)
    public void thr0wDoesNotRequireExceptionInMethodSignature() {
        thr0w(new Exception());
    }

    private static final class TestCheckedException extends Exception {

    }

    @Test
    public void thr0wThrowsCheckedExceptionAsIsWithoutDeclaration() {
        TestCheckedException testException = new TestCheckedException();

        try {
            thr0w(testException);
        } catch (Throwable expected) { // Loooool, compiler doesn't let us declare expected exception as TestCheckedException because it doesn't see it in method description xD
            assertSame(testException, expected);
        }
    }
}
