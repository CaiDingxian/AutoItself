package blockman.native.core.reg

import blockman.native.Libs.Companion.adv32
import com.oracle.truffle.api.`object`.HiddenKey
import com.sun.jna.platform.win32.Advapi32
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinReg
import com.sun.jna.ptr.IntByReference
import java.lang.RuntimeException
import java.util.function.Function

/*
* open(hkey,subPath)
* subKeys()
* values()
* eachSubKey{
*
* }
* eachValue
*
*
*
* */


fun main() {


}

class RegVal() {

}

class RegTpl {


    fun setValue() {

    }

    fun createKey() {
        //adv32.RegCreateKeyEx()
    }

    //取注册表值
    fun getVal(hKey: WinReg.HKEY, keyName: String, valueName: String) {
        val valueType = IntByReference()
        val value = ByteArray(1024)
        val valueSize = IntByReference(1024)

        val ret = adv32.RegGetValue(
            hKey,
            keyName,
            valueName,
            Advapi32.RRF_RT_ANY,
            valueType,
            value,
            valueSize
        )
    }

    fun getVals(hKey: WinReg.HKEY, subKey: String) {
        val preLoadHKeyRef = WinReg.HKEYByReference()
        val preLoadRet = adv32.RegOpenKeyEx(
            hKey, subKey, 0,
            WinNT.KEY_READ xor WinNT.KEY_WRITE xor WinNT.KEY_WOW64_64KEY, preLoadHKeyRef
        )

        var i = 0
        var plRet = 0
        val plValueMap = HashMap<String, String>()

        val plValueSize = IntByReference(40)//未考究原因，引用不能每轮调用都new一个，必须用同一个，并setValue为新值
        val plNameSize = IntByReference(40)
        while (plRet == 0) {
            val valueName = CharArray(100)
            //val plValueType = IntByReference()
            val valueData = ByteArray(80)
            val valueType = IntByReference()
            plNameSize.value = 40
            plValueSize.value = 40
            plRet = adv32.RegEnumValue(
                preLoadHKeyRef.value,
                i,
                valueName,
                plNameSize,
                null,
                valueType,
                valueData,
                plValueSize
            )
            i++
            val plNameStr = String(valueName).replace("\u0000", "")

        }

        for ((i, pl) in plValueMap) {
            adv32.RegDeleteValue(preLoadHKeyRef.value, i)
            val dataArr = pl
            //adv32.RegSetValueEx(preLoadHKeyRef.value,i.toString(),0,REG_SZ,dataArr,dataArr.size)
        }

        for ((i, pl) in plValueMap.values.withIndex()) {
            val v = pl.toCharArray()
            //adv32.RegSetValueEx(preLoadHKeyRef.value,i.toString(),0, REG_SZ,(pl+"\u0000").toCharArray(),(pl+"\u0000".toCharArray()).length*2)
            adv32.RegSetValueEx(preLoadHKeyRef.value, (i + 1).toString(), 0, WinNT.REG_SZ, v, (v.size) * 2)
        }

        adv32.RegCloseKey(preLoadHKeyRef.value)
    }

    //取得子键键名列表
    fun subKeys(hKey: WinReg.HKEY): List<String> {
        var i = 0
        var ret = 0
        val keyList = ArrayList<String>()
        val keyNameSize = IntByReference(1024)
        val keyName = CharArray(1024)
        while (ret == 0) {
            keyNameSize.value = 1024
            ret = adv32.RegEnumKeyEx(
                hKey, i, keyName, keyNameSize,
                null, null, null, null
            )
            i++
            val keyNameStr = String(keyName).replace("\u0000", "")
            keyList.add(keyNameStr)
        }
        return keyList
    }

    val regKeyMap = mapOf(
        "HKEY_CURRENT_USER" to WinReg.HKEY_CURRENT_USER,
        "HKEY_CURRENT_USER" to WinReg.HKEY_LOCAL_MACHINE,
        "HKEY_CURRENT_CONFIG" to WinReg.HKEY_CURRENT_CONFIG,
        "HKEY_CLASSES_ROOT" to WinReg.HKEY_CLASSES_ROOT,
        "HKEY_USERS" to WinReg.HKEY_USERS
    )

    fun open(path: String): WinReg.HKEY {
        lateinit var rootPath: WinReg.HKEY
        var subPath = ""
        if (!regKeyMap.none { path.startsWith(it.key) }) throw RuntimeException("不支持的注册表路径")
        regKeyMap.forEach {
            if (path.startsWith(it.key)) {
                rootPath = regKeyMap[it.key]!!
                subPath = path.replaceFirst(it.key + "\\", "")
            }
        }
        val hKeyRef: WinReg.HKEYByReference? = WinReg.HKEYByReference()
        adv32.RegOpenKeyEx(rootPath, subPath, 0, 0, hKeyRef)
        return hKeyRef!!.value
    }

    fun flush(hKey: WinReg.HKEY): Int {
        return adv32.RegFlushKey(hKey)
    }

    fun close(hKey: WinReg.HKEY): Int {
        return adv32.RegCloseKey(hKey)
    }

    fun exec(path: String, f: (WinReg.HKEY) -> Any) {
        val hkey = open(path)
        f.invoke(hkey)
        flush(hkey)
        close(hkey)
    }

}

class Reg {


}