package blockman.server.js

import com.oracle.truffle.js.scriptengine.GraalJSEngineFactory
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.PolyglotAccess
import java.io.Serializable
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine
import com.voidgeek.blockman.swinapi.Act
import org.springframework.util.StringUtils
import java.io.File
import java.io.FileReader
import java.util.function.Predicate
import javax.script.ScriptContext
import javax.script.ScriptContext.GLOBAL_SCOPE
import javax.script.ScriptEngineManager


class JsEngine {
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

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
//
//    val eng = ScriptEngineManager().getEngineByName("graal.js")


            val engine = GraalJSScriptEngine.create(null,
                Context.newBuilder("js")
                    .option("js.script-engine-global-scope-import", "true")
                    .allowAllAccess(true)
                    .allowHostAccess(HostAccess.newBuilder(HostAccess.EXPLICIT).build())
                    .allowHostClassLookup { s -> true })

//            var bindings = engine.createBindings()
//            bindings["polyglot.js.allowHostAccess"] = true
//            bindings["polyglot.js.allowHostClassLookup"] = Predicate<String> {
//                true
//            }
            //bindings["js.script-engine-global-scope-import"]=true

//        val x=it
//        false
//    }

            engine.put("act", Act())
            //engine.setBindings(bindings,GLOBAL_SCOPE)
            val js = FileReader(File("D:\\DEV\\TestModSys\\run.js"))
            val jsStr = js.readText()
            js.close()
            val r = engine.eval(jsStr)
            val b = engine.context.getBindings(ScriptContext.ENGINE_SCOPE).entries
            val x = 0
        }
    }
}

