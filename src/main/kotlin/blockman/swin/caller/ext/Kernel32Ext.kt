package blockman.swin.caller.ext

import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WTypes
import com.sun.jna.platform.win32.WinDef

interface K32Ext : Kernel32 {
    fun GetShortPathNameW(
        lpszLongPath: WTypes.LPWSTR,
        lpszShortPath: WTypes.LPWSTR,
        cchBuffer: WinDef.DWORD
    ): WinDef.DWORD
}
