package blockman.server.ime

import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.WString
import com.sun.jna.platform.win32.*
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions
import java.nio.CharBuffer
import java.nio.Buffer
import com.sun.jna.platform.win32.WTypes.LPWSTR

interface K32Ext : Kernel32 {
    fun GetShortPathNameW(
        lpszLongPath: LPWSTR,
        lpszShortPath: LPWSTR,
        cchBuffer: WinDef.DWORD
    ): WinDef.DWORD
}


interface U32Ext : User32 {
    fun GetParent(
        hWnd: WinDef.HWND
    ): WinDef.HWND

    fun GetFocus(
    ): WinDef.HWND

    fun UnloadKeyboardLayout(
        hkl: WinDef.HKL
    ): WinDef.BOOL

    fun MessageBoxW(
        hWnd: WinDef.HWND,
        lpText: WTypes.LPWSTR,
        lpCaption: WTypes.LPWSTR,
        uType: WinDef.UINT
    ): Int

    fun GetWindowTextW(
        hWnd: WinDef.HWND,
        lpString: LPWSTR,
        nMaxCount: Int
    ): Int

    fun GetWindowModuleFileNameW(
        hwnd: WinDef.HWND,
        pszFileName: LPWSTR,
        cchFileNameMax: WinDef.UINT
    ): WinDef.UINT
}

interface Imm32Ext : StdCallLibrary, WinNT {
    fun ImmGetIMEFileNameW(
        hkl: WinDef.HKL,
        lpszFileName: CharArray,
        uBufLen: WinDef.UINT
    ): WinDef.UINT

    fun ImmGetDescriptionW(
        hkl: WinDef.HKL,
        lpszDescription: WTypes.LPWSTR,
        uBufLen: WinDef.UINT
    ): WinDef.UINT

}

fun main() {
    SIMM().listLayout()
}

class SIMM {
    fun listLayout() {

        val u32 = Native.load("user32", U32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as U32Ext
        val imm32 = Native.load("imm32.dll", Imm32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as Imm32Ext
        val k32 = Native.load("kernel32", K32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as K32Ext
        val hklArr = Array<WinDef.HKL?>(10, { null })
        u32.GetKeyboardLayoutList(10, hklArr)

        hklArr.map {

            var strArr = CharArray(100)
//           val str=Memory(16*50)
            if (it != null) {
                u32.GetKeyboardLayoutName(strArr)
                val m = Memory(16 * 100)
                //m.clear()

                val p = WTypes.LPWSTR(m)

                //p.value=""
                val fName = CharArray(100)

                //val len=u32.GetWindowModuleFileName(u32.FindWindow(null,"微信"),fName, 100)
                // Kernel32.INSTANCE. ("E:\\迅雷下载\\学校网站简单\\使用教程.html",fName,100)
                val len = imm32.ImmGetDescriptionW(it, p, WinDef.UINT(50))

                //c[len.toInt()-1]= 0.toChar()

                //u32.GetWindowTextW(u32.FindWindow(null,"下载"),p,50)
                //u32.MessageBoxW(WinDef.HWND(null),p,p,WinDef.UINT(0))
                //k32.GetShortPathNameW(LPWSTR("E:\\迅雷下载\\学校网站简单\\使用教程.html"),p, WinDef.DWORD(50))
                val c = p.pointer.getCharArray(0, 100)
                println(Native.toString(c))

                //val dstr= dstrArr.getString(0,"UTF16")
                val str = String(strArr).substringBefore(0.toChar())
                if (str == "00000409") {
                    //u32.UnloadKeyboardLayout(it)E0210804
                }
                println(String(strArr))
            }
        }
//        val keybdLayoutNames=keybdLayoutNameIds.map {
//
//        }

    }
}