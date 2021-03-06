package blockman.native.api

import blockman.native.Libs.Companion.u32
import blockman.native.constants.Consts.Companion.WM_GETTEXT
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WTypes
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import org.graalvm.polyglot.HostAccess
import java.util.regex.Pattern

class Win {
    fun go(any: String)
    {

        val a=0
    }

    @HostAccess.Export
    fun getTopWin(): Long? {
        return u32.GetForegroundWindow()?.let{
            Pointer.nativeValue(it.pointer)
        }

    }

    @HostAccess.Export
    fun getFocusWin(): Long? {
        return u32.GetFocus()?.let{
            Pointer.nativeValue(it.pointer)
        }

    }

    @HostAccess.Export
    fun getActiveWin(): Long? {
        return u32.GetActiveWindow()?.let{
                Pointer.nativeValue(it.pointer)
            }
    }

    @HostAccess.Export
    fun findWin(className: String?, name: String?): Long? {
        return u32.FindWindow(className, name)?.let{
            Pointer.nativeValue(it.pointer)
        }
    }

    @HostAccess.Export
    fun findWinEx(hwnd: Long?, chwnd: Long?, className: String?, name: String?): Long? {
        return u32.FindWindowEx(
            hwnd?.let { WinDef.HWND(Pointer(it)) },
            chwnd?.let { WinDef.HWND(Pointer(it)) },
            className, name
        )?.let { Pointer.nativeValue(it.pointer) }

    }

    fun closeWin(hwnd: Long): Boolean {
        return u32.CloseWindow(WinDef.HWND(Pointer(hwnd)))
    }

    fun QueryWin(qStr: String): Long? {

        return null
    }

    fun getWinTitle(hwnd: Long): String {
        val str = CharArray(100)
        u32.GetWindowText(WinDef.HWND(Pointer(hwnd)), str, 100)
        return String(str).replace("\u0000", "")
    }

    @HostAccess.Export
    fun msgBox(title: String, content: String, boxType: Long): Int {
        return u32.MessageBoxW(
            WinDef.HWND(null),
            WTypes.LPWSTR(title),
            WTypes.LPWSTR(content),
            WinDef.UINT(boxType)
        )
    }

    fun getWinContent(hwnd: Long): String? {
        var strs = Native.malloc(Native.WCHAR_SIZE * 100L)
        u32.SendMessage(
            WinDef.HWND(Pointer(hwnd)), WM_GETTEXT, WinDef.WPARAM(16), WinDef.LPARAM(strs)
        )
        val b = WTypes.LPWSTR(Pointer(strs)).value
        Native.free(strs)
        return b
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


        //?????????????????????????????????????????????????????????????????????????????????????????????????????????num???
        //????????????????????????????????????????????????num???
        for ((i, n) in path.withIndex()) {
            if (i == 0) continue//?????????????????????
            val chwnd = path[i]["hwnd"] as WinDef.HWND?
            val phwnd = path[i - 1]["hwnd"] as WinDef.HWND?
            val cClazz = path[i]["clazz"] as String?
            val cTitle = path[i]["title"] as String?
            //?????????????????????????????????????????????????????????
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

    fun genWinQueryStr(hwndNum: Long): String {
        var q = ""
        val desktop = u32.GetDesktopWindow()

        //val hwnd = guiInfo.hwndFocus
        val hwnd = WinDef.HWND(Pointer(hwndNum))
        if (hwnd != null) {
            val ml = genWinPath(Pointer.nativeValue(hwnd.pointer))
            for ((i, m) in ml.withIndex()) {

                if (m["hwnd"] != desktop) {
                    if (i != 1) q += "=>"
                    //"{{"+m["title"] as String + " " + m["clazz"] + " " + m["num"]
                    var numStr = m["num"]
                    if (numStr == 0) numStr = ""
                    else numStr = "num:[[" + m["num"] + "]]"
                    q += "class:[[${m["clazz"]}]] title:[[${m["title"]}]] " + numStr
                }
            }
            println(q)
        }
        return q
    }

    fun getWinByQueryStr(pathStr:String)
    {
    val pathStr = "class:[[SunAwtFrame]] title:[[TestModSys [D:\\DEV\\TestModSys0\\TestModSys] - ...\\src\\main\\kotlin\\blockman\\swin\\caller\\api\\Win.kt [TestModSys.main] - IntelliJ IDEA (Administrator)]] \n"
    val pathStrs = pathStr.split("=>")
    for (pStr in pathStrs) {
        val c = Pattern.compile("class:\\[\\[(.*?)\\]\\]")
        val t = Pattern.compile("title:\\[\\[(.*?)\\]\\]")
        val n =Pattern.compile("num:\\[\\[(.*?)\\]\\]")
        val cm = c.matcher(pStr)
        val tm=t.matcher(pStr)
        val nm=t.matcher(pStr)
        cm.find()
        tm.find()
        nm.find()
        val g = cm.group(1)
        val x = 0
    }
    }


}

fun main() {

    //Win().getWinContent(6947888)

//    val h = u32.FindWindow(null, "??????")
//

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
        val guiInfo = WinUser.GUITHREADINFO()
        u32.GetGUIThreadInfo(0, guiInfo)
        val q = win.genWinQueryStr(Pointer.nativeValue(guiInfo.hwndFocus?.pointer))
        print(q)

    }


}
