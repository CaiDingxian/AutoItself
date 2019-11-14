package blockman.native.api

import blockman.native.Libs.Companion.k32
import org.graalvm.polyglot.HostAccess

class Sys {

    @HostAccess.Export
    fun getLastError(): Int {
        return k32.GetLastError()
    }
}