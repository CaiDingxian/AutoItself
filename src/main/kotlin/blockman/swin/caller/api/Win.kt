package blockman.swin.caller.api

import blockman.swin.caller.Caller.Companion.u32
import blockman.swin.caller.constants.Consts.Companion.WM_GETTEXT
import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WTypes
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import org.graalvm.polyglot.HostAccess
import java.lang.reflect.Method
import java.util.regex.Pattern

class Win {
    fun go(any: String)
    {

        val a=0
    }

    @HostAccess.Export
    fun findWin(className: String?, name: String?): Long? {
        return Pointer.nativeValue(u32.FindWindow(className, name).pointer)
    }

    @HostAccess.Export
    fun msgBox(title: String, content: String): Int {
        return u32.MessageBoxW(
            WinDef.HWND(null),
            WTypes.LPWSTR(title),
            WTypes.LPWSTR(content),
            WinDef.UINT(0x00000004L)
        )
    }

    fun getWinContent(hwnd: Long) {
        var strs = Native.malloc(Native.WCHAR_SIZE * 100L)
        u32.SendMessage(
            WinDef.HWND(Pointer(hwnd)), WM_GETTEXT, WinDef.WPARAM(16), WinDef.LPARAM(strs)
        )
        val b = WTypes.LPWSTR(Pointer(strs)).value
        Native.free(strs)
        print(b)
    }

    @HostAccess.Export
    fun setWinContent(hwnd: Long, text: String): Long {
        val WM_SETTEXT = 0x000C
        val peer = Pointer.createConstant(hwnd)
        val hwndObj = WinDef.HWND(peer)
        val p = Pointer.nativeValue(WTypes.LPWSTR(text).pointer)
        val ret = u32.SendMessage(
            hwndObj, WM_SETTEXT,
            WinDef.WPARAM(0), WinDef.LPARAM(p)
        )
        return ret.toLong()
    }


    fun genWinPath(hwnd: Long): MutableList<MutableMap<String, Any?>> {
        var gh: WinDef.HWND? = WinDef.HWND(Pointer(hwnd))
        val path = mutableListOf<MutableMap<String, Any?>>()
        while (gh != null) {
            val title = CharArray(200)
            u32.GetWindowText(gh, title, title.size)
            val clazz = CharArray(200)
            u32.GetClassName(gh, clazz, clazz.size)
            val map = mutableMapOf<String, Any?>(
                "hwnd" to gh,
                "title" to String(title).replace("\u0000", "") as String?,
                "clazz" to String(clazz).replace("\u0000", "") as String?
            )
            path.add(map)
            gh = u32.GetAncestor(gh, 1)

        }

        path.reverse()


        //从第二个节点开始，查询该节点是父节点的第几个指定类和标题的节点，填充到num值
        //如果只有一个节点，那么将不会填充num值
        for ((i, n) in path.withIndex()) {
            if (i == 0) continue//跳过第一个节点
            val chwnd = path[i]["hwnd"] as WinDef.HWND?
            val phwnd = path[i - 1]["hwnd"] as WinDef.HWND?
            val cClazz = path[i]["clazz"] as String?
            val cTitle = path[i]["title"] as String?
            //记录当前遍历到第几个指定类和标题的节点
            n["num"] = getWinQueryOrder(phwnd, chwnd, cClazz, cTitle)
        }
        return path
    }

    fun getWinQueryOrder(parent: WinDef.HWND?, targetChild: WinDef.HWND?, clazz: String?, title: String?): Int? {
        var order = 0
        var tempChild: WinDef.HWND? = null
        while (true) {
            tempChild = u32.FindWindowEx(parent, tempChild, clazz, title)
            if (tempChild == targetChild) {
                return order
            }
            if (tempChild == null) {
                return null
            }
            order++
        }
    }


}

fun main() {

    Win().getWinContent(6947888)

//    val h = u32.FindWindow(null, "下载")
//
    val desktop = u32.GetDesktopWindow()
//
//    val pathStr =
//        "class:[[SunAwtFrame]] title:[[TestModSys [D:\\DEV\\TestModSys0\\TestModSys] - ...\\src\\main\\kotlin\\blockman\\swin\\caller\\api\\Win.kt [TestModSys.main] - IntelliJ IDEA (Administrator)]] \n"
//    val pathStrs = pathStr.split("=>")
//    for (pStr in pathStrs) {
//        val c = Pattern.compile("class:\\[\\[(.*?)\\]\\]")
//        val m = c.matcher(pStr)
//        m.find()
//        val g = m.group(1)
//        val x = 0
//    }
    val win = Win()
    while (true) {
        Thread.sleep(500)
//        val a=win.getWinQueryOrder(WinDef.HWND(Pointer(2294674)),WinDef.HWND(Pointer(4064020)),"Edit","")
//
//        print(a)
        val guiInfo = WinUser.GUITHREADINFO()
        u32.GetGUIThreadInfo(0, guiInfo)
        val hwnd = guiInfo.hwndFocus
        if (hwnd != null) {
            val ml = win.genWinPath(Pointer.nativeValue(hwnd.pointer))
            for ((i, m) in ml.withIndex()) {

                if (m["hwnd"] != desktop) {
                    if (i != 1) print("=>")
                    //"{{"+m["title"] as String + " " + m["clazz"] + " " + m["num"]
                    var numStr = m["num"]
                    if (numStr == 0) numStr = ""
                    else numStr = "num:[[" + m["num"] + "]]"
                    print("class:[[${m["clazz"]}]] title:[[${m["title"]}]] " + numStr)
                }
            }
            println()
        }
    }


}