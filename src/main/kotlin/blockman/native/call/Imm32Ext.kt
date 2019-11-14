package blockman.native.call

import com.sun.jna.platform.win32.WTypes
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.win32.StdCallLibrary

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