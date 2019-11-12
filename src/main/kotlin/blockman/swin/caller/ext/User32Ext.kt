package blockman.swin.caller.ext

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.*


interface User32Ext : User32 {

    fun WindowFromPoint(point: WinDef.POINT): WinDef.HWND

    fun MapVirtualKeyW(
        uCode: Int,
        uMapType: Int
    ): Long

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
        lpString: WTypes.LPWSTR,
        nMaxCount: Int
    ): Int

    fun GetWindowModuleFileNameW(
        hwnd: WinDef.HWND,
        pszFileName: WTypes.LPWSTR,
        cchFileNameMax: WinDef.UINT
    ): WinDef.UINT

    fun keybd_event(bVk: Byte, bScan: Byte, dwFlags: Int, dwExtraInfo: Int)
}
