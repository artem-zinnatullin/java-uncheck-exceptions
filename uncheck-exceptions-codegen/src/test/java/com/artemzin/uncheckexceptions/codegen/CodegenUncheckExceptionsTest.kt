package com.artemzin.uncheckexceptions.codegen

import org.junit.Test

import org.junit.Assert.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter
import java.io.StringWriter

class CodegenUncheckExceptionsTest {

    @Test
    fun generateClassDoesNotThrowExceptions() {
        generateUncheckExceptionsClass()
    }

    @Test
    fun generateClassProducesExpectedJavaBytecode() {
        val classReader = ClassReader(generateUncheckExceptionsClass())
        val stringWriter = StringWriter()

        classReader.accept(TraceClassVisitor(PrintWriter(stringWriter)), 0)

        assertEquals(
                "// class version 50.0 (50)\n" +
                        "// access flags 0x11\n" +
                        "public final class com/artemzin/uncheckexceptions/UncheckExceptions {\n" +
                        "\n" +
                        "\n" +
                        "  // access flags 0x9\n" +
                        "  public static thr0w(Ljava/lang/Throwable;)V\n" +
                        "    ALOAD 0\n" +
                        "    ATHROW\n" +
                        "    MAXSTACK = 1\n" +
                        "    MAXLOCALS = 1\n" +
                        "}\n",
                stringWriter.toString()
        )
    }
}
