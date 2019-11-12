package blockman.swin.caller.api

import blockman.swin.caller.Caller
import org.graalvm.polyglot.HostAccess

class Sys {

    @HostAccess.Export
    fun getLastError(): Int {
        return Caller.k32.GetLastError()
    }
}