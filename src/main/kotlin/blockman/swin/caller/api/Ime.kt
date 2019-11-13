package blockman.swin.caller.api

import blockman.swin.caller.Caller
import blockman.swin.caller.Caller.Companion.adv32
import com.sun.jna.platform.win32.Advapi32
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinReg
import com.sun.jna.ptr.IntByReference
import java.util.function.Predicate

class Ime {

    fun imeDeleteByName(str: String) {

    }

    fun rebuildPreload() {
        val preLoadHKeyRef = WinReg.HKEYByReference()
        val preLoadRet = adv32.RegOpenKeyEx(
            WinReg.HKEY_CURRENT_USER, "HKEY_CURRENT_USER\\Keyboard Layout\\Preload", 0,
            WinNT.KEY_READ xor WinNT.KEY_WRITE xor WinNT.KEY_WOW64_64KEY, preLoadHKeyRef
        )
        val tempPreLoadValue = CharArray(100)
        var i = 0
        var plRet = 0
        val plValueList = ArrayList<String>()
        while (plRet == 0) {
            val plValueType = IntByReference()
            val plValue = ByteArray(100)
            val plValueSize = IntByReference(50)
            plRet = adv32.RegEnumValue(
                preLoadHKeyRef.value,
                i++,
                tempPreLoadValue,
                IntByReference(40),
                IntByReference(0),
                plValueType,
                plValue,
                plValueSize
            )
            val plNameStr = String(tempPreLoadValue).replace("\u0000", "")
            plValueList.add(plNameStr)
            adv32.RegDeleteValue(preLoadHKeyRef.value, plNameStr)
        }
        adv32.RegCloseKey(preLoadHKeyRef.value)
    }


    fun imeDeleteByCond(cond: Predicate<Array<String>>) {
        val hKeyRef = WinReg.HKEYByReference()
        val sRet = adv32.RegOpenKeyEx(
            WinReg.HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control\\Keyboard Layouts", 0,
            WinNT.KEY_READ xor WinNT.KEY_WRITE xor WinNT.KEY_WOW64_64KEY, hKeyRef
        )

        var enumRet = 0
        var i = 0
        var ret = 0
        while (enumRet == 0) {
            val lpcName = IntByReference(140)
            val name = CharArray(300)
            enumRet = adv32.RegEnumKeyEx(
                hKeyRef.value, i, name, lpcName,
                null, null, null, null
            )
            i++
            val valueType = IntByReference()
            val value = ByteArray(100)
            val valueSize = IntByReference(50)
            val keyNameStr = String(name).replace("\u0000", "")
            ret = adv32.RegGetValue(
                hKeyRef.value,
                keyNameStr,
                "Layout Text",
                Advapi32.RRF_RT_ANY,
                valueType,
                value,
                valueSize
            )
            val valueStr = String(value).replace("\u0000", "")

            val isMatched = cond.test(arrayOf(keyNameStr, valueStr))
            if (isMatched) {
                //adv32.RegDeleteTreeW(hKeyRef.value,keyNameStr)
                println("DELETE： $keyNameStr")
            }
        }
        adv32.RegFlushKey(hKeyRef.value)
        adv32.RegCloseKey(hKeyRef.value)
        //CTF输入法注册表项需要使用链接，链接到真实目录中去删除真正的输入法
        //备份注册表：reg export HKEY_CURRENT_USER\Console C:\1.reg

    }

    fun immDelIfNameLike(vararg patterns: String) {
        imeDeleteByCond(
            Predicate { immKv ->
                patterns.any { pattern ->
                    immKv[1].matches(Regex(pattern))
                }
            })
    }

    fun immDelIfNameNotLike(vararg patterns: String) {
        imeDeleteByCond(Predicate { immKv -> patterns.none { pattern -> immKv[1].matches(Regex(pattern)) } })
    }

    fun immListInstalledIme() {

    }

    fun ImmListAvailableIme() {

    }

    fun ImmDeleteIme() {

    }

    fun ImmAddIme() {

    }


}

fun main() {
    Ime().immDelIfNameLike("^Danish$")
}