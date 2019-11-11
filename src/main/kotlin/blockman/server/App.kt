package com.voidgeek.tms.app

import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.WH_CALLWNDPROC
import com.sun.jna.platform.win32.WinUser.WH_KEYBOARD_LL

import java.io.IOException

open interface WinProc : WinUser.HOOKPROC {
    open fun callback(nCode: Int, wParam: WinDef.WPARAM?, lParam: WinDef.LPARAM?): WinDef.LRESULT
}

@Structure.FieldOrder("lResult", "lParam", "wParam", "message", "hwnd")
open class CWPRETSTRUCT : Structure() {
    var lResult: WinDef.LRESULT? = null
    var lParam: WinDef.LPARAM? = null
    var wParam: WinDef.WPARAM? = null
    var message: WinDef.UINT? = null
    var hwnd: WinDef.HWND? = null
}

object App {
    @Volatile
    private var quit: Boolean = false
    private var hhk: WinUser.HHOOK? = null
    private var winHook: WinUser.HOOKPROC? = null

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val lib = User32.INSTANCE
        val hMod = Kernel32.INSTANCE.GetModuleHandle("Kernel32.dll")

        winHook = object : WinProc {
            override fun callback(nCode: Int, wParam: WinDef.WPARAM?, lParam: WinDef.LPARAM?): WinDef.LRESULT {
                if (nCode >= 0) {
                    when (wParam?.toInt()) {
                        WinUser.WM_CREATE, WinUser.WM_CHAR, WinUser.WM_CLOSE -> {

                        }
                    }
                }
                print("Hit")
//                val ptr = lParam?.pointer
//                val peer = Pointer.nativeValue(ptr)
                return lib.CallNextHookEx(hhk, nCode, wParam, lParam)
            }
        }

        hhk = lib.SetWindowsHookEx(5, winHook, hMod, 0)
        println("win hook installed")
        print(Kernel32.INSTANCE.GetLastError())
        object : Thread() {
            var i = 0
            override fun run() {
                while (true) {
                    i++


                    Thread.sleep(100)
                    if (i > 200) {
                        lib.UnhookWindowsHookEx(hhk)
                        System.exit(0)
                    }
                }
            }
        }.start()

        print("runing")
        var result: Int = 1
        val msg = WinUser.MSG()

        while (result != 0) {
            result = lib.GetMessage(msg, null, 0, 0)
            if (result == -1) {
                print("error in get message")
                break
            } else {
                print("got message")
                lib.TranslateMessage(msg)
                lib.DispatchMessage(msg)
            }
        }

        //        User32.INSTANCE.FindWindow(null,"old");
        //        System.out.println("OK");
    }
}
