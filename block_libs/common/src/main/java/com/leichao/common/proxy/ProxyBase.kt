package com.leichao.common.proxy

open class ProxyBase(className: String) {

    private lateinit var clazz: Class<*>
    private lateinit var instance: Any

    init {
        try {
            clazz = Class.forName(className)
            instance = clazz.getField("INSTANCE").get(clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun invoke(methodName: String, vararg params: Any): Any {
        return try {
            val clazzParams = arrayOfNulls<Class<*>>(params.size)
            params.forEachIndexed { index, any ->
                clazzParams[index] = when {
                    any.javaClass == java.lang.Byte::class.java -> Byte::class.java
                    any.javaClass == java.lang.Character::class.java -> Char::class.java
                    any.javaClass == java.lang.Short::class.java -> Short::class.java
                    any.javaClass == java.lang.Integer::class.java -> Int::class.java
                    any.javaClass == java.lang.Long::class.java -> Long::class.java
                    any.javaClass == java.lang.Float::class.java -> Float::class.java
                    any.javaClass == java.lang.Double::class.java -> Double::class.java
                    any.javaClass == java.lang.Boolean::class.java -> Boolean::class.java
                    else -> any.javaClass
                }
            }
            clazz.getMethod(methodName, *clazzParams).invoke(instance, *params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}