package blockman.swin.caller


import blockman.swin.caller.ext.Imm32Ext
import blockman.swin.caller.ext.K32Ext
import blockman.swin.caller.ext.U32Ext
import com.sun.jna.Native
import com.sun.jna.win32.W32APIOptions

class Caller {
    companion object {
        @JvmStatic
        val u32 = Native.load("user32", U32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as U32Ext
        @JvmStatic
        val imm32 = Native.load("imm32.dll", Imm32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as Imm32Ext
        @JvmStatic
        val k32 = Native.load("kernel32", K32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as K32Ext
    }
}