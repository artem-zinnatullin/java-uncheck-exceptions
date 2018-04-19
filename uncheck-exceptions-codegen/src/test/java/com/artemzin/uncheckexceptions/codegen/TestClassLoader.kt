package com.artemzin.uncheckexceptions.codegen

class TestClassLoader : ClassLoader() {
    override fun loadClass(name: String): Class<*> {
        return when (name) {
            "com.artemzin.uncheckexceptions.UncheckExceptions" -> {
                val generatedClass = generateUncheckExceptionsClass()
                defineClass(name, generatedClass, 0, generatedClass.size)
            }

            else -> super.loadClass(name)
        }
    }
}
