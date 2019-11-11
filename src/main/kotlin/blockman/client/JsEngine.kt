package blockman.client

import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.PolyglotAccess
import java.io.Serializable
import java.util.function.Predicate
import javax.script.*
import javax.script.ScriptContext
import javax.script.Bindings
import javax.script.ScriptEngineManager
import javax.script.ScriptEngine
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine


class X : Serializable {
    var a = 0
    var b = 50

    @HostAccess.Export
    fun x(): Int {
        print("OK")
        return 1
    }

    override fun toString(): String {
        return "XOK"
    }
}

fun main() {

//
//    val engine = ScriptEngineManager().getEngineByName("graal.js")
//    val bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE)
    val engine = GraalJSScriptEngine.create(null,
        Context.newBuilder("js")
            .allowHostAccess(HostAccess.newBuilder(HostAccess.EXPLICIT).build())
            .allowHostClassLookup { s -> false })
//    bindings["polyglot.js.allowHostAccess"] = true
//    bindings["polyglot.js.allowHostClassLookup"] = Predicate<String>{
//        val x=it
//        false
//    }
    engine.put("javaObj", X())
    val r = engine.eval("(javaObj.x())")
    val x = 0
}