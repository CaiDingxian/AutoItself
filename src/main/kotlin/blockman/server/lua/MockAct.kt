package blockman.server.lua

import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction

class MockAct {
    fun jaf(str: String, number: Long): Boolean {
        return true
    }


}

fun main() {
    val luaMethod = object : VarArgFunction() {
        override fun invoke(args: Varargs): LuaValue {
            val s = args as LuaClosure

            return LuaValue.valueOf("OK")
        }
    }


}