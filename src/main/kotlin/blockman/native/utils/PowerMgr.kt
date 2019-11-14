package blockman.native.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.regex.Pattern

object PowerManager {
    @Throws(IOException::class, InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        //         var x = Kernel32Util.getLogicalProcessorInformation();
        //         win32
        //
        val runtime = Runtime.getRuntime()
        val p = runtime.exec("cmd")

        val sw = OutputStreamWriter(p.outputStream)
        sw.write("chcp 65001 \r\n")
        sw.write("powercfg /list \r\n")

        sw.flush()
        val r = BufferedReader(InputStreamReader(p.inputStream))
        val str = r.readLine()
//        println(str)
//        while (true)
//        {
//            val cstr=CharArray(100)
//            val b=r.read(cstr)
//            if(b<cstr.size)
//            {
//                print(String(cstr))
//            }
//        }
        while (true) {
            val l = r.readLine()
            println(l)
            Thread.sleep(1000)
            if (l.matches(".*高性能.*".toRegex())) {
                val pattern = Pattern.compile("GUID:\\s(.*)\\s\\s").matcher(l)
                pattern.find()
                val guid = pattern.group(1)
                println("powercfg /s $guid\r\n")
                sw.write("powercfg /s $guid\r\n")
                sw.flush()
                break;
            }
        }

    }
}