package blockman.server

import blockman.native.Libs.Companion.u32


fun main() {
    val t = System.currentTimeMillis()
    for (i in 1..10000) {
        val a = u32.FindWindow("456", "456465")
        val b = u32.FindWindow("A", "A")
        val c = u32.FindWindow("B", "B")

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


