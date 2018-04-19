package com.artemzin.uncheckexceptions.codegen

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_FRAMES
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.util.CheckClassAdapter
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.jar.Attributes.Name.*
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

/**
 * Throwing checked exceptions & {@code Throwable} without declaring {@code throws Exception} or wrapping them in a
 * {@code RuntimeException} is not possible in Java source code, Java compiler declines attempts to compile such code.
 *
 * However, it's perfectly legal to throw any extender of {@code Throwable} from Java bytecode POV.
 * See https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.athrow.
 *
 * Since it's not possible to get Java compiler to generate such bytecode, we need to generate bytecode ourselves…
 */
fun main(args: Array<String>) {
    val UncheckExceptionsClass = generateUncheckExceptionsClass()
    val jar = generateJar(UncheckExceptionsClass to "com/artemzin/uncheckexceptions/UncheckExceptions.class")

    File("uncheck-exceptions.jar").writeBytes(jar)
}

fun generateUncheckExceptionsClass(): ByteArray {
    val classWriter = ClassWriter(COMPUTE_FRAMES)
    val classVisitor = CheckClassAdapter(classWriter)

    classVisitor
            .visit(
                    V1_6,
                    ACC_PUBLIC + ACC_FINAL,
                    "com/artemzin/uncheckexceptions/UncheckExceptions",
                    null,
                    "java/lang/Object",
                    null
            )

    classVisitor
            .visitMethod(
                    ACC_PUBLIC + ACC_STATIC,
                    "thr0w",
                    "(Ljava/lang/Throwable;)V",
                    null,
                    null
            )
            .apply {
                // Consider adding @NotNull annotation, but JSR305 is screwed so it's unclear for now.
                visitCode()

                visitVarInsn(ALOAD, 0)
                visitInsn(ATHROW)

                visitMaxs(1, 1)
            }
            .visitEnd()

    classVisitor.visitEnd()

    return classWriter.toByteArray()
}

fun generateJar(generatedClass: Pair<ByteArray, String>): ByteArray {
    val manifest = Manifest().apply {
        mainAttributes[MANIFEST_VERSION] = "1.0"
    }

    val outputStream = ByteArrayOutputStream()
    val jarOutputStream = JarOutputStream(outputStream, manifest)

    val jarEntry = JarEntry(generatedClass.second)
    jarEntry.time = System.currentTimeMillis()

    jarOutputStream.putNextEntry(jarEntry)

    jarOutputStream.write(generatedClass.first)
    jarOutputStream.closeEntry()
    jarOutputStream.close()

    return outputStream.toByteArray()
}
