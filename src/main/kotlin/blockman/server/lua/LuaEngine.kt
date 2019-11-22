package org.luaj.vm2.lib.jse

import blockman.native.api.Adv
import blockman.native.api.Ime
import blockman.native.api.Input
import blockman.native.api.Win
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.Varargs
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import java.io.File

val globals = Globals()
fun main() {
    val e = LuaEngine()
    e.runLua("")
    System.gc()
    val a = 0
}

class LuaEngine {
    fun runLua(scriptPath: String) {
        //val globals = JsePlatform.standardGlobals()

        globals.load(JseBaseLib())
        globals.load(PackageLib())
        globals.load(Bit32Lib())
        globals.load(TableLib())
        globals.load(StringLib())
        //globals.load(CoroutineLib())
        globals.load(JseMathLib())
        globals.load(JseIoLib())
        globals.load(JseOsLib())
        val l = LuajavaLib()
        globals.load(l)
        globals.load(DebugLib())
        LoadState.install(globals)
        LuaC.install(globals)


        //globals.load(DebugLib())
        //val sethook = globals.get("debug").get("sethook")
        //globals.set("debug", LuaValue.NIL)
        val chunk = globals.loadfile(scriptPath)

        val s = object : VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                var a = args
                //args.arg(1)
                return LuaValue.valueOf("OK")
            }
        }
        val any = Any()

        Object().getClass()
        //如果设置x=?,将会切断x与LuaValue的连接，GC可以回收对象
        globals.set("win", SafeJavaInstance(Win()))
        globals.set("input", SafeJavaInstance(Input()))
        globals.set("adv", SafeJavaInstance(Adv()))
        globals.set("ime", SafeJavaInstance(Ime()))
        val a = chunk.invoke()
        //chunk.call()
        val b = 3
    }
}