package org.luaj.vm2.lib.jse

import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction
import java.lang.reflect.Method

internal class SafeJavaInstance(instance: Any) : JavaInstance(instance) {
    override fun get(key: LuaValue): LuaValue {
        if (jclass == null)
            jclass = JavaClass.forClass(m_instance.javaClass)
        val f = jclass.getField(key)
        if (f != null)
            try {
                return CoerceJavaToLua.coerce(f.get(m_instance))
            } catch (e: Exception) {
                throw LuaError(e)
            }

        val m = jclass.getMethod(key)
        if (m != null) {
            if (m is JavaMethod) {
                if (allow(m.method.declaringClass, m.method)) {
                    return m
                } else {
                    return LuaValue.NIL
                }
            }

        }
        val c = jclass.getInnerClass(key)
        return if (c != null) JavaClass.forClass(c) else super.get(key)
    }


    fun allow(clazz: Class<*>, method: Method): Boolean {
        println()
        //print(clazz.name+"."+method.name)
        print(clazz.name + "." + method.name + " IS NOT ALLOW TO CALL !!!")
        return false
    }

}