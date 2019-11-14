package blockman.native.call

import com.sun.jna.platform.win32.*
import com.sun.jna.platform.win32.WinReg.HKEY




interface Adv32Ext : Advapi32 {
    fun RegDeleteTreeW(hKey: HKEY, name: String): Int
    fun RegSaveKeyExW(hKey: HKEY,lpFile:String,lpSecurityAttributes:WinBase.SECURITY_ATTRIBUTES,flag:Int)
    fun RegFlushKey(hKey: HKEY):Int
}
