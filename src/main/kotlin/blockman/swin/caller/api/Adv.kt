package blockman.swin.caller.api

import blockman.swin.caller.Caller.Companion.adv32
import blockman.swin.caller.Caller.Companion.k32
import blockman.swin.caller.Caller.Companion.u32
import com.sun.jna.WString
import com.sun.jna.platform.win32.Advapi32.RRF_RT_ANY
import com.sun.jna.platform.win32.Advapi32.RRF_RT_REG_SZ
import com.sun.jna.platform.win32.WinNT.*
import com.sun.jna.platform.win32.WinReg
import com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER
import com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE
import com.sun.jna.ptr.IntByReference

class Adv {
    fun addKey() {

        k32.SetLastError(0)

        val hkey = WinReg.HKEY()
        val hKeyRef = WinReg.HKEYByReference(hkey)
        val ret = adv32.RegOpenKeyEx(
            HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control\\Keyboard Layouts", 0,
            KEY_READ xor KEY_WRITE xor KEY_WOW64_64KEY, hKeyRef
        )
        print(k32.GetLastError())


        val keys = ArrayList<String>()
        var enumRet = 0
        var i = 0
        while (enumRet != ERROR_NO_MORE_ITEMS) {
            val lpcName = IntByReference(100)
            var name = CharArray(1000)
            enumRet = adv32.RegEnumKeyEx(
                hKeyRef.value, i, name, lpcName,
                null, null, null, null
            )
            i++
            keys.add(String(name).replace("\u0000", ""))
        }

        for (regKey in keys) {
            val sHKeyRef = WinReg.HKEYByReference()
            val sRet = adv32.RegOpenKeyEx(
                HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control\\Keyboard Layouts", 0,
                KEY_READ xor KEY_WRITE xor KEY_WOW64_64KEY, sHKeyRef
            )
            print(k32.GetLastError())
            val valueType = IntByReference()
            val value = ByteArray(100)
            val valueSize = IntByReference(50)
            val r5 = adv32.RegGetValue(sHKeyRef.value, regKey, "Layout Text", RRF_RT_ANY, valueType, value, valueSize)
            val str = String(value)
            adv32.RegCloseKey(sHKeyRef.value)
        }
        //CTF输入法注册表项需要使用链接，链接到真实目录中去删除真正的输入法

        print(k32.GetLastError())
        adv32.RegCloseKey(hKeyRef.value)

        val x = 0
    }
}

fun main() {
    Adv().addKey()
}