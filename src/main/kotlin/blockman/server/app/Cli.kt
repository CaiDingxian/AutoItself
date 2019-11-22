package blockman.server.app

import org.luaj.vm2.lib.jse.LuaEngine
import org.springframework.core.io.ClassPathResource

class Cli {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val eng = LuaEngine()
            eng.runLua(ClassPathResource("lua/index.lua").path)
        }
    }
}