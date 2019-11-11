package blockman.server

import com.sun.jna.Native
import com.sun.jna.platform.win32.*
import com.sun.jna.platform.win32.WinBase.CREATE_SUSPENDED
import com.sun.jna.platform.win32.User32
import com.sun.jna.win32.W32APIOptions


fun main() {
    processMgr.create()
}

interface K32Ext : Kernel32 {
    fun ResumeThread(
        hThread: WinNT.HANDLE
    ): WinDef.DWORD
}

object processMgr {

    fun create() {
        val sa = WinBase.SECURITY_ATTRIBUTES()

        sa.bInheritHandle = true
        sa.dwLength = WinDef.DWORD(sa.size().toLong())
        val u32 = User32.INSTANCE
        val k32 = Native.load("kernel32", K32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as K32Ext
//val k32=Kernel32.INSTANCE
        k32.SetLastError(0)
        val sui = WinBase.STARTUPINFO()
        sui.cb = WinDef.DWORD(sui.size().toLong())
        var pi = WinBase.PROCESS_INFORMATION()

        val ret = k32.CreateProcessW(
            "F:/CDX/HeidiSQL.exe",
            null, sa, sa, true, WinDef.DWORD(CREATE_SUSPENDED.toLong()),
            null, null, sui, pi
        )
        print(ret)
        print(k32.GetLastError())

        k32.ResumeThread(pi.readField("hThread") as WinNT.HANDLE)
        val msg = WinUser.MSG()
        Thread {
            while (true) {
                Thread.sleep(200)
                val state = User32.INSTANCE.GetAsyncKeyState(WinUser.VK_CONTROL).toInt()
                if (state and 0x8000 == 0x8000) {
                    System.exit(0)
                }
            }

        }.start()

        while (User32.INSTANCE.GetMessage(msg, null, 0, 0) > 0) {
            Thread.sleep(200)
            User32.INSTANCE.TranslateMessage(msg)
            User32.INSTANCE.DispatchMessage(msg)
        }
    }
}