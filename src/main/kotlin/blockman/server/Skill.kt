package blockman.server

import blockman.swin.caller.Caller
import com.sun.jna.platform.win32.User32Util
import java.lang.reflect.Method
import kotlin.concurrent.schedule
import kotlin.jvm.internal.Ref

fun main() {
    val t = System.currentTimeMillis()
    for (i in 1..10000) {
        val a = Caller.u32.FindWindow("456", "456465")
        val b = Caller.u32.FindWindow("A", "A")
        val c = Caller.u32.FindWindow("B", "B")

    }
    print(System.currentTimeMillis() - t)
//var x=Int::class.java
//val t=0
}

class Skill {
    var a: Double? = null
    fun go(num: Int?) {

        a = 456.0
//        val sem=javax.script.ScriptEngineManager()
//        val jse=sem.getEngineByName("js")
//        print(jse.eval("1+1"))
    }

}


