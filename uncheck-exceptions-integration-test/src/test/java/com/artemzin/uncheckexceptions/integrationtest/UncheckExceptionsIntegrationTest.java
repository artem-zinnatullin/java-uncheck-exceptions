package com.artemzin.uncheckexceptions.integrationtest;

import com.artemzin.uncheckexceptions.UncheckExceptions;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class UncheckExceptionsIntegrationTest {

    @Test(expected = Exception.class)
    public void thr0wDoesNotRequireExceptionInMethodSignature() {
        UncheckExceptions.thr0w(new Exception());
    }

    private static final class TestCheckedException extends Exception {

    }

    @Test
    public void thr0wThrowsCheckedExceptionAsIsWithoutDeclaration() {
        TestCheckedException testException = new TestCheckedException();

        try {
            UncheckExceptions.thr0w(testException);
        } catch (Throwable expected) { // Loooool, compiler doesn't let us declare expected exception as TestCheckedException because it doesn't see it in method description xD
            assertSame(testException, expected);
        }
    }
}
