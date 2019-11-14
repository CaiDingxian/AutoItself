package blockman.native.core.reg

import blockman.native.Libs.Companion.adv32
import com.sun.jna.platform.win32.WinReg
import com.sun.jna.ptr.IntByReference
import kotlin.jvm.internal.Ref


fun main() {

    RegTpl().exec({ x ->
        //adv32.RegEnumKeyEx(x,)

    })
}

class RegTpl {

    fun setValue() {

    }

    fun getValue() {

    }

    fun CreateKey() {
        //adv32.RegCreateKeyEx()
    }


    fun forEachKeys() {
        val hKey: WinReg.HKEY = WinReg.HKEY()
        var keyName = CharArray(100)
        var enumRet = 0
        var i = 0
        while (enumRet == 0) {
            val lpcName = IntByReference(140)
            val name = CharArray(300)
            enumRet = adv32.RegEnumKeyEx(
                hKey, i, name, lpcName,
                null, null, null, null
            )
        }
    }

    fun forEachValues() {
        val hKey: WinReg.HKEY = WinReg.HKEY()
        var i = 0
        var plRet = 0

        //未考究原因，引用不能每轮调用都new一个，必须用同一个，并setValue为新值
        val plValueSize = IntByReference(40)
        val plNameSize = IntByReference(40)
        while (plRet == 0) {
            val tempPreLoadValue = CharArray(100)
            //val plValueType = IntByReference()
            val plValue = ByteArray(80)
            plNameSize.value = 40
            plValueSize.value = 40
            plRet = adv32.RegEnumValue(
                hKey,
                i,
                tempPreLoadValue,
                plNameSize,
                null,
                null,
                plValue,
                plValueSize
            )
            i++
            val plNameStr = String(tempPreLoadValue).replace("\u0000", "")
        }
    }


    fun exec(f: (WinReg.HKEY) -> Any) {
        val hKeyRef = WinReg.HKEYByReference()
        adv32.RegOpenKeyEx(WinReg.HKEY_CURRENT_USER, "", 0, 0, hKeyRef)
        f.invoke(hKeyRef.value)
        adv32.RegFlushKey(hKeyRef.value)
        adv32.RegCloseKey(hKeyRef.value)
    }
}

class Reg {


}