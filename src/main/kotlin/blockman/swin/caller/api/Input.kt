package blockman.swin.caller.api

import blockman.swin.caller.Caller
import blockman.swin.caller.constants.Consts
import blockman.swin.caller.constants.Consts.Companion.WM_LBUTTONUP
import blockman.swin.caller.constants.Consts.Companion.WM_UNICHAR
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.WM_CHAR
import org.graalvm.polyglot.HostAccess

class Input {

    @HostAccess.Export
    fun msgMouse(hwnd: Long, x: Int, y: Int, action: Int): Long {
        Thread.sleep(1000)
        val pos = WinDef.LPARAM(x.toLong() + (y.toLong() shl 16))

        val lresult = Caller.u32.SendMessage(
            WinDef.HWND(Pointer(hwnd)),
            action, WinDef.WPARAM(0), pos
        )
        return lresult.toLong()
    }

    @HostAccess.Export
    fun msgClick(hwnd: Long, x: Int, y: Int): Long {
        val r1 = msgMouse(hwnd, x, y, Consts.WM_LBUTTONDOWN)
        Thread.sleep(100)
        val r2 = msgMouse(hwnd, x, y, Consts.WM_LBUTTONUP)
        return 1
    }

    @HostAccess.Export
    fun msgChar(hwnd: Long, charCode: String, byUnicode: Boolean): Long {
        //写一个字符串转换，将Home转Home键码
        val ret = Caller.u32.SendMessage(
            WinDef.HWND(Pointer(hwnd)), if (byUnicode) WM_UNICHAR else WM_CHAR,
            WinDef.WPARAM(charCode[0].toLong()), WinDef.LPARAM(0)
        )
        return ret.toLong()
    }


}