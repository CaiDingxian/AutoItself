package blockman.server.lua

import org.luaj.vm2.LuaValue
import java.lang.reflect.Type

class TypeMapping {

    val javaToLua = HashMap<Class<*>, LuaValue>()

    val luaToJava = HashMap<LuaValue, Class<*>>()

    fun map() {

    }
}