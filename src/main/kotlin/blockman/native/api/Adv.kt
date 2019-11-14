package blockman.native.api

import blockman.native.Libs.Companion.adv32
import blockman.native.Libs.Companion.k32
import com.sun.jna.platform.win32.Advapi32.RRF_RT_ANY
import com.sun.jna.platform.win32.WinNT.*
import com.sun.jna.platform.win32.WinReg
import com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE
import com.sun.jna.ptr.IntByReference

class Adv {
    fun addKey() {

        k32.SetLastError(0)

        val hKeyRef = WinReg.HKEYByReference()
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
            val className = CharArray(1000)
            val classNameSize = IntByReference(100)
            enumRet = adv32.RegEnumKeyEx(
                hKeyRef.value, i, name, lpcName,
                null, className, classNameSize, null
            )
            i++
            keys.add(String(name).replace("\u0000", ""))
        }
        adv32.RegCloseKey(hKeyRef.value)

        val sHKeyRef = WinReg.HKEYByReference()
        val sRet = adv32.RegOpenKeyEx(
            HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control", 0,
            KEY_READ xor KEY_WRITE xor KEY_WOW64_64KEY, sHKeyRef
        )
        for (regKey in keys) {
            print(k32.GetLastError())
            val valueType = IntByReference()
            val value = ByteArray(100)
            val valueSize = IntByReference(50)
            val r5 = adv32.RegGetValue(sHKeyRef.value, "Keyboard Layouts\\"+regKey, "Layout Text", RRF_RT_ANY, valueType, value, valueSize)
            val str = String(value)

        }
        adv32.RegCloseKey(sHKeyRef.value)
        //CTF输入法注册表项需要使用链接，链接到真实目录中去删除真正的输入法
        //备份注册表：reg export HKEY_CURRENT_USER\Console C:\1.reg
        print(k32.GetLastError())

        //adv32.RegSetValueEx()

        val x = 0
    }
}

fun main() {
    Adv().addKey()
}