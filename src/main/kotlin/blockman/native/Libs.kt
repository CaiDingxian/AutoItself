package blockman.native


import blockman.native.call.Adv32Ext
import blockman.native.call.Imm32Ext
import blockman.native.call.K32Ext
import blockman.native.call.User32Ext
import com.sun.jna.Native
import com.sun.jna.win32.W32APIOptions

class Libs {
    companion object {
        @JvmStatic
        val u32 = Native.load("user32", User32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as User32Ext
        @JvmStatic
        val imm32 = Native.load("imm32.dll", Imm32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as Imm32Ext
        @JvmStatic
        val k32 = Native.load("kernel32", K32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as K32Ext
        @JvmStatic
        val adv32 = Native.load("advapi32", Adv32Ext::class.java, W32APIOptions.DEFAULT_OPTIONS) as Adv32Ext

    }
}