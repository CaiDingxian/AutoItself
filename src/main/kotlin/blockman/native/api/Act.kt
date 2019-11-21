package com.voidgeek.blockman.swinapi

import blockman.native.Libs.Companion.k32
import blockman.native.Libs.Companion.u32
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinUser.KEYBDINPUT.*
import org.graalvm.polyglot.HostAccess
import java.awt.event.KeyEvent.VK_B
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.BaseTSD

class Act {


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
