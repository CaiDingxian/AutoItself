package com.voidgeek.blockman.swinapi

import blockman.swin.caller.Caller
import blockman.swin.caller.Caller.Companion.k32
import blockman.swin.caller.Caller.Companion.u32
import blockman.swin.caller.cmd.Cmd
import blockman.swin.caller.constants.*
import blockman.swin.caller.constants.Consts.Companion.HWND_TOPMOST
import blockman.swin.caller.constants.Consts.Companion.SWP_ASYNCWINDOWPOS
import blockman.swin.caller.constants.Consts.Companion.SWP_NOMOVE
import blockman.swin.caller.constants.Consts.Companion.SWP_NOSIZE
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.*
import com.sun.jna.platform.win32.WinUser.KEYBDINPUT.*
import org.graalvm.polyglot.HostAccess
import java.awt.event.KeyEvent.VK_B
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.BaseTSD
import java.awt.event.KeyEvent

class Act {



    @HostAccess.Export
    fun exec(cmd: String): Int {
        return 1//Cmd.exec(cmd)
    }

    @HostAccess.Export
    fun msgBox(title: String, content: String): Int {
        return u32.MessageBoxW(
            WinDef.HWND(null),
            WTypes.LPWSTR(title),
            WTypes.LPWSTR(content),
            WinDef.UINT(0x00000004L)
        )
    }

    @HostAccess.Export
    fun findWin(className: String?, name: String?): Long? {
        return Pointer.nativeValue(u32.FindWindow(className, name).pointer)
    }

    @HostAccess.Export
    fun fixWinTop(hwnd: Long): Boolean {

        return u32.SetWindowPos(
            WinDef.HWND(Pointer(hwnd)),
            WinDef.HWND(Pointer(HWND_TOPMOST)),
            0, 0, 0, 0, SWP_ASYNCWINDOWPOS xor SWP_NOMOVE xor SWP_NOSIZE
        )
    }

    @HostAccess.Export
    fun sendClick(hwnd: Long, x: Int, y: Int): Long {
        val WM_LBUTTONDOWN = 0x0201 //按下鼠标左键
        val WM_LBUTTONUP = 0x0202 //释放鼠标左键
        Thread.sleep(1000)
        val pos = WinDef.LPARAM(x.toLong() + (y.toLong() shl 16))

        val lresult = u32.SendMessage(
            WinDef.HWND(Pointer(hwnd)),
            WM_LBUTTONDOWN, WinDef.WPARAM(0), pos
        )
        Thread.sleep(100)
        u32.SendMessage(WinDef.HWND(Pointer(hwnd)), WM_LBUTTONUP, WinDef.WPARAM(0), pos)
        return 1
    }


    @HostAccess.Export
    fun setWinContent(hwnd: Long, text: String): Long {
        val WM_SETTEXT = 0x000C
        val peer = Pointer.createConstant(hwnd)
        val hwndObj = WinDef.HWND(peer)
        val p = Pointer.nativeValue(WTypes.LPWSTR(text).pointer)
        val ret = u32.SendMessage(
            hwndObj, WM_SETTEXT,
            WinDef.WPARAM(0), WinDef.LPARAM(p)
        )
        return ret.toLong()
    }

    @HostAccess.Export
    fun getLastError(): Int {
        return Caller.k32.GetLastError()
    }

    @HostAccess.Export
    fun sendCharErr() {

        k32.SetLastError(0)
        //u32.keybd_event(0,0x23.toByte(),KEYEVENTF_SCANCODE,0)
        Thread.sleep(5000)
        // u32.keybd_event(0,0xa3.toByte(),KEYEVENTF_SCANCODE xor KEYEVENTF_KEYUP,0)
        Thread.sleep(100)
        var inputArr = arrayOf(WinUser.INPUT(), WinUser.INPUT())
        inputArr[0].input.ki.wVk = WinDef.WORD(VK_B.toLong())
        inputArr[0].type = WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD.toLong())
        inputArr[0].input.ki.time == WinDef.DWORD(0)

        inputArr[1].input.ki.wVk = WinDef.WORD(VK_B.toLong())
        inputArr[1].type = WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD.toLong())
        inputArr[1].input.ki.dwFlags = WinDef.DWORD(KEYEVENTF_KEYUP.toLong())
        inputArr[1].input.ki.time == WinDef.DWORD(0)
        //print(Kernel32.INSTANCE.GetLastError())

        val r = u32.SendInput(WinDef.DWORD(1), arrayOf(inputArr[0]), inputArr[0].size())
        print(k32.GetLastError())
        Thread.sleep(100)
        val r2 = u32.SendInput(WinDef.DWORD(1), arrayOf(inputArr[1]), inputArr[1].size())
    }


    @HostAccess.Export
    fun inputChar() {

        k32.SetLastError(0)
        //u32.keybd_event(0,0x23.toByte(),KEYEVENTF_SCANCODE,0)
        Thread.sleep(5000)
        // u32.keybd_event(0,0xa3.toByte(),KEYEVENTF_SCANCODE xor KEYEVENTF_KEYUP,0)
        val input = WinUser.INPUT()

        input.type = WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD.toLong())

        input.input.setType("ki") // Because setting INPUT_INPUT_KEYBOARD is not enough: https://groups.google.com/d/msg/jna-users/NDBGwC1VZbU/cjYCQ1CjBwAJ
        input.input.ki.wScan = WinDef.WORD('我'.toLong())
        input.input.ki.time = WinDef.DWORD(0)
        input.input.ki.dwExtraInfo = BaseTSD.ULONG_PTR(0)
        input.input.ki.wVk = WinDef.WORD(0) // 0x41
        input.input.ki.dwFlags = WinDef.DWORD(KEYEVENTF_UNICODE.toLong())  // keydown

        User32.INSTANCE.SendInput(WinDef.DWORD(1), input.toArray(1) as Array<WinUser.INPUT>, input.size())

        input.input.ki.dwFlags = WinDef.DWORD(KEYEVENTF_UNICODE.toLong())  // keyup

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
        input.input.ki.dwFlags = WinDef.DWORD(KEYEVENTF_SCANCODE.toLong())  // keydown

        User32.INSTANCE.SendInput(WinDef.DWORD(1), input.toArray(1) as Array<WinUser.INPUT>, input.size())
        User32.INSTANCE.SendInput(WinDef.DWORD(1), input.toArray(1) as Array<WinUser.INPUT>, input.size())

    }


}


fun main() {
    val dc = u32.GetDC(WinDef.HWND(Pointer(0)))

    //Act().inputScanCode()
    //print(-1 shl 3)
//    Cmd.exec("Notepad")
//
//    val x=0
    //setWinContent(0x00010BB8,"D://")
}
