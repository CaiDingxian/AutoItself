package blockman.server

import com.sun.jna.Pointer
import com.sun.jna.platform.DesktopWindow
import com.sun.jna.platform.win32.*
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinUser.*
import java.lang.Exception

class EHookProc : WinEventProc {
    override fun callback(
        hWinEventHook: WinNT.HANDLE?,
        event: DWORD?,
        hwnd: HWND?,
        idObject: LONG?,
        idChild: LONG?,
        dwEventThread: DWORD?,
        dwmsEventTime: DWORD?
    ) {
        val arr = CharArray(100)
        User32.INSTANCE.GetWindowText(hwnd, arr, 100)
        println(String(arr))
    }
}


class Hookito {
    /**
     * Callback method. cf :
     * https://msdn.microsoft.com/en-us/library/windows/desktop/ms644975(v=vs.85).aspx
     *
     *
     * nCode [in] : Specifies whether the hook procedure must process
     * the message. If nCode is HC_ACTION, the hook procedure must
     * process the message. If nCode is less than zero, the hook
     * procedure must pass the message to the CallNextHookEx function
     * without further processing and must return the value returned by
     * CallNextHookEx. wParam [in] : Specifies whether the message was
     * sent by the current thread. If the message was sent by the
     * current thread, it is nonzero; otherwise, it is zero. lParam [in]
     * : A pointer to a CWPSTRUCT structure that contains details about
     * the message.
     *
     *
     */
    // used by introspection from jna.


    fun hookwinProc(): WinNT.HANDLE? {
        val hwndToHook = User32.INSTANCE.FindWindow(null, "DEV")
        val threadtoHook = User32.INSTANCE.GetWindowThreadProcessId(hwndToHook, null)
        // Hook of the wndProc
        val EVENT_OBJECT_CODE = 0x8000
        return User32.INSTANCE.SetWinEventHook(
            EVENT_OBJECT_CODE, EVENT_OBJECT_CODE,
            hInst,
            EHookProc(), 0, 0, 0x0000
        )
    }

    companion object {
        var hInst: HMODULE? = null
        @JvmStatic
        fun main(args: Array<String>) {
            hInst = Kernel32.INSTANCE.GetModuleHandle(null)
            var hookito = Hookito()
            var hk = hookito!!.hookwinProc()
            val msg = MSG()
            Thread {
                while (true) {
                    Thread.sleep(200)
                    val state = User32.INSTANCE.GetAsyncKeyState(VK_CONTROL).toInt()
                    if (state and 0x8000 == 0x8000) {
                        User32.INSTANCE.UnhookWinEvent(hk)
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


}