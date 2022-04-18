package blockman.server.app

import blockman.native.Libs.Companion.u32
import blockman.native.api.Input
import blockman.native.api.Win
import blockman.native.core.cmd.Cmd
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import org.luaj.vm2.lib.jse.LuaEngine
import org.springframework.core.io.ClassPathResource

class Cli {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
//            val eng = LuaEngine()
//            eng.runLua(ClassPathResource("lua/index.lua").path)

            val cmd=Cmd()
            cmd.commit("\"D:\\Program Files (x86)\\VMware\\Infrastructure\\Virtual Infrastructure Client\\Launcher\\VpxClient.exe\"", arrayOf())
            Thread.sleep(1000)
          var chwnd:WinDef.HWND? =null
            val win=Win()
            var vmWin=u32.FindWindow(
                null,
                "VMware vSphere Client"
            )

            var p:Pointer?=null

            var loginBtn:WinDef.HWND?=null

            var i=0
             var edit=u32.EnumChildWindows(vmWin,
                 {
                         h,data->
                     val str=win.getWinContent(Pointer.nativeValue(h.pointer))
                     val title=win.getWinTitle(Pointer.nativeValue(h.pointer))
                     println(i.toString()+":"+str+" "+title)
                     if(i==15){
                         win.setWinContent(Pointer.nativeValue(h.pointer),"administrator@vsphere.local")
                     }
                     if(i==14){
                         win.setWinContent(Pointer.nativeValue(h.pointer),"Xyytmx.;12")
                     }
                     if(i==8){
                         loginBtn=h
                     }
                     i++
                     true
                 }
            ,p)

            val input=Input()
            input.msgClick(Pointer.nativeValue(loginBtn!!.pointer),1,1)

        }
    }
}
