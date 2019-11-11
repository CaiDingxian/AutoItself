package com.voidgeek.tms.app

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HMODULE
import com.sun.jna.platform.win32.WinDef.LRESULT
import com.sun.jna.platform.win32.WinDef.WPARAM
import com.sun.jna.platform.win32.WinDef.LPARAM
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.HHOOK
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc
import com.sun.jna.platform.win32.WinUser.MSG

/** Sample implementation of a low-level keyboard hook on W32.  */
object KeyHook {
    @Volatile
    private var quit: Boolean = false
    private var hhk: HHOOK? = null
    private var keyboardHook: LowLevelKeyboardProc? = null

    @JvmStatic
    fun main(args: Array<String>) {
        val lib = User32.INSTANCE
        val hMod = Kernel32.INSTANCE.GetModuleHandle(null)
        keyboardHook = LowLevelKeyboardProc { nCode, wParam, info ->
            if (nCode >= 0) {
                when (wParam.toInt()) {
                    WinUser.WM_KEYUP, WinUser.WM_KEYDOWN, WinUser.WM_SYSKEYUP, WinUser.WM_SYSKEYDOWN -> {
                        System.err.println("in callback, key=" + info.vkCode)
                        if (info.vkCode == 81) {
                            quit = true
                        }
                    }
                }
            }

            val ptr = info.pointer
            val peer = Pointer.nativeValue(ptr)
            lib.CallNextHookEx(hhk, nCode, wParam, LPARAM(peer))
        }
        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0)
        println("Keyboard hook installed, type anywhere, 'q' to quit")
        object : Thread() {
            override fun run() {
                while (!quit) {
                    try {
                        Thread.sleep(10)
                    } catch (e: Exception) {
                    }

                }
                System.err.println("unhook and exit")
                lib.UnhookWindowsHookEx(hhk)
                System.exit(0)
            }
        }.start()

        // This bit never returns from GetMessage
        var result: Int
        val msg = MSG()
        result = lib.GetMessage(msg, null, 0, 0)
        while (result != 0) {
            if (result == -1) {
                System.err.println("error in get message")
                break
            } else {
                System.err.println("got message")
                lib.TranslateMessage(msg)
                lib.DispatchMessage(msg)
            }
        }
        lib.UnhookWindowsHookEx(hhk)
    }
}