package blockman.server

import blockman.swin.caller.Caller
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.win32.W32APIOptions
import com.voidgeek.blockman.swinapi.Act
import kotlin.random.Random

fun main() {
    var h = Caller.u32.WindowFromPoint(WinDef.POINT(5, 1080))
    while (h == null) {
        h = Caller.u32.WindowFromPoint(WinDef.POINT(Random.nextInt(0, 1000), Random.nextInt(0, 1000)))
    }

    var className = CharArray(100)
    Caller.u32.GetClassName(h, className, 100)
    print(String(className).trim() + " ")

    var winText = CharArray(100)
    Caller.u32.GetWindowText(h, winText, 100)
    print(String(winText).trim() + " ")
//WinAnalyzer().analyze()
}

interface U32Ext : User32 {
    fun GetParent(
        hWnd: WinDef.HWND
    ): WinDef.HWND

    fun GetFocus(

    ): WinDef.HWND
}

class WinAnalyzer {
    fun analyze() {
        Thread.sleep(2000)
        val u32 = Native.load("user32", U32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as U32Ext
        //val chwnd=u32.GetForegroundWindow()

        while (true) {
            Thread.sleep(1000)
            val guiInfo = WinUser.GUITHREADINFO()
            u32.GetGUIThreadInfo(0, guiInfo)
            val hwnd = guiInfo.hwndActive

            var gh: WinDef.HWND? = hwnd
            val path = mutableListOf<WinDef.HWND>()
            while (gh != null) {
                path.add(gh)
                gh = u32.GetAncestor(gh, 1)

            }

            path.forEach {
                var className = CharArray(100)
                u32.GetClassName(it, className, 100)
                print(String(className).trim() + " ")

                var winText = CharArray(100)
                u32.GetWindowText(it, winText, 100)
                print(String(winText).trim() + " ")
            }
            println("-------------")


            //u32.GetClassName(hwnd,charArray,100)
            //u32.GetActiveWindow()
            //print(String(charArray))
        }
    }
}