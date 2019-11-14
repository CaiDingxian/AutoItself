package blockman.native.api

import blockman.native.Libs.Companion.u32
import blockman.native.constants.Consts
import blockman.native.constants.Consts.Companion.WM_UNICHAR
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.BaseTSD
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.WM_CHAR
import com.sun.jna.platform.win32.WinUser.WM_KEYDOWN
import org.graalvm.polyglot.HostAccess
import java.awt.event.KeyEvent

class Input {

    @HostAccess.Export
    fun fixWinTop(hwnd: Long): Boolean {

        return u32.SetWindowPos(
            WinDef.HWND(Pointer(hwnd)),
            WinDef.HWND(Pointer(Consts.HWND_TOPMOST)),
            0, 0, 0, 0, Consts.SWP_ASYNCWINDOWPOS xor Consts.SWP_NOMOVE xor Consts.SWP_NOSIZE
        )
    }

    @HostAccess.Export
    fun msgMouse(hwnd: Long, x: Int, y: Int, action: Int): Long {
        Thread.sleep(1000)
        val pos = WinDef.LPARAM(x.toLong() + (y.toLong() shl 16))

        val lresult = u32.SendMessage(
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
        val ret = u32.SendMessage(
            WinDef.HWND(Pointer(hwnd)), if (byUnicode) WM_UNICHAR else WM_CHAR,
            WinDef.WPARAM(charCode[0].toLong()), WinDef.LPARAM(0)
        )
        return ret.toLong()
    }

    fun msgKey(hwnd: Long) {
        //u32.SendMessage(WinDef.HWND(Pointer(hwnd)), WM_KEYDOWN)
    }


    @HostAccess.Export
    fun inputScanCode() {
        // Prepare input reference
        val input = WinUser.INPUT()

        input.type = WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD.toLong())
        input.input.setType("ki") // Because setting INPUT_INPUT_KEYBOARD is not enough: https://groups.google.com/d/msg/jna-users/NDBGwC1VZbU/cjYCQ1CjBwAJ
        input.input.ki.wScan = WinDef.WORD(u32.MapVirtualKeyW(KeyEvent.VK_5, WinUser.MAPVK_VK_TO_VSC))
        input.input.ki.time = WinDef.DWORD(0)
        input.input.ki.dwExtraInfo = BaseTSD.ULONG_PTR(0)
        // Press "a"
        input.input.ki.wVk = WinDef.WORD(0) // 0x41
        input.input.ki.dwFlags = WinDef.DWORD(WinUser.KEYBDINPUT.KEYEVENTF_SCANCODE.toLong())  // keydown
        User32.INSTANCE.SendInput(WinDef.DWORD(1), input.toArray(1) as Array<WinUser.INPUT>, input.size())
        User32.INSTANCE.SendInput(WinDef.DWORD(1), input.toArray(1) as Array<WinUser.INPUT>, input.size())

    }

    @HostAccess.Export
    fun inputVK() {

        // Prepare input reference
        val input = WinUser.INPUT()

        input.type = WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD.toLong())
        input.input.setType("ki") // Because setting INPUT_INPUT_KEYBOARD is not enough: https://groups.google.com/d/msg/jna-users/NDBGwC1VZbU/cjYCQ1CjBwAJ
        input.input.ki.wScan = WinDef.WORD(0)
        input.input.ki.time = WinDef.DWORD(0)
        input.input.ki.dwExtraInfo = BaseTSD.ULONG_PTR(0)

        // Press "a"
        input.input.ki.wVk = WinDef.WORD('A'.toLong()) // 0x41
        input.input.ki.dwFlags = WinDef.DWORD(0)  // keydown

        User32.INSTANCE.SendInput(WinDef.DWORD(1), input.toArray(1) as Array<WinUser.INPUT>, input.size())

        // Release "a"
        input.input.ki.wVk = WinDef.WORD('A'.toLong()) // 0x41
        input.input.ki.dwFlags = WinDef.DWORD(2)  // keyup

        User32.INSTANCE.SendInput(WinDef.DWORD(1), input.toArray(1) as Array<WinUser.INPUT>, input.size())

    }


}