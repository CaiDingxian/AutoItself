package blockman.swin.caller.api

import blockman.swin.caller.Caller
import com.sun.jna.platform.win32.Advapi32
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinReg
import com.sun.jna.ptr.IntByReference

class Reg {

    fun listKeys(hKeyNum: Int): List<String> {
        val keyList = ArrayList<String>()
        var enumRet = 0
        var i = 0
        val hKey = WinReg.HKEY(hKeyNum)
        while (enumRet == 0) {
            val lpcName = IntByReference(100)
            var name = CharArray(200)
            enumRet = Caller.adv32.RegEnumKeyEx(
                hKey, i++, name, lpcName,
                null, null, null, null
            )
            keyList.add(String(name).replace("\u0000", ""))
        }
        return keyList.toList()
    }

    fun openKey() {

    }


    fun addKey() {

        Caller.k32.SetLastError(0)

        val hKeyRef = WinReg.HKEYByReference()
        val ret = Caller.adv32.RegOpenKeyEx(
            WinReg.HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control\\Keyboard Layouts", 0,
            WinNT.KEY_READ xor WinNT.KEY_WRITE xor WinNT.KEY_WOW64_64KEY, hKeyRef
        )
        print(Caller.k32.GetLastError())


        val keys = ArrayList<String>()
        var enumRet = 0
        var i = 0
        while (enumRet != WinNT.ERROR_NO_MORE_ITEMS) {
            val lpcName = IntByReference(100)
            var name = CharArray(1000)
            enumRet = Caller.adv32.RegEnumKeyEx(
                hKeyRef.value, i, name, lpcName,
                null, null, null, null
            )
            i++
            keys.add(String(name).replace("\u0000", ""))
        }
        Caller.adv32.RegCloseKey(hKeyRef.value)

        val sHKeyRef = WinReg.HKEYByReference()
        val sRet = Caller.adv32.RegOpenKeyEx(
            WinReg.HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control", 0,
            WinNT.KEY_READ xor WinNT.KEY_WRITE xor WinNT.KEY_WOW64_64KEY, sHKeyRef
        )
        for (regKey in keys) {
            print(Caller.k32.GetLastError())
            val valueType = IntByReference()
            val value = ByteArray(100)
            val valueSize = IntByReference(50)
            val r5 = Caller.adv32.RegGetValue(
                sHKeyRef.value, "Keyboard Layouts\\" + regKey, "Layout Text",
                Advapi32.RRF_RT_ANY, valueType, value, valueSize
            )
            val str = String(value)

        }
        Caller.adv32.RegCloseKey(sHKeyRef.value)
        //CTF输入法注册表项需要使用链接，链接到真实目录中去删除真正的输入法
        //备份注册表：reg export HKEY_CURRENT_USER\Console C:\1.reg
        print(Caller.k32.GetLastError())

        //adv32.RegSetValueEx()

        val x = 0
    }
}