package blockman.server.lua

import com.voidgeek.blockman.swinapi.Act
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseIoLib
import org.luaj.vm2.lib.jse.JseMathLib
import java.lang.reflect.Method

class Binder {

    var target: Any? = null

    val funsCache = HashMap<String, Method>()

    fun bind(target: Any, globals: Globals) {
        this.target = target
        val methods = target.javaClass.declaredMethods
        for (m in methods) {
            funsCache.put(m.name + "#" + m.parameterTypes.size, m)
        }

        val luaMethod = object : VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                val key = this.name + args.narg()
                val method = funsCache[key]

                //method.invoke(target,)

                return LuaValue.valueOf("OK")
            }
        }
        val x = 0;
    }
}

fun main() {
    val globals = Globals()
    globals.load(JseBaseLib())
    globals.load(PackageLib())
    globals.load(Bit32Lib())
    globals.load(TableLib())
    globals.load(StringLib())
    //globals.load(CoroutineLib())
    globals.load(JseMathLib())
    globals.load(JseIoLib())
    //globals.load(JseOsLib())
    //globals.load(LuajavaLib())
    LoadState.install(globals)
    LuaC.install(globals)
    //globals.load(DebugLib())
    //val sethook = globals.get("debug").get("sethook")
    //globals.set("debug", LuaValue.NIL)
    val chunk = globals.load(
        """
            function go()
                return x()
            end
            -- print(a)
            return go()
        """.trimIndent()
    )



    Binder().bind(Act(), globals)
}